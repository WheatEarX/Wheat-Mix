package com.wheat_ear.enchantment;

import net.minecraft.entity.EquipmentSlot;

public class RandomEnchantment extends ModEnchantment {
    public RandomEnchantment() {
        super(Rarity.UNCOMMON, ModEnchantmentTargets.ANY_ITEM, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }
}
