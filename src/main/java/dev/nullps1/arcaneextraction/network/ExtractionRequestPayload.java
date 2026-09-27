package dev.nullps1.arcaneextraction.network;

import dev.nullps1.arcaneextraction.ArcaneExtraction;
import dev.nullps1.arcaneextraction.menu.ArcaneExtractorMenu;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ExtractionRequestPayload(
        int containerId,
        String enchantmentId,
        boolean extractAll) implements CustomPacketPayload {

    public static final Type<ExtractionRequestPayload> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(
                    ArcaneExtraction.MOD_ID,
                    "extraction_request"));

    public static final StreamCodec<ByteBuf, ExtractionRequestPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    ExtractionRequestPayload::containerId,
                    ByteBufCodecs.STRING_UTF8,
                    ExtractionRequestPayload::enchantmentId,
                    ByteBufCodecs.BOOL,
                    ExtractionRequestPayload::extractAll,
                    ExtractionRequestPayload::new);

    public static ExtractionRequestPayload single(int containerId, String enchantmentId) {
        return new ExtractionRequestPayload(containerId, enchantmentId, false);
    }

    public static ExtractionRequestPayload all(int containerId) {
        return new ExtractionRequestPayload(containerId, "", true);
    }

    public static void handle(ExtractionRequestPayload payload, IPayloadContext context) {
        Player player = context.player();
        if (!(player.containerMenu instanceof ArcaneExtractorMenu menu)
                || menu.containerId != payload.containerId()
                || !menu.stillValid(player)) {
            return;
        }

        if (payload.extractAll()) {
            menu.extractAll(player);
        } else {
            menu.extractByRegistryId(player, payload.enchantmentId());
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
