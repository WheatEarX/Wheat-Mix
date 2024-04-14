package com.wheat_ear.mixin.note_armor;

import com.wheat_ear.item.ModItems;
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

    @Inject(method = "attackLivingEntity", at = @At("RETURN"))
    public void attackLivingEntity(LivingEntity target, CallbackInfo ci) {
        ItemStack stack = getEquippedStack(EquipmentSlot.HEAD);
        if (stack.isOf(ModItems.NOTE_HELMET)) {
            World world = getWorld();
            if (world instanceof ServerWorld serverWorld) {
                Random random = new Random();

                serverWorld.spawnParticles(ParticleTypes.SONIC_BOOM, getX(), getY() + 0.5, getZ(), 5, 0.8, 0.2, 0.8, 1.0);
                target.damage(serverWorld.getDamageSources().magic(), random.nextInt(5, 15));
            }
        }
    }
}
