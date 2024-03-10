package com.wheat_ear.enchantment;

import net.minecraft.entity.EquipmentSlot;

public class CheaterEnchantment extends ModEnchantment {
    public CheaterEnchantment() {
        super(Rarity.VERY_RARE, ModEnchantmentTargets.CREATIVE_TRAIL_CARD, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMinLevel() {
        return 1;
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }
}
