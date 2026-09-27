package dev.nullps1.arcaneextraction.blockentity;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public final class ArcaneExtractorAutomationHandler implements IItemHandler {
    private final ArcaneExtractorInventory inventory;

    public ArcaneExtractorAutomationHandler(ArcaneExtractorInventory inventory) {
        this.inventory = inventory;
    }

    @Override
    public int getSlots() {
        return inventory.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        if (ArcaneExtractorInventory.isOutputSlot(slot)) {
            return stack;
        }
        return inventory.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (!ArcaneExtractorInventory.isOutputSlot(slot)) {
            return ItemStack.EMPTY;
        }
        return inventory.extractItem(slot, amount, simulate);
    }

    @Override
    public int getSlotLimit(int slot) {
        return inventory.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return !ArcaneExtractorInventory.isOutputSlot(slot)
                && inventory.isItemValid(slot, stack);
    }
}
