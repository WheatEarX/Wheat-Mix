package com.wheat_ear.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class FlatBlock extends Block implements BlockEntityProvider {

    private final VoxelShape FLAT_SHAPE = Block.createCuboidShape(0.0, 15.9, 0.0, 16.0, 16.0, 16.0);

    public FlatBlock(Settings settings) {
        super(settings);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return FLAT_SHAPE;
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockState state1 = ((FlatBlockEntity) world.getBlockEntity(pos)).blockState;

        spawnBreakParticles(world, player, pos, state1);

        world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(player, state));
        return state1;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FlatBlockEntity(pos, state);
    }
}
