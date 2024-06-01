package com.wheat_ear.effect;

import com.wheat_ear.WheatMix;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEffects {
    public static final StatusEffect AIR_BREATHING = Registry.register(Registries.STATUS_EFFECT,
            new Identifier(WheatMix.MOD_ID, "air_breathing"),
            new AirBreathingEffect(StatusEffectCategory.BENEFICIAL, 938271));
    public static final StatusEffect JUMP_REDUCTION = Registry.register(Registries.STATUS_EFFECT,
            new Identifier(WheatMix.MOD_ID, "jump_reduction"),
            new ModEffect(StatusEffectCategory.BENEFICIAL, 114514));
    public static final StatusEffect RESISTANCE_REDUCTION = Registry.register(Registries.STATUS_EFFECT,
            new Identifier(WheatMix.MOD_ID, "resistance_reduction"),
            new ModEffect(StatusEffectCategory.BENEFICIAL, 13992091));
    public static final StatusEffect QUICK_FALLING = Registry.register(Registries.STATUS_EFFECT,
            new Identifier(WheatMix.MOD_ID, "quick_falling"),
            new ModEffect(StatusEffectCategory.BENEFICIAL, 69));
    public static final StatusEffect SLOW_SWIMMING = Registry.register(Registries.STATUS_EFFECT,
            new Identifier(WheatMix.MOD_ID, "slow_swimming"),
            new ModEffect(StatusEffectCategory.BENEFICIAL, 6748392));
    public static final StatusEffect HEALTH_REDUCTION = Registry.register(Registries.STATUS_EFFECT,
            new Identifier(WheatMix.MOD_ID, "health_reduction"),
            new ModEffect(StatusEffectCategory.BENEFICIAL, 16271230)
                    .addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH,
                            "81DCE4D8-70BB-81A1-30BD-5380DE36A94C", -4.0, EntityAttributeModifier.Operation.ADDITION));
    // Newer
    public static final StatusEffect FIRE_ASPECT = Registry.register(Registries.STATUS_EFFECT,
            new Identifier(WheatMix.MOD_ID, "fire_aspect"),
            new ModEffect(StatusEffectCategory.BENEFICIAL, 0xEF6723));
    public static final StatusEffect SHARPNESS = Registry.register(Registries.STATUS_EFFECT,
            new Identifier(WheatMix.MOD_ID, "sharpness"),
            new ModEffect(StatusEffectCategory.BENEFICIAL, 0x3D79B4));
}
