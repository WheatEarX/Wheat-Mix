package com.wheat_ear.mixin.note_block_armor;

import com.wheat_ear.item.ModItems;
import com.wheat_ear.others.ModUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.sound.SoundCategory;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        ItemStack stack = getEquippedStack(EquipmentSlot.HEAD);
        if (stack.isOf(ModItems.NOTE_HELMET)) {
            World world = getWorld();
            if (!world.isClient) {
                Random random = new Random();
                world.playSound(null, getBlockPos(), ModUtil.getRandomFromRegistry(Registries.SOUND_EVENT), SoundCategory.PLAYERS,
                        random.nextFloat(0.5F, 10.0F), random.nextFloat(0.5F, 10.0F));
            }
        }
    }
}
