package com.wheat_ear.block;

import net.minecraft.block.SkullBlock;

public class VillagerHeadSkullType implements SkullBlock.SkullType {
    public static VillagerHeadSkullType INSTANCE = new VillagerHeadSkullType();
    private VillagerHeadSkullType() {
        SkullBlock.SkullType.TYPES.put(asString(), this);
    }
    @Override
    public String asString() {
        return "villager";
    }
}
