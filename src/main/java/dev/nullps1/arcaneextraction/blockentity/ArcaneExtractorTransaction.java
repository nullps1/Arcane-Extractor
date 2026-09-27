package dev.nullps1.arcaneextraction.blockentity;

import dev.nullps1.arcaneextraction.extraction.EnchantmentExtractor;
import dev.nullps1.arcaneextraction.extraction.ExtractionFailure;
import dev.nullps1.arcaneextraction.extraction.ExtractionResult;
import java.util.Objects;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public final class ArcaneExtractorTransaction {
    private ArcaneExtractorTransaction() {}

    public static Result extract(IItemHandlerModifiable inventory, Holder<Enchantment> selected) {
        Objects.requireNonNull(inventory, "inventory");

        ItemStack source = inventory.getStackInSlot(ArcaneExtractorInventory.SOURCE_SLOT);
        ItemStack book = inventory.getStackInSlot(ArcaneExtractorInventory.BOOK_SLOT);
        ItemStack output = inventory.getStackInSlot(ArcaneExtractorInventory.OUTPUT_SLOT);

        if (!book.is(Items.BOOK) || book.isEmpty()) {
            return new Failure(FailureReason.MISSING_BOOK, null);
        }
        if (!output.isEmpty()) {
            return new Failure(FailureReason.OUTPUT_OCCUPIED, null);
        }

        ExtractionResult extraction = EnchantmentExtractor.extract(source, selected);
        if (extraction instanceof ExtractionResult.Failure failure) {
            return new Failure(FailureReason.EXTRACTION_REJECTED, failure.reason());
        }

        ExtractionResult.Success success = (ExtractionResult.Success) extraction;
        ItemStack remainingBooks = book.copy();
        remainingBooks.shrink(1);

        inventory.setStackInSlot(ArcaneExtractorInventory.SOURCE_SLOT, success.source());
        inventory.setStackInSlot(ArcaneExtractorInventory.BOOK_SLOT, remainingBooks);
        inventory.setStackInSlot(ArcaneExtractorInventory.OUTPUT_SLOT, success.enchantedBook());

        return new Success(success);
    }

    public sealed interface Result permits Success, Failure {}

    public record Success(ExtractionResult.Success extraction) implements Result {
        public Success {
            Objects.requireNonNull(extraction, "extraction");
        }
    }

    public record Failure(FailureReason reason, ExtractionFailure extractionFailure) implements Result {
        public Failure {
            Objects.requireNonNull(reason, "reason");
        }
    }

    public enum FailureReason {
        MISSING_BOOK,
        OUTPUT_OCCUPIED,
        EXTRACTION_REJECTED
    }
}
