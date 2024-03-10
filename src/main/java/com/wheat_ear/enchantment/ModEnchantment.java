package com.wheat_ear.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

public class ModEnchantment extends Enchantment {
    public final ModEnchantmentTarget enchantmentTarget;

    public ModEnchantment(Rarity rarity, ModEnchantmentTarget enchantmentTarget, EquipmentSlot[] slotTypes) {
        super(rarity, null, slotTypes);
        this.enchantmentTarget = enchantmentTarget;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return enchantmentTarget.isAcceptable(stack.getItem());
    }
}
