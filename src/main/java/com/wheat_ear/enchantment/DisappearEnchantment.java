package com.wheat_ear.enchantment;

import net.minecraft.entity.EquipmentSlot;

public class DisappearEnchantment extends ModEnchantment {
    public DisappearEnchantment() {
        super(Rarity.RARE, ModEnchantmentTargets.ANY_ITEM, EquipmentSlot.values());
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
