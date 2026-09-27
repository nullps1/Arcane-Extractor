package dev.nullps1.arcaneextraction.registry;

import dev.nullps1.arcaneextraction.ArcaneExtraction;
import dev.nullps1.arcaneextraction.blockentity.ArcaneExtractorBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, ArcaneExtraction.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ArcaneExtractorBlockEntity>>
            ARCANE_EXTRACTOR = BLOCK_ENTITY_TYPES.register(
                    "arcane_extractor",
                    () -> BlockEntityType.Builder.of(
                            ArcaneExtractorBlockEntity::new,
                            ModBlocks.ARCANE_EXTRACTOR.get())
                            .build(null));

    private ModBlockEntities() {}

    public static void register(IEventBus bus) {
        BLOCK_ENTITY_TYPES.register(bus);
    }
}
