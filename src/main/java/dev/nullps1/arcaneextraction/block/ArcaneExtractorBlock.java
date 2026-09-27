package dev.nullps1.arcaneextraction.block;

import com.mojang.serialization.MapCodec;
import dev.nullps1.arcaneextraction.blockentity.ArcaneExtractorBlockEntity;
import dev.nullps1.arcaneextraction.menu.ArcaneExtractorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public final class ArcaneExtractorBlock extends BaseEntityBlock {
    public static final MapCodec<ArcaneExtractorBlock> CODEC = simpleCodec(ArcaneExtractorBlock::new);
    private static final Component TITLE =
            Component.translatable("container.arcane_extraction.arcane_extractor");

    public ArcaneExtractorBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<ArcaneExtractorBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ArcaneExtractorBlockEntity(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof ArcaneExtractorBlockEntity extractor)) {
            return null;
        }

        return new SimpleMenuProvider(
                (containerId, playerInventory, player) ->
                        new ArcaneExtractorMenu(containerId, playerInventory, extractor),
                TITLE);
    }

    @Override
    protected InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hitResult) {
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            MenuProvider provider = getMenuProvider(state, level, pos);
            if (provider != null) {
                serverPlayer.openMenu(provider, buffer -> buffer.writeBlockPos(pos));
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }
}
