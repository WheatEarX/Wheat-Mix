package com.wheat_ear.mixin.enchantment_effect;

import com.wheat_ear.effect.ModEffects;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("DataFlowIssue")
@Mixin(LivingEntity.class)
public abstract class PlayerEntityMixin extends Entity {
    @Shadow public abstract boolean hasStatusEffect(StatusEffect effect);

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

    @Shadow protected abstract void setSyncedArmorStack(EquipmentSlot slot, ItemStack armor);

    @Shadow @Nullable public abstract StatusEffectInstance getStatusEffect(StatusEffect effect);

    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tickMovement", at = @At("TAIL"))
    public void tickMovement(CallbackInfo ci) {
        if (hasStatusEffect(ModEffects.SHARPNESS)) {
            if (getEquippedStack(EquipmentSlot.HEAD).isEmpty()) {
                ItemStack stack = Items.END_ROD.getDefaultStack();
                int amplifier = getStatusEffect(ModEffects.SHARPNESS).getAmplifier();

                stack.addEnchantment(Enchantments.SHARPNESS, amplifier + 1);

                setSyncedArmorStack(EquipmentSlot.HEAD, stack);
            }
        }
    }

    @Inject(method = "jump", at = @At("TAIL"))
    public void jump(CallbackInfo ci) {
        if (!getWorld().isClient) {
            ItemStack headStack = getEquippedStack(EquipmentSlot.HEAD);
            if (headStack.isOf(Items.END_ROD) && EnchantmentHelper.getLevel(Enchantments.SHARPNESS, headStack) > 1) {
                World world = getWorld();
                BlockPos blockPos = getBlockPos().up(2);
                world.setBlockState(blockPos, Blocks.AIR.getDefaultState());
            }
        }
    }
}
