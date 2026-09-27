package dev.nullps1.arcaneextraction.extraction;

import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public final class EnchantmentExtractor {
    private EnchantmentExtractor() {
    }

    public static List<ExtractionCandidate> candidates(ItemStack source) {
        if (source == null || source.isEmpty()) {
            return List.of();
        }

        ItemEnchantments enchantments =
                source.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        return enchantments.keySet().stream()
                .map(enchantment -> new ExtractionCandidate(
                        enchantment,
                        enchantments.getLevel(enchantment)))
                .toList();
    }

    public static ExtractionResult extract(ItemStack source, Holder<Enchantment> selected) {
        if (source == null || source.isEmpty()) {
            return new ExtractionResult.Failure(ExtractionFailure.EMPTY_SOURCE);
        }

        if (selected == null) {
            return new ExtractionResult.Failure(ExtractionFailure.INVALID_SELECTION);
        }

        ItemEnchantments enchantments =
                source.getOrDefault(DataComponents.ENCHANTMENTS, ItemEnchantments.EMPTY);

        if (enchantments.isEmpty()) {
            return new ExtractionResult.Failure(ExtractionFailure.SOURCE_NOT_ENCHANTED);
        }

        int level = enchantments.getLevel(selected);
        if (level <= 0) {
            return new ExtractionResult.Failure(ExtractionFailure.ENCHANTMENT_NOT_PRESENT);
        }

        ItemStack cleanedSource = source.copy();
        ItemEnchantments.Mutable remaining = new ItemEnchantments.Mutable(enchantments);
        remaining.removeIf(selected::equals);
        cleanedSource.set(DataComponents.ENCHANTMENTS, remaining.toImmutable());
        cleanedSource.remove(DataComponents.REPAIR_COST);

        ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
        ItemEnchantments.Mutable stored = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        stored.set(selected, level);
        enchantedBook.set(DataComponents.STORED_ENCHANTMENTS, stored.toImmutable());

        return new ExtractionResult.Success(
                cleanedSource,
                enchantedBook,
                new ExtractionCandidate(selected, level));
    }
}
