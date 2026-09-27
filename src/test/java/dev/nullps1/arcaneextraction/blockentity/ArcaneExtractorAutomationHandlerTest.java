package dev.nullps1.arcaneextraction.blockentity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import org.junit.jupiter.api.Test;

class ArcaneExtractorAutomationHandlerTest {
    @Test
    void automationCanInsertValidSourceAndBooks() {
        ArcaneExtractorInventory inventory = new ArcaneExtractorInventory(() -> {});
        ArcaneExtractorAutomationHandler automation = new ArcaneExtractorAutomationHandler(inventory);

        ItemStack source = new ItemStack(Items.DIAMOND_PICKAXE);
        source.enchant(testEnchantment("automation", 1), 1);

        assertTrue(automation.insertItem(ArcaneExtractorInventory.SOURCE_SLOT, source, false).isEmpty());
        assertTrue(automation.insertItem(
                ArcaneExtractorInventory.BOOK_SLOT,
                new ItemStack(Items.BOOK, 3),
                false).isEmpty());

        assertEquals(1, inventory.getStackInSlot(ArcaneExtractorInventory.SOURCE_SLOT).getCount());
        assertEquals(3, inventory.getStackInSlot(ArcaneExtractorInventory.BOOK_SLOT).getCount());
    }

    @Test
    void automationCannotInsertIntoOutput() {
        ArcaneExtractorInventory inventory = new ArcaneExtractorInventory(() -> {});
        ArcaneExtractorAutomationHandler automation = new ArcaneExtractorAutomationHandler(inventory);
        ItemStack attempted = new ItemStack(Items.ENCHANTED_BOOK);

        ItemStack remainder = automation.insertItem(
                ArcaneExtractorInventory.OUTPUT_START,
                attempted,
                false);

        assertEquals(1, remainder.getCount());
        assertTrue(inventory.getStackInSlot(ArcaneExtractorInventory.OUTPUT_START).isEmpty());
    }

    @Test
    void automationCannotExtractInputs() {
        ArcaneExtractorInventory inventory = new ArcaneExtractorInventory(() -> {});
        ArcaneExtractorAutomationHandler automation = new ArcaneExtractorAutomationHandler(inventory);

        ItemStack source = new ItemStack(Items.DIAMOND_PICKAXE);
        source.enchant(testEnchantment("automation", 1), 1);
        inventory.setStackInSlot(ArcaneExtractorInventory.SOURCE_SLOT, source);
        inventory.setStackInSlot(ArcaneExtractorInventory.BOOK_SLOT, new ItemStack(Items.BOOK, 4));

        assertTrue(automation.extractItem(ArcaneExtractorInventory.SOURCE_SLOT, 1, false).isEmpty());
        assertTrue(automation.extractItem(ArcaneExtractorInventory.BOOK_SLOT, 1, false).isEmpty());
        assertEquals(1, inventory.getStackInSlot(ArcaneExtractorInventory.SOURCE_SLOT).getCount());
        assertEquals(4, inventory.getStackInSlot(ArcaneExtractorInventory.BOOK_SLOT).getCount());
    }

    @Test
    void automationCanExtractOutput() {
        ArcaneExtractorInventory inventory = new ArcaneExtractorInventory(() -> {});
        ArcaneExtractorAutomationHandler automation = new ArcaneExtractorAutomationHandler(inventory);
        inventory.setStackInSlot(
                ArcaneExtractorInventory.OUTPUT_START,
                new ItemStack(Items.ENCHANTED_BOOK));

        ItemStack extracted = automation.extractItem(
                ArcaneExtractorInventory.OUTPUT_START,
                1,
                false);

        assertTrue(extracted.is(Items.ENCHANTED_BOOK));
        assertTrue(inventory.getStackInSlot(ArcaneExtractorInventory.OUTPUT_START).isEmpty());
    }

    @Test
    void automationCanExtractFromAnyBufferedOutputSlot() {
        ArcaneExtractorInventory inventory = new ArcaneExtractorInventory(() -> {});
        ArcaneExtractorAutomationHandler automation = new ArcaneExtractorAutomationHandler(inventory);
        int lastOutput = ArcaneExtractorInventory.OUTPUT_END - 1;
        inventory.setStackInSlot(lastOutput, new ItemStack(Items.ENCHANTED_BOOK));

        ItemStack extracted = automation.extractItem(lastOutput, 1, false);

        assertTrue(extracted.is(Items.ENCHANTED_BOOK));
        assertTrue(inventory.getStackInSlot(lastOutput).isEmpty());
    }

    @Test
    void simulationDoesNotMutateAutomationInventory() {
        ArcaneExtractorInventory inventory = new ArcaneExtractorInventory(() -> {});
        ArcaneExtractorAutomationHandler automation = new ArcaneExtractorAutomationHandler(inventory);

        ItemStack books = new ItemStack(Items.BOOK, 2);
        ItemStack remainder = automation.insertItem(
                ArcaneExtractorInventory.BOOK_SLOT,
                books,
                true);

        assertTrue(remainder.isEmpty());
        assertTrue(inventory.getStackInSlot(ArcaneExtractorInventory.BOOK_SLOT).isEmpty());
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
