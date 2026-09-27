package dev.nullps1.arcaneextraction.blockentity;

import dev.nullps1.arcaneextraction.extraction.EnchantmentExtractor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ArcaneExtractorInventory extends ItemStackHandler {
    public static final int SOURCE_SLOT = 0;
    public static final int BOOK_SLOT = 1;
    public static final int OUTPUT_START = 2;
    public static final int OUTPUT_COUNT = 6;
    public static final int OUTPUT_END = OUTPUT_START + OUTPUT_COUNT;
    public static final int SLOT_COUNT = OUTPUT_END;

    private final Runnable changedCallback;

    public ArcaneExtractorInventory(Runnable changedCallback) {
        super(SLOT_COUNT);
        this.changedCallback = changedCallback;
    }

    public static boolean isOutputSlot(int slot) {
        return slot >= OUTPUT_START && slot < OUTPUT_END;
    }

    public int firstEmptyOutputSlot() {
        for (int slot = OUTPUT_START; slot < OUTPUT_END; slot++) {
            if (getStackInSlot(slot).isEmpty()) {
                return slot;
            }
        }
        return -1;
    }

    public int emptyOutputSlots() {
        int count = 0;
        for (int slot = OUTPUT_START; slot < OUTPUT_END; slot++) {
            if (getStackInSlot(slot).isEmpty()) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        if (slot == SOURCE_SLOT) {
            return !EnchantmentExtractor.candidates(stack).isEmpty();
        }
        if (slot == BOOK_SLOT) {
            return stack.is(Items.BOOK);
        }
        return false;
    }

    @Override
    protected void onContentsChanged(int slot) {
        changedCallback.run();
    }
}
