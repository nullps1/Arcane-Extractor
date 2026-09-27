package dev.nullps1.arcaneextraction.blockentity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.junit.jupiter.api.Test;

class ArcaneExtractorTransactionTest {
    @Test
    void successfulExtractionConsumesOneBookAndUsesFirstOutputSlot() {
        Holder<Enchantment> efficiency = testEnchantment("efficiency", 5);
        Holder<Enchantment> unbreaking = testEnchantment("unbreaking", 5);

        ArcaneExtractorInventory inventory = new ArcaneExtractorInventory(() -> {});
        ItemStack source = new ItemStack(Items.DIAMOND_PICKAXE);
        source.enchant(efficiency, 5);
        source.enchant(unbreaking, 3);

        inventory.setStackInSlot(ArcaneExtractorInventory.SOURCE_SLOT, source);
        inventory.setStackInSlot(ArcaneExtractorInventory.BOOK_SLOT, new ItemStack(Items.BOOK, 2));

        ArcaneExtractorTransaction.Success success = assertInstanceOf(
                ArcaneExtractorTransaction.Success.class,
                ArcaneExtractorTransaction.extract(inventory, unbreaking));

        ItemEnchantments remaining = inventory.getStackInSlot(ArcaneExtractorInventory.SOURCE_SLOT)
                .getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        ItemEnchantments stored = inventory.getStackInSlot(ArcaneExtractorInventory.OUTPUT_START)
                .getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);

        assertEquals(5, remaining.getLevel(efficiency));
        assertEquals(0, remaining.getLevel(unbreaking));
        assertEquals(1, inventory.getStackInSlot(ArcaneExtractorInventory.BOOK_SLOT).getCount());
        assertEquals(3, stored.getLevel(unbreaking));
        assertEquals(ArcaneExtractorInventory.OUTPUT_START, success.outputSlot());
    }

    @Test
    void repeatedExtractionUsesNextFreeOutputWithoutClearingPreviousBook() {
        Holder<Enchantment> efficiency = testEnchantment("efficiency", 5);
        Holder<Enchantment> unbreaking = testEnchantment("unbreaking", 5);

        ArcaneExtractorInventory inventory = new ArcaneExtractorInventory(() -> {});
        ItemStack source = new ItemStack(Items.DIAMOND_PICKAXE);
        source.enchant(efficiency, 5);
        source.enchant(unbreaking, 3);

        inventory.setStackInSlot(ArcaneExtractorInventory.SOURCE_SLOT, source);
        inventory.setStackInSlot(ArcaneExtractorInventory.BOOK_SLOT, new ItemStack(Items.BOOK, 2));

        ArcaneExtractorTransaction.Success first = assertInstanceOf(
                ArcaneExtractorTransaction.Success.class,
                ArcaneExtractorTransaction.extract(inventory, efficiency));
        ArcaneExtractorTransaction.Success second = assertInstanceOf(
                ArcaneExtractorTransaction.Success.class,
                ArcaneExtractorTransaction.extract(inventory, unbreaking));

        assertEquals(ArcaneExtractorInventory.OUTPUT_START, first.outputSlot());
        assertEquals(ArcaneExtractorInventory.OUTPUT_START + 1, second.outputSlot());
        assertTrue(inventory.getStackInSlot(ArcaneExtractorInventory.BOOK_SLOT).isEmpty());
        assertEquals(0, inventory.getStackInSlot(ArcaneExtractorInventory.SOURCE_SLOT)
                .getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
                .size());
    }

    @Test
    void missingBookRejectsWithoutChangingInventory() {
        Holder<Enchantment> efficiency = testEnchantment("efficiency", 5);

        ArcaneExtractorInventory inventory = new ArcaneExtractorInventory(() -> {});
        ItemStack source = new ItemStack(Items.DIAMOND_PICKAXE);
        source.enchant(efficiency, 5);
        inventory.setStackInSlot(ArcaneExtractorInventory.SOURCE_SLOT, source.copy());

        ArcaneExtractorTransaction.Failure failure = assertInstanceOf(
                ArcaneExtractorTransaction.Failure.class,
                ArcaneExtractorTransaction.extract(inventory, efficiency));

        assertEquals(ArcaneExtractorTransaction.FailureReason.MISSING_BOOK, failure.reason());
        assertEquals(5, inventory.getStackInSlot(ArcaneExtractorInventory.SOURCE_SLOT)
                .getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
                .getLevel(efficiency));
        assertTrue(inventory.getStackInSlot(ArcaneExtractorInventory.OUTPUT_START).isEmpty());
    }

    @Test
    void fullOutputBufferRejectsWithoutConsumingBook() {
        Holder<Enchantment> efficiency = testEnchantment("efficiency", 5);

        ArcaneExtractorInventory inventory = new ArcaneExtractorInventory(() -> {});
        ItemStack source = new ItemStack(Items.DIAMOND_PICKAXE);
        source.enchant(efficiency, 5);

        inventory.setStackInSlot(ArcaneExtractorInventory.SOURCE_SLOT, source);
        inventory.setStackInSlot(ArcaneExtractorInventory.BOOK_SLOT, new ItemStack(Items.BOOK, 2));
        for (int slot = ArcaneExtractorInventory.OUTPUT_START;
                slot < ArcaneExtractorInventory.OUTPUT_END;
                slot++) {
            inventory.setStackInSlot(slot, new ItemStack(Items.ENCHANTED_BOOK));
        }

        ArcaneExtractorTransaction.Failure failure = assertInstanceOf(
                ArcaneExtractorTransaction.Failure.class,
                ArcaneExtractorTransaction.extract(inventory, efficiency));

        assertEquals(ArcaneExtractorTransaction.FailureReason.OUTPUT_FULL, failure.reason());
        assertEquals(2, inventory.getStackInSlot(ArcaneExtractorInventory.BOOK_SLOT).getCount());
        assertEquals(5, inventory.getStackInSlot(ArcaneExtractorInventory.SOURCE_SLOT)
                .getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
                .getLevel(efficiency));
    }

    @Test
    void slotValidationMatchesBufferedMachineContract() {
        Holder<Enchantment> efficiency = testEnchantment("efficiency", 5);
        ItemStack enchantedTool = new ItemStack(Items.DIAMOND_PICKAXE);
        enchantedTool.enchant(efficiency, 1);

        ArcaneExtractorInventory inventory = new ArcaneExtractorInventory(() -> {});

        assertEquals(8, inventory.getSlots());
        assertTrue(inventory.isItemValid(ArcaneExtractorInventory.SOURCE_SLOT, enchantedTool));
        assertTrue(inventory.isItemValid(ArcaneExtractorInventory.BOOK_SLOT, new ItemStack(Items.BOOK)));
        for (int slot = ArcaneExtractorInventory.OUTPUT_START;
                slot < ArcaneExtractorInventory.OUTPUT_END;
                slot++) {
            assertTrue(!inventory.isItemValid(slot, new ItemStack(Items.BOOK)));
        }
    }

    private static Holder<Enchantment> testEnchantment(String name, int maxLevel) {
        var supportedItems = HolderSet.direct(
                BuiltInRegistries.ITEM.wrapAsHolder(Items.DIAMOND_PICKAXE));
        var definition = Enchantment.definition(
                supportedItems,
                10,
                maxLevel,
                Enchantment.constantCost(1),
                Enchantment.constantCost(1),
                1,
                EquipmentSlotGroup.MAINHAND);

        return Holder.direct(new Enchantment(
                Component.literal(name),
                definition,
                HolderSet.empty(),
                DataComponentMap.EMPTY));
    }
}
