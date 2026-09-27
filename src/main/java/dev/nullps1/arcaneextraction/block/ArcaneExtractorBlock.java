package dev.nullps1.arcaneextraction.block;

import com.mojang.serialization.MapCodec;
import dev.nullps1.arcaneextraction.blockentity.ArcaneExtractorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public final class ArcaneExtractorBlock extends BaseEntityBlock {
    public static final MapCodec<ArcaneExtractorBlock> CODEC = simpleCodec(ArcaneExtractorBlock::new);

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
}
