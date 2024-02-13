package com.wheat_ear.effect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class AirBreathingEffect extends StatusEffect {
    protected AirBreathingEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (entity instanceof LivingEntity) {
            if (!entity.isInFluid()) {
                entity.setAir(300);
            }
        }
    }
}
