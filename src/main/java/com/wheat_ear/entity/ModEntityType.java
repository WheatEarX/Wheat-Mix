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
    public static final EntityType<ZombieEggEntity> ZOMBIE_EGG = FabricEntityTypeBuilder.create(SpawnGroup.MISC, ZombieEggEntity::create)
            .dimensions(new EntityDimensions(0.25F, 0.25F, false)).trackRangeChunks(4).trackedUpdateRate(8).build();

    public static void registerModEntityTypes() {
        Registry.register(Registries.ENTITY_TYPE, new Identifier(WheatMix.MOD_ID, "zombie_egg"), ZOMBIE_EGG);
    }
}
