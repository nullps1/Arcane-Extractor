package dev.nullps1.arcaneextraction.extraction;

import java.util.Objects;
import net.minecraft.world.item.ItemStack;

public sealed interface ExtractionResult permits ExtractionResult.Success, ExtractionResult.Failure {
    record Success(ItemStack source, ItemStack enchantedBook, ExtractionCandidate extracted) implements ExtractionResult {
        public Success {
            Objects.requireNonNull(source, "source");
            Objects.requireNonNull(enchantedBook, "enchantedBook");
            Objects.requireNonNull(extracted, "extracted");
        }
    }

    record Failure(ExtractionFailure reason) implements ExtractionResult {
        public Failure {
            Objects.requireNonNull(reason, "reason");
        }
    }
}
