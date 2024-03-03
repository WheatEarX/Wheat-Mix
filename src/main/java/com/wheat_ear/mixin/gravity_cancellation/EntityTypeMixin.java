package com.wheat_ear.mixin.gravity_cancellation;

import com.wheat_ear.enchantment.ModEnchantments;
import com.wheat_ear.others.ModUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Consumer;

@Mixin(EntityType.class)
public abstract class EntityTypeMixin<T> {
    @Inject(method = "spawn(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/nbt/NbtCompound;Ljava/util/function/Consumer;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/SpawnReason;ZZ)Lnet/minecraft/entity/Entity;",
    at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void spawn(ServerWorld world, NbtCompound itemNbt, Consumer<T> afterConsumer, BlockPos pos, SpawnReason reason, boolean alignPosition, boolean invertY, CallbackInfoReturnable<T> cir, Entity entity) {
        int gravityCancellationLevel = ModUtil.getEnchantmentLevelFromNbt(itemNbt, ModEnchantments.GRAVITY_CANCELLATION);

        if (gravityCancellationLevel > 0) {
            ModUtil.setGravityCancelled(entity);
        }
    }
}
