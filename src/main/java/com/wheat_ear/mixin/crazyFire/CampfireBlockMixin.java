package com.wheat_ear.mixin.crazyFire;

import net.minecraft.block.CampfireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CampfireBlock.class)
public class CampfireBlockMixin {
    @Unique
    public final boolean crazy = false;
}
