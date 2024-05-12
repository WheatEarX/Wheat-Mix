package com.wheat_ear.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class ModEntityType {
    public static final EntityType<ZombieEggEntity> ZOMBIE_EGG = FabricEntityTypeBuilder.create(SpawnGroup.MISC, ZombieEggEntity::create)
            .dimensions(new EntityDimensions(0.25F, 0.25F, false)).trackRangeChunks(4).trackedUpdateRate(8).build();
}
