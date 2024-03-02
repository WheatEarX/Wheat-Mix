package com.wheat_ear.block;

import com.wheat_ear.WheatMix;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block RANDOM_BLOCK = Registry.register(Registries.BLOCK,
            new Identifier(WheatMix.MOD_ID, "random_block"),
            new RandomBlock(AbstractBlock.Settings.create().ticksRandomly()));
}
