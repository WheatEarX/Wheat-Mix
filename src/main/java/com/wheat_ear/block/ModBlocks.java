package com.wheat_ear.block;

import com.wheat_ear.WheatMix;
import net.minecraft.block.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block RANDOM_BLOCK = new Block(AbstractBlock.Settings.create());
    public static final Block FLAT_BLOCK = new FlatBlock(AbstractBlock.Settings.create().hardness(0.5F).sounds(BlockSoundGroup.STONE));

    public static void registerModBlocks() {
        registerModBlock("random_block", RANDOM_BLOCK);
        registerModBlock("flat_block", FLAT_BLOCK);
    }

    private static void registerModBlock(String id, Block block) {
        Registry.register(Registries.BLOCK, new Identifier(WheatMix.MOD_ID, id), block);
    }
}
