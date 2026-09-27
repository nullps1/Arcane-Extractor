package dev.nullps1.arcaneextraction.registry;

import dev.nullps1.arcaneextraction.ArcaneExtraction;
import dev.nullps1.arcaneextraction.block.ArcaneExtractorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(ArcaneExtraction.MOD_ID);

    public static final DeferredBlock<ArcaneExtractorBlock> ARCANE_EXTRACTOR =
            BLOCKS.registerBlock(
                    "arcane_extractor",
                    ArcaneExtractorBlock::new,
                    BlockBehaviour.Properties.of()
                            .mapColor(MapColor.COLOR_PURPLE)
                            .strength(3.5F)
                            .requiresCorrectToolForDrops());

    private ModBlocks() {}

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }
}
