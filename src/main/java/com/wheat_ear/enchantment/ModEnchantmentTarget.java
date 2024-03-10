package com.wheat_ear.enchantment;

import net.minecraft.item.Item;

public interface ModEnchantmentTarget {
    boolean isAcceptable(Item item);
}
