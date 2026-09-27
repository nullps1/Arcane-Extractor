package dev.nullps1.arcaneextraction.extraction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
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

class EnchantmentExtractorTest {
    @Test
    void candidatesExposeEveryEnchantmentAndExactLevel() {
        Holder<Enchantment> efficiency = testEnchantment("efficiency", 5);
        Holder<Enchantment> unbreaking = testEnchantment("unbreaking", 5);

        ItemStack source = new ItemStack(Items.DIAMOND_PICKAXE);
        source.enchant(efficiency, 5);
        source.enchant(unbreaking, 3);

        List<ExtractionCandidate> candidates = EnchantmentExtractor.candidates(source);

        assertEquals(2, candidates.size());
        assertTrue(candidates.contains(new ExtractionCandidate(efficiency, 5)));
        assertTrue(candidates.contains(new ExtractionCandidate(unbreaking, 3)));
    }

    @Test
    void extractRemovesOnlySelectedEnchantmentAndPreservesOtherComponents() {
        Holder<Enchantment> efficiency = testEnchantment("efficiency", 5);
        Holder<Enchantment> unbreaking = testEnchantment("unbreaking", 5);

        ItemStack source = new ItemStack(Items.DIAMOND_PICKAXE);
        source.enchant(efficiency, 5);
        source.enchant(unbreaking, 3);
        source.set(DataComponents.CUSTOM_NAME, Component.literal("Old Faithful"));
        source.set(DataComponents.DAMAGE, 42);

        ExtractionResult.Success success = assertInstanceOf(
                ExtractionResult.Success.class,
                EnchantmentExtractor.extract(source, unbreaking));

        ItemEnchantments remaining =
                success.source().getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        ItemEnchantments stored =
                success.enchantedBook().getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY);

        assertEquals(5, remaining.getLevel(efficiency));
        assertEquals(0, remaining.getLevel(unbreaking));
        assertEquals(3, stored.getLevel(unbreaking));
        assertEquals(1, stored.size());
        assertEquals(Component.literal("Old Faithful"), success.source().get(DataComponents.CUSTOM_NAME));
        assertEquals(42, success.source().getOrDefault(DataComponents.DAMAGE, 0));
        assertTrue(success.enchantedBook().is(Items.ENCHANTED_BOOK));
        assertEquals(new ExtractionCandidate(unbreaking, 3), success.extracted());
    }

    @Test
    void extractDoesNotMutateInputStack() {
        Holder<Enchantment> efficiency = testEnchantment("efficiency", 5);
        Holder<Enchantment> unbreaking = testEnchantment("unbreaking", 5);

        ItemStack source = new ItemStack(Items.DIAMOND_PICKAXE);
        source.enchant(efficiency, 5);
        source.enchant(unbreaking, 3);

        EnchantmentExtractor.extract(source, unbreaking);

        ItemEnchantments original =
                source.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);
        assertEquals(5, original.getLevel(efficiency));
        assertEquals(3, original.getLevel(unbreaking));
    }

    @Test
    void missingSelectionFailsWithoutMutation() {
        Holder<Enchantment> efficiency = testEnchantment("efficiency", 5);
        Holder<Enchantment> unbreaking = testEnchantment("unbreaking", 5);

        ItemStack source = new ItemStack(Items.DIAMOND_PICKAXE);
        source.enchant(efficiency, 5);

        ExtractionResult.Failure failure = assertInstanceOf(
                ExtractionResult.Failure.class,
                EnchantmentExtractor.extract(source, unbreaking));

        assertEquals(ExtractionFailure.ENCHANTMENT_NOT_PRESENT, failure.reason());
        assertEquals(5, source.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY)
                .getLevel(efficiency));
    }

    @Test
    void unenchantedSourceFails() {
        ExtractionResult.Failure failure = assertInstanceOf(
                ExtractionResult.Failure.class,
                EnchantmentExtractor.extract(
                        new ItemStack(Items.DIAMOND_PICKAXE),
                        testEnchantment("efficiency", 5)));

        assertEquals(ExtractionFailure.SOURCE_NOT_ENCHANTED, failure.reason());
    }

    @Test
    void arbitraryDataDrivenEnchantmentRequiresNoAllowlist() {
        Holder<Enchantment> modded = testEnchantment("modded:void_step", 3);

        ItemStack source = new ItemStack(Items.DIAMOND_PICKAXE);
        source.enchant(modded, 2);

        ExtractionResult.Success success = assertInstanceOf(
                ExtractionResult.Success.class,
                EnchantmentExtractor.extract(source, modded));

        assertEquals(2, success.enchantedBook()
                .getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)
                .getLevel(modded));
    }

    @Test
    void aboveDeclaredMaxLevelIsPreservedExactly() {
        Holder<Enchantment> overlevelled = testEnchantment("overlevelled", 5);

        ItemStack source = new ItemStack(Items.DIAMOND_PICKAXE);
        source.enchant(overlevelled, 12);

        ExtractionResult.Success success = assertInstanceOf(
                ExtractionResult.Success.class,
                EnchantmentExtractor.extract(source, overlevelled));

        assertEquals(12, success.extracted().level());
        assertEquals(12, success.enchantedBook()
                .getOrDefault(DataComponents.STORED_ENCHANTMENTS, ItemEnchantments.EMPTY)
                .getLevel(overlevelled));
    }

    @Test
    void unrelatedComponentsSurviveCompatibilityExtractionExceptRepairCost() {
        Holder<Enchantment> modded = testEnchantment("modded:oddity", 1);

        ItemStack source = new ItemStack(Items.DIAMOND_PICKAXE);
        source.enchant(modded, 1);
        source.set(DataComponents.CUSTOM_NAME, Component.literal("Component Soup"));
        source.set(DataComponents.REPAIR_COST, 19);
        source.set(DataComponents.DAMAGE, 77);

        ExtractionResult.Success success = assertInstanceOf(
                ExtractionResult.Success.class,
                EnchantmentExtractor.extract(source, modded));

        assertEquals(Component.literal("Component Soup"), success.source().get(DataComponents.CUSTOM_NAME));
        assertEquals(0, success.source().getOrDefault(DataComponents.REPAIR_COST, 0));
        assertEquals(77, success.source().getOrDefault(DataComponents.DAMAGE, 0));
        assertEquals(19, source.getOrDefault(DataComponents.REPAIR_COST, 0));
    }

    @Test
    void failedExtractionDoesNotResetRepairCost() {
        Holder<Enchantment> present = testEnchantment("present", 1);
        Holder<Enchantment> missing = testEnchantment("missing", 1);

        ItemStack source = new ItemStack(Items.DIAMOND_PICKAXE);
        source.enchant(present, 1);
        source.set(DataComponents.REPAIR_COST, 37);

        ExtractionResult.Failure failure = assertInstanceOf(
                ExtractionResult.Failure.class,
                EnchantmentExtractor.extract(source, missing));

        assertEquals(ExtractionFailure.ENCHANTMENT_NOT_PRESENT, failure.reason());
        assertEquals(37, source.getOrDefault(DataComponents.REPAIR_COST, 0));
    }

    @Test
    void emptySourceFailsBeforeSelectionValidation() {
        ExtractionResult.Failure failure = assertInstanceOf(
                ExtractionResult.Failure.class,
                EnchantmentExtractor.extract(ItemStack.EMPTY, null));

        assertEquals(ExtractionFailure.EMPTY_SOURCE, failure.reason());
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
