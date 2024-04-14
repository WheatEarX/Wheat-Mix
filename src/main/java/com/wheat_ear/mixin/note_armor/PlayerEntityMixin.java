package com.wheat_ear.mixin.note_armor;

import com.wheat_ear.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttributeValue(Lnet/minecraft/entity/attribute/EntityAttribute;)D"))
    public void attack(Entity target, CallbackInfo ci) {
        ItemStack stack = getEquippedStack(EquipmentSlot.HEAD);
        if (stack.isOf(ModItems.NOTE_HELMET)) {
            World world = getWorld();
            if (world instanceof ServerWorld serverWorld) {
                Random random = new Random();

                serverWorld.spawnParticles(ParticleTypes.SONIC_BOOM, getX(), getY() + 0.5, getZ(), 10, 0.2, 0.0, 0.0, 2.0);
                serverWorld.spawnParticles(ParticleTypes.NOTE, getX(), getY() + 2, getZ(), 8, random.nextInt(4) / 24.0F, 0.0, 0.0, 1.0);
                target.damage(serverWorld.getDamageSources().magic(), random.nextInt(5, 15));
                serverWorld.createExplosion(this, target.getX(), target.getY(), target.getZ(), 1.5F, World.ExplosionSourceType.MOB);
            }
        }
    }
}
