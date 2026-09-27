package dev.nullps1.arcaneextraction.menu;

import dev.nullps1.arcaneextraction.blockentity.ArcaneExtractorBlockEntity;
import dev.nullps1.arcaneextraction.blockentity.ArcaneExtractorInventory;
import dev.nullps1.arcaneextraction.blockentity.ArcaneExtractorTransaction;
import dev.nullps1.arcaneextraction.extraction.EnchantmentExtractor;
import dev.nullps1.arcaneextraction.extraction.ExtractionCandidate;
import dev.nullps1.arcaneextraction.registry.ModBlocks;
import dev.nullps1.arcaneextraction.registry.ModMenus;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public final class ArcaneExtractorMenu extends AbstractContainerMenu {
    private static final int MACHINE_SLOTS = 3;
    private static final int PLAYER_INVENTORY_START = MACHINE_SLOTS;
    private static final int PLAYER_INVENTORY_END = PLAYER_INVENTORY_START + 27;
    private static final int HOTBAR_START = PLAYER_INVENTORY_END;
    private static final int HOTBAR_END = HOTBAR_START + 9;

    private final ArcaneExtractorBlockEntity blockEntity;
    private final ArcaneExtractorInventory machineInventory;
    private final ContainerLevelAccess access;

    public ArcaneExtractorMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf data) {
        this(
                containerId,
                playerInventory,
                null,
                new ArcaneExtractorInventory(() -> {}),
                ContainerLevelAccess.create(playerInventory.player.level(), data.readBlockPos()));
    }

    public ArcaneExtractorMenu(
            int containerId,
            Inventory playerInventory,
            ArcaneExtractorBlockEntity blockEntity) {
        this(
                containerId,
                playerInventory,
                blockEntity,
                blockEntity.machineInventory(),
                ContainerLevelAccess.create(blockEntity.getLevel(), blockEntity.getBlockPos()));
    }

    private ArcaneExtractorMenu(
            int containerId,
            Inventory playerInventory,
            ArcaneExtractorBlockEntity blockEntity,
            ArcaneExtractorInventory machineInventory,
            ContainerLevelAccess access) {
        super(ModMenus.ARCANE_EXTRACTOR.get(), containerId);
        this.blockEntity = blockEntity;
        this.machineInventory = machineInventory;
        this.access = access;

        addSlot(new SlotItemHandler(machineInventory, ArcaneExtractorInventory.SOURCE_SLOT, 26, 35));
        addSlot(new SlotItemHandler(machineInventory, ArcaneExtractorInventory.BOOK_SLOT, 62, 35));
        addSlot(new SlotItemHandler(machineInventory, ArcaneExtractorInventory.OUTPUT_SLOT, 134, 35) {
            @Override
            public boolean mayPlace(ItemStack stack) {
                return false;
            }
        });

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(
                        playerInventory,
                        column + row * 9 + 9,
                        8 + column * 18,
                        104 + row * 18));
            }
        }

        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(playerInventory, column, 8 + column * 18, 162));
        }
    }

    public List<ExtractionCandidate> candidates() {
        return EnchantmentExtractor.candidates(
                machineInventory.getStackInSlot(ArcaneExtractorInventory.SOURCE_SLOT));
    }

    @Override
    public boolean clickMenuButton(Player player, int id) {
        if (blockEntity == null || id < 0) {
            return false;
        }

        List<ExtractionCandidate> candidates = candidates();
        if (id >= candidates.size()) {
            return false;
        }

        ArcaneExtractorTransaction.Result result =
                blockEntity.extract(candidates.get(id).enchantment());
        if (result instanceof ArcaneExtractorTransaction.Success) {
            broadcastChanges();
            return true;
        }

        return false;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = slot.getItem();
        ItemStack original = stack.copy();

        if (index < MACHINE_SLOTS) {
            if (!moveItemStackTo(stack, PLAYER_INVENTORY_START, HOTBAR_END, true)) {
                return ItemStack.EMPTY;
            }
        } else if (machineInventory.isItemValid(ArcaneExtractorInventory.BOOK_SLOT, stack)) {
            if (!moveItemStackTo(stack, ArcaneExtractorInventory.BOOK_SLOT,
                    ArcaneExtractorInventory.BOOK_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else if (machineInventory.isItemValid(ArcaneExtractorInventory.SOURCE_SLOT, stack)) {
            if (!moveItemStackTo(stack, ArcaneExtractorInventory.SOURCE_SLOT,
                    ArcaneExtractorInventory.SOURCE_SLOT + 1, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < HOTBAR_START) {
            if (!moveItemStackTo(stack, HOTBAR_START, HOTBAR_END, false)) {
                return ItemStack.EMPTY;
            }
        } else if (!moveItemStackTo(stack, PLAYER_INVENTORY_START, PLAYER_INVENTORY_END, false)) {
            return ItemStack.EMPTY;
        }

        if (stack.isEmpty()) {
            slot.set(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        if (stack.getCount() == original.getCount()) {
            return ItemStack.EMPTY;
        }

        slot.onTake(player, stack);
        return original;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(access, player, ModBlocks.ARCANE_EXTRACTOR.get());
    }
}
