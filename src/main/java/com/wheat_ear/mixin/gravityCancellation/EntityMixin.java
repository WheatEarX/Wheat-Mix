package com.wheat_ear.mixin.gravityCancellation;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EyeOfEnderEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract void setNoGravity(boolean noGravity);

    @Shadow public abstract void setVelocity(Vec3d velocity);

    @Unique
    public boolean gravityCancelled = false;


    @SuppressWarnings("ConstantValue")
    @Inject(method = "baseTick", at = @At("RETURN"))
    public void baseTick(CallbackInfo ci) {
        if (gravityCancelled) {
            this.setNoGravity(true);
            if ((Object) this instanceof ProjectileEntity || (Object) this instanceof EyeOfEnderEntity) {
                this.setVelocity(Vec3d.ZERO);
            }
        }
    }

    @Inject(method = "writeNbt", at = @At("RETURN"))
    public void writeNbt(NbtCompound nbt, CallbackInfoReturnable<NbtCompound> cir) {
        if (this.gravityCancelled) {
            nbt.putBoolean("GravityCancelled", true);
        }
    }

    @Inject(method = "readNbt", at = @At("RETURN"))
    public void readNbt(NbtCompound nbt, CallbackInfo ci) {
        this.gravityCancelled = nbt.getBoolean("GravityCancelled");
    }
}
