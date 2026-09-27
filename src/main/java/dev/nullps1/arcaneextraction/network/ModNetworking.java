package dev.nullps1.arcaneextraction.network;

import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public final class ModNetworking {
    private static final String PROTOCOL_VERSION = "1";

    private ModNetworking() {}

    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PROTOCOL_VERSION);
        registrar.playToServer(
                ExtractionRequestPayload.TYPE,
                ExtractionRequestPayload.STREAM_CODEC,
                ExtractionRequestPayload::handle);
    }
}
