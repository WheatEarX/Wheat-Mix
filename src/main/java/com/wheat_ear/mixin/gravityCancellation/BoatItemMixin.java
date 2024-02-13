package com.wheat_ear.mixin.gravityCancellation;

import com.wheat_ear.others.ModUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.BoatItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BoatItem.class)
public abstract class BoatItemMixin extends Item {
    public BoatItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "createEntity", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void createEntity(World world, HitResult hitResult, ItemStack stack, PlayerEntity player, CallbackInfoReturnable<BoatEntity> cir, Vec3d vec3d, BoatEntity boatEntity) {
        if (ModUtil.canSetGravityCancelled(stack)) {
            ModUtil.setGravityCancelled(boatEntity);
        }
    }
}
