package com.wheat_ear.mixin;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin {
    /**
     * @author Wheat_ear
     * @reason For easy
     */
    @Overwrite
    public boolean isAcceptableItem(ItemStack stack) {
        return true;
    }
}
