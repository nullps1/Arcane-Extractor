package dev.nullps1.arcaneextraction.extraction;

import java.util.Objects;
import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;

public record ExtractionCandidate(Holder<Enchantment> enchantment, int level) {
    public ExtractionCandidate {
        Objects.requireNonNull(enchantment, "enchantment");
        if (level <= 0) {
            throw new IllegalArgumentException("level must be positive");
        }
    }
}
