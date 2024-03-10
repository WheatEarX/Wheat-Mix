package com.wheat_ear.enchantment;

import com.wheat_ear.item.CreativeTrailCardItem;

public class ModEnchantmentTargets {
    public static final ModEnchantmentTarget ANY_ITEM = item -> true;
    public static final ModEnchantmentTarget CREATIVE_TRAIL_CARD = item -> item instanceof CreativeTrailCardItem;
}
