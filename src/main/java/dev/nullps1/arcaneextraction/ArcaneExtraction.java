package dev.nullps1.arcaneextraction;

import com.mojang.logging.LogUtils;
import dev.nullps1.arcaneextraction.registry.ModBlockEntities;
import dev.nullps1.arcaneextraction.registry.ModBlocks;
import dev.nullps1.arcaneextraction.registry.ModCapabilities;
import dev.nullps1.arcaneextraction.registry.ModItems;
import dev.nullps1.arcaneextraction.registry.ModMenus;
import dev.nullps1.arcaneextraction.network.ModNetworking;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.slf4j.Logger;

@Mod(ArcaneExtraction.MOD_ID)
public final class ArcaneExtraction {
    public static final String MOD_ID = "arcane_extraction";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ArcaneExtraction(IEventBus modEventBus) {
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModMenus.register(modEventBus);
        modEventBus.addListener(ModCapabilities::register);
        modEventBus.addListener(ModNetworking::register);
        modEventBus.addListener(this::addCreativeTabContents);

        LOGGER.info("Arcane Extraction initializing");
    }

    private void addCreativeTabContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModItems.ARCANE_EXTRACTOR.get());
        }
    }
}
