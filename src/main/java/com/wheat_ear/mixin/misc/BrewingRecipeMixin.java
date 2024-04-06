package com.wheat_ear.mixin.misc;

import com.wheat_ear.item.ModItems;
import com.wheat_ear.others.ModPotions;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingRecipeRegistry.class)
public abstract class BrewingRecipeMixin {
    @Shadow
    public static void registerPotionRecipe(Potion input, Item item, Potion output) {
    }

    @Inject(method = "registerDefaults", at = @At("RETURN"))
    private static void registerDefaults(CallbackInfo ci) {
        registerPotionRecipe(Potions.AWKWARD, ModItems.FISH_PUFFER, ModPotions.AIR_BREATHING);
        registerPotionRecipe(ModPotions.AIR_BREATHING, Items.REDSTONE, ModPotions.LONG_AIR_BREATHING);

        registerPotionRecipe(Potions.SLOWNESS, Items.FERMENTED_SPIDER_EYE, ModPotions.JUMP_REDUCTION);
        registerPotionRecipe(ModPotions.JUMP_REDUCTION, Items.REDSTONE, ModPotions.LONG_JUMP_REDUCTION);
        registerPotionRecipe(ModPotions.JUMP_REDUCTION, Items.GLOWSTONE_DUST, ModPotions.STRONG_JUMP_REDUCTION);

        registerPotionRecipe(Potions.SLOW_FALLING, Items.FERMENTED_SPIDER_EYE, ModPotions.QUICK_FALLING);
        registerPotionRecipe(ModPotions.QUICK_FALLING, Items.REDSTONE, ModPotions.LONG_QUICK_FALLING);
        registerPotionRecipe(ModPotions.QUICK_FALLING, Items.GLOWSTONE_DUST, ModPotions.STRONG_QUICK_FALLING);

        registerPotionRecipe(Potions.AWKWARD, Items.NETHERITE_SCRAP, ModPotions.HEALTH_REDUCTION);
        registerPotionRecipe(ModPotions.HEALTH_REDUCTION, Items.REDSTONE, ModPotions.LONG_HEALTH_REDUCTION);
        registerPotionRecipe(ModPotions.HEALTH_REDUCTION, Items.GLOWSTONE_DUST, ModPotions.STRONG_HEALTH_REDUCTION);
    }
}
