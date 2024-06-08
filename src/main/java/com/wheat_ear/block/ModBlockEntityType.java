package com.wheat_ear.block;

import com.wheat_ear.WheatMix;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntityType {
    public static final BlockEntityType<FlatBlockEntity> FLAT_BLOCK_ENTITY = Registry.register(Registries.BLOCK_ENTITY_TYPE,
            new Identifier(WheatMix.MOD_ID, "flat_block_entity"),
            FabricBlockEntityTypeBuilder.create(FlatBlockEntity::new, ModBlocks.FLAT_BLOCK).build());
}
