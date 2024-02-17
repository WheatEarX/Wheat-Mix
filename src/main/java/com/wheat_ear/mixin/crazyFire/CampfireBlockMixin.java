package com.wheat_ear.mixin.crazyFire;

import net.minecraft.block.Block;
import net.minecraft.block.CampfireBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin extends Block {
    public CampfireBlockMixin(Settings settings) {
        super(settings);
    }

    @Unique
    public final boolean crazy = false;
}
