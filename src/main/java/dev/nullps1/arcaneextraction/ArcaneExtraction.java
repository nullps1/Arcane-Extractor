package dev.nullps1.arcaneextraction;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(ArcaneExtraction.MOD_ID)
public final class ArcaneExtraction {
    public static final String MOD_ID = "arcane_extraction";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ArcaneExtraction(IEventBus modEventBus) {
        LOGGER.info("Arcane Extraction initializing");
    }
}
