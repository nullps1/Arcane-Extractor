package dev.nullps1.arcaneextraction.blockentity;

import dev.nullps1.arcaneextraction.extraction.EnchantmentExtractor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ArcaneExtractorInventory extends ItemStackHandler {
    public static final int SOURCE_SLOT = 0;
    public static final int BOOK_SLOT = 1;
    public static final int OUTPUT_SLOT = 2;
    public static final int SLOT_COUNT = 3;

    private final Runnable changedCallback;

    public ArcaneExtractorInventory(Runnable changedCallback) {
        super(SLOT_COUNT);
        this.changedCallback = changedCallback;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return switch (slot) {
            case SOURCE_SLOT -> !EnchantmentExtractor.candidates(stack).isEmpty();
            case BOOK_SLOT -> stack.is(Items.BOOK);
            case OUTPUT_SLOT -> false;
            default -> false;
        };
    }

    @Override
    protected void onContentsChanged(int slot) {
        changedCallback.run();
    }
}
