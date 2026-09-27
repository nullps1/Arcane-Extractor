package dev.nullps1.arcaneextraction.blockentity;

import dev.nullps1.arcaneextraction.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

public final class ArcaneExtractorBlockEntity extends BlockEntity {
    private static final String INVENTORY_TAG = "inventory";

    private final ArcaneExtractorInventory inventory =
            new ArcaneExtractorInventory(this::setChanged);
    private final ArcaneExtractorAutomationHandler automationInventory =
            new ArcaneExtractorAutomationHandler(inventory);

    public ArcaneExtractorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ARCANE_EXTRACTOR.get(), pos, state);
    }

    public IItemHandler inventory() {
        return inventory;
    }

    public IItemHandler automationInventory() {
        return automationInventory;
    }

    public ArcaneExtractorInventory machineInventory() {
        return inventory;
    }

    public ArcaneExtractorTransaction.Result extract(Holder<Enchantment> selected) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return new ArcaneExtractorTransaction.Failure(
                    ArcaneExtractorTransaction.FailureReason.EXTRACTION_REJECTED,
                    null);
        }

        ArcaneExtractorTransaction.Result result =
                ArcaneExtractorTransaction.extract(inventory, selected);

        if (result instanceof ArcaneExtractorTransaction.Success) {
            serverLevel.playSound(
                    null,
                    worldPosition,
                    SoundEvents.ENCHANTMENT_TABLE_USE,
                    SoundSource.BLOCKS,
                    0.8F,
                    1.15F);

            serverLevel.sendParticles(
                    ParticleTypes.ENCHANT,
                    worldPosition.getX() + 0.5D,
                    worldPosition.getY() + 1.0D,
                    worldPosition.getZ() + 0.5D,
                    18,
                    0.35D,
                    0.25D,
                    0.35D,
                    0.05D);
        }

        return result;
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
