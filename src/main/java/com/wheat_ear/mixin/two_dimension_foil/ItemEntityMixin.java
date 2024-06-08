package com.wheat_ear.mixin.two_dimension_foil;

import com.wheat_ear.block.FlatBlockEntity;
import com.wheat_ear.block.ModBlocks;
import com.wheat_ear.item.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Shadow public abstract ItemStack getStack();

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tick(CallbackInfo ci) {
        if (getStack().isOf(ModItems.TWO_DIMENSIONS_FOIL)) {
            if (isOnGround() && !getWorld().isClient) {
                World world = getWorld();
                BlockPos blockPos = getBlockPos().down();
                if (!world.getBlockState(blockPos).isOf(Blocks.AIR)) {
                    BlockState blockState = world.getBlockState(blockPos);
                    world.setBlockState(blockPos, ModBlocks.FLAT_BLOCK.getDefaultState());
                    FlatBlockEntity flatBlockEntity = (FlatBlockEntity) world.getBlockEntity(blockPos);
                    if (flatBlockEntity != null) {
                        flatBlockEntity.blockState = blockState;
                    }
                }
            }
        }
    }
}
