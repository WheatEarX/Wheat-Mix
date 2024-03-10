package com.wheat_ear.mixin.bounce;

import com.wheat_ear.enchantment.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    @Override
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        if (EnchantmentHelper.getLevel(ModEnchantments.BOUNCE, getEquippedStack(EquipmentSlot.FEET)) > 0) {
            return 0;
        }
        return super.computeFallDamage(fallDistance, damageMultiplier);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        if (EnchantmentHelper.getLevel(ModEnchantments.BOUNCE, getEquippedStack(EquipmentSlot.FEET)) > 0) {
            if (isOnGround()) {
                Random localRandom = new Random();
                int bounceMultiplier = EnchantmentHelper.getLevel(ModEnchantments.BOUNCE, getEquippedStack(EquipmentSlot.FEET)) * 6;

                addVelocity(localRandom.nextFloat() * localRandom.nextInt(-bounceMultiplier, bounceMultiplier), localRandom.nextFloat() * localRandom.nextInt(-bounceMultiplier, bounceMultiplier) * 0.5, localRandom.nextFloat() * localRandom.nextInt(-bounceMultiplier, bounceMultiplier));
            }
        }
    }
}
