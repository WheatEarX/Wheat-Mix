package com.wheat_ear.mixin.misc;

import com.mojang.authlib.GameProfile;
import com.wheat_ear.enchantment.ModEnchantments;
import com.wheat_ear.others.ModUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin extends PlayerEntity {

    public ClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    public void tickMovement(CallbackInfo ci) {
        int sightClearLevel;
        for (ItemStack stack: getArmorItems()) {
            sightClearLevel = EnchantmentHelper.getLevel(ModEnchantments.SIGHT_CLEAR, stack);
            if (sightClearLevel > 0) {
                clearLookAt();
                break;
            }
        }
    }

    @SuppressWarnings("RedundantCast")
    @Unique
    private void clearLookAt() {
        MinecraftClient client = ModUtil.getValue(ClientPlayerEntity.class, MinecraftClient.class, this, "client");
        HitResult hitResult = client.crosshairTarget;
        World world = getWorld();
        if (hitResult != null) {
            switch (hitResult.getType()) {
                case BLOCK -> {
                    BlockPos blockPos = ((BlockHitResult) hitResult).getBlockPos();
                    BlockState blockState;
                    for (BlockPos pos : BlockPos.iterateOutwards(blockPos, 5, 5, 5)) {
                        blockState = world.getBlockState(pos);
                        world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                        blockState.getBlock().onBreak(world, pos, blockState, this);
                    }
                }
                case ENTITY -> {
                    Entity entity = ((EntityHitResult) hitResult).getEntity();
                    Vec3d pos = entity.getPos();
                    world.createExplosion(this, pos.x, pos.y, pos.z, 5, World.ExplosionSourceType.TNT);
                    entity.damage(world.getDamageSources().mobAttack((LivingEntity) (Entity) this), 20000);
                }
            }
        }
    }
}
