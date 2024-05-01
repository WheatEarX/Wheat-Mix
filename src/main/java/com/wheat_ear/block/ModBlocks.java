package com.wheat_ear.block;

import com.wheat_ear.WheatMix;
import net.minecraft.block.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block RANDOM_BLOCK = Registry.register(Registries.BLOCK,
            new Identifier(WheatMix.MOD_ID, "random_block"),
            new RandomBlock(AbstractBlock.Settings.create().ticksRandomly()));
    public static final Block VILLAGER_HEAD = Registry.register(Registries.BLOCK,
            new Identifier(WheatMix.MOD_ID, "villager_head"),
            new SkullBlock(VillagerHeadSkullType.INSTANCE, AbstractBlock.Settings.copy(Blocks.PLAYER_HEAD)));
    public static final Block VILLAGER_WALL_HEAD = Registry.register(Registries.BLOCK,
            new Identifier(WheatMix.MOD_ID, "villager_wall_head"),
            new WallSkullBlock(VillagerHeadSkullType.INSTANCE, AbstractBlock.Settings.copy(Blocks.PLAYER_WALL_HEAD)));
}
