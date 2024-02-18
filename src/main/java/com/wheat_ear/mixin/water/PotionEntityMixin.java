package com.wheat_ear.mixin.water;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(PotionEntity.class)
public abstract class PotionEntityMixin extends Entity {
    public PotionEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "onBlockHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/thrown/PotionEntity;extinguishFire(Lnet/minecraft/util/math/BlockPos;)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void onBlockHit(BlockHitResult blockHitResult, CallbackInfo ci, ItemStack itemStack, Potion potion, List<?> list, boolean bl, Direction direction, BlockPos blockPos, BlockPos blockPos2) {
        getWorld().setBlockState(blockPos.up(), Blocks.WATER.getDefaultState());
    }
}
