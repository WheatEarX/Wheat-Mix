package com.wheat_ear.others;

import com.wheat_ear.effect.ModEffects;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class ModFoodComponents {
    public static final FoodComponent REDDEN_APPLE = new FoodComponent.Builder()
            .hunger(6)
            .statusEffect(new StatusEffectInstance(StatusEffects.UNLUCK, 6000, 4), 1.0F)
            .statusEffect(new StatusEffectInstance(ModEffects.RESISTANCE_REDUCTION, 6000, 2), 1.0F)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 200, 0), 1.0F)
            .alwaysEdible().build();
    public static final FoodComponent VILLAGER_MEAT = new FoodComponent.Builder()
            .hunger(4).saturationModifier(0.4F).meat().build();
    public static final FoodComponent COOKED_VILLAGER_MEAT = new FoodComponent.Builder()
            .hunger(9).saturationModifier(0.9F).meat().build();
}
