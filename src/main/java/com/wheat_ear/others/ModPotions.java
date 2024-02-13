package com.wheat_ear.others;

import com.wheat_ear.WheatMix;
import com.wheat_ear.effect.ModEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModPotions {
    public static final Potion AIR_BREATHING = Registry.register(Registries.POTION,
            new Identifier(WheatMix.MOD_ID, "air_breathing"),
            new Potion(new StatusEffectInstance(ModEffects.AIR_BREATHING, 3600)));
    public static final Potion LONG_AIR_BREATHING = Registry.register(Registries.POTION,
            new Identifier(WheatMix.MOD_ID, "long_air_breathing"),
            new Potion("air_breathing",
                    new StatusEffectInstance(ModEffects.AIR_BREATHING, 9600)));
    public static final Potion JUMP_REDUCTION = Registry.register(Registries.POTION,
            new Identifier(WheatMix.MOD_ID, "jump_reduction"),
            new Potion(new StatusEffectInstance(ModEffects.JUMP_REDUCTION, 3600)));
    public static final Potion LONG_JUMP_REDUCTION = Registry.register(Registries.POTION,
            new Identifier(WheatMix.MOD_ID, "long_jump_reduction"),
            new Potion("jump_reduction",
                    new StatusEffectInstance(ModEffects.JUMP_REDUCTION, 9600)));
    public static final Potion STRONG_JUMP_REDUCTION = Registry.register(Registries.POTION,
            new Identifier(WheatMix.MOD_ID, "strong_jump_reduction"),
            new Potion("jump_reduction",
                    new StatusEffectInstance(ModEffects.JUMP_REDUCTION, 1800, 1)));
    public static final Potion QUICK_FALLING = Registry.register(Registries.POTION,
            new Identifier(WheatMix.MOD_ID, "quick_falling"),
            new Potion(new StatusEffectInstance(ModEffects.QUICK_FALLING, 1800)));
    public static final Potion LONG_QUICK_FALLING = Registry.register(Registries.POTION,
            new Identifier(WheatMix.MOD_ID, "long_quick_falling"),
            new Potion("quick_falling",
                    new StatusEffectInstance(ModEffects.QUICK_FALLING, 4800)));
    public static final Potion STRONG_QUICK_FALLING = Registry.register(Registries.POTION,
            new Identifier(WheatMix.MOD_ID, "strong_quick_falling"),
            new Potion("quick_falling",
                    new StatusEffectInstance(ModEffects.QUICK_FALLING, 1000, 2)));
    public static final Potion HEALTH_REDUCTION = Registry.register(Registries.POTION,
            new Identifier(WheatMix.MOD_ID, "health_reduction"),
            new Potion(new StatusEffectInstance(ModEffects.HEALTH_REDUCTION, 2400)));
    public static final Potion LONG_HEALTH_REDUCTION = Registry.register(Registries.POTION,
            new Identifier(WheatMix.MOD_ID, "long_health_reduction"),
            new Potion("health_reduction",
                    new StatusEffectInstance(ModEffects.HEALTH_REDUCTION, 6400)));
    public static final Potion STRONG_HEALTH_REDUCTION = Registry.register(Registries.POTION,
            new Identifier(WheatMix.MOD_ID, "strong_health_reduction"),
            new Potion("health_reduction",
                    new StatusEffectInstance(ModEffects.HEALTH_REDUCTION, 1600, 2)));
}
