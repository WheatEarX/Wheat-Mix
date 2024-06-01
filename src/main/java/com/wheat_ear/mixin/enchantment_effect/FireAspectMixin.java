package com.wheat_ear.mixin.enchantment_effect;

import com.wheat_ear.effect.ModEffects;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public abstract class FireAspectMixin {
    @SuppressWarnings("DataFlowIssue")
    @Inject(method = "getFireAspect", at = @At("HEAD"), cancellable = true)
    private static void getFireAspect(LivingEntity entity, CallbackInfoReturnable<Integer> cir) {
        if (entity.hasStatusEffect(ModEffects.FIRE_ASPECT)) {
            int level = entity.getStatusEffect(ModEffects.FIRE_ASPECT).getAmplifier();
            cir.setReturnValue(level + 1);
        }
    }
}
