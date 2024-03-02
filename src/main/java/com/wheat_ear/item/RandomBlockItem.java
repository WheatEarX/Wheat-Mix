package com.wheat_ear.item;

import com.wheat_ear.others.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.Registries;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class RandomBlockItem extends BlockItem {
    public RandomBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Override
    protected boolean place(ItemPlacementContext context, BlockState state) {
        World world = context.getWorld();
        if (!world.isClient) {
            BlockPos blockPos = context.getBlockPos();

            Block block = ModUtil.getRandomFromRegistry(Registries.BLOCK);
            BlockState blockState = block.getPlacementState(context);

            if (blockState == null || block.getRequiredFeatures().contains(FeatureFlags.UPDATE_1_21)) {
                return place(context, state);
            }
            return world.setBlockState(blockPos, blockState, 26);
        }
        return true;
    }
}
