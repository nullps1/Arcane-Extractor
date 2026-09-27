package dev.nullps1.arcaneextraction.blockentity;

import dev.nullps1.arcaneextraction.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

public final class ArcaneExtractorBlockEntity extends BlockEntity {
    private static final String INVENTORY_TAG = "inventory";

    private final ArcaneExtractorInventory inventory =
            new ArcaneExtractorInventory(this::setChanged);

    public ArcaneExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ARCANE_EXTRACTOR.get(), pos, state);
    }

    public IItemHandler inventory() {
        return inventory;
    }

    public ArcaneExtractorTransaction.Result extract(Holder<Enchantment> selected) {
        if (level == null || level.isClientSide()) {
            return new ArcaneExtractorTransaction.Failure(
                    ArcaneExtractorTransaction.FailureReason.EXTRACTION_REJECTED,
                    null);
        }
        return ArcaneExtractorTransaction.extract(inventory, selected);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put(INVENTORY_TAG, inventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains(INVENTORY_TAG)) {
            inventory.deserializeNBT(registries, tag.getCompound(INVENTORY_TAG));
        }
    }
}
