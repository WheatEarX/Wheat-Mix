package com.wheat_ear.entity;

import com.wheat_ear.WheatMix;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntityType {
    public static final EntityType<PeaEntity> PEA = Registry.register(Registries.ENTITY_TYPE,
            new Identifier(WheatMix.MOD_ID, "pea"),
            FabricEntityTypeBuilder.create(SpawnGroup.MISC)
                    .dimensions(new EntityDimensions(0.6F, 0.6F, false))
                    .trackedUpdateRate(3)
                    .entityFactory(PeaEntity::create)
                    .build());
}
