package dev.nullps1.arcaneextraction.client;

import dev.nullps1.arcaneextraction.client.screen.ArcaneExtractorScreen;
import dev.nullps1.arcaneextraction.registry.ModMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

@EventBusSubscriber(value = Dist.CLIENT)
public final class ArcaneExtractionClient {
    private ArcaneExtractionClient() {}

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.ARCANE_EXTRACTOR.get(), ArcaneExtractorScreen::new);
    }
}
