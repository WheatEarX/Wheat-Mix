package com.wheat_ear.mixin.zombie_breed;

import com.wheat_ear.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChickenEntity.class)
public abstract class ChickenEntityMixin extends AnimalEntity {
    public ChickenEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/passive/ChickenEntity;getVelocity()Lnet/minecraft/util/math/Vec3d;", shift = At.Shift.AFTER))
    public void tickMovement(CallbackInfo ci) {
        if (((ChickenEntity) (AnimalEntity) this).hasJockey() && --((ChickenEntity) (AnimalEntity) this).eggLayTime <= 3000) {
            playSound(SoundEvents.ENTITY_CHICKEN_EGG, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            dropItem(ModItems.ZOMBIE_EGG);
            emitGameEvent(GameEvent.ENTITY_PLACE);
            ((ChickenEntity) (AnimalEntity) this).eggLayTime = this.random.nextInt(6000) + 6000;
        }
    }
}
