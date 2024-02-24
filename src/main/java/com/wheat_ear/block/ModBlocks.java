package com.wheat_ear.block;

import com.wheat_ear.WheatMix;
import com.wheat_ear.fluid.ModFluids;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block PAPER_BLOCK = Registry.register(Registries.BLOCK,
            new Identifier(WheatMix.MOD_ID, "paper_block"),
            new Block(AbstractBlock.Settings.create()));
    public static final Block PAPER = Registry.register(Registries.BLOCK,
            new Identifier(WheatMix.MOD_ID, "paper"),
            new FluidBlock(ModFluids.PAPER, AbstractBlock.Settings.copy(Blocks.WATER)));
}
