package com.wheat_ear.item;

import com.wheat_ear.WheatMix;
import com.wheat_ear.block.ModBlocks;
import com.wheat_ear.others.ModFoodComponents;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ModItems {
    public static final Item FISH_PUFFER = Registry.register(Registries.ITEM,
            new Identifier(WheatMix.MOD_ID, "fish_puffer"),
            new Item(new Item.Settings().food(FoodComponents.PUFFERFISH)));
    public static final Item REDDEN_APPLE = Registry.register(Registries.ITEM,
            new Identifier(WheatMix.MOD_ID, "redden_apple"),
            new Item(new Item.Settings().food(ModFoodComponents.REDDEN_APPLE).rarity(Rarity.UNCOMMON)));
    public static final Item DOLPHIN_LIQUID_BOTTLE = Registry.register(Registries.ITEM,
            new Identifier(WheatMix.MOD_ID, "dolphin_liquid_bottle"),
            new DolphinLiquidItem(new Item.Settings().maxCount(1)));
    public static final Item CREATIVE_TRAIL_CARD_LEVEL_1 = Registry.register(Registries.ITEM,
            new Identifier(WheatMix.MOD_ID, "creative_trail_card_level_1"),
            new CreativeTrailCardItem((byte) 1, 10, new Item.Settings().rarity(Rarity.UNCOMMON)));
    public static final Item CREATIVE_TRAIL_CARD_LEVEL_2 = Registry.register(Registries.ITEM,
            new Identifier(WheatMix.MOD_ID, "creative_trail_card_level_2"),
            new CreativeTrailCardItem((byte) 2, 20, new Item.Settings().rarity(Rarity.RARE)));
    public static final Item CREATIVE_TRAIL_CARD_LEVEL_3 = Registry.register(Registries.ITEM,
            new Identifier(WheatMix.MOD_ID, "creative_trail_card_level_3"),
            new CreativeTrailCardItem((byte) 3, 25, new Item.Settings().rarity(Rarity.EPIC)));
    public static final Item REMOTE_CONNECTOR = Registry.register(Registries.ITEM,
            new Identifier(WheatMix.MOD_ID, "remote_connector"),
            new RemoteConnectorItem(new Item.Settings().maxCount(1)));
    public static final Item RANDOM_ITEM = Registry.register(Registries.ITEM,
            new Identifier(WheatMix.MOD_ID, "random_item"),
            new RandomItem(new Item.Settings()));
    public static final Item RANDOM_BLOCK = Registry.register(Registries.ITEM,
            new Identifier(WheatMix.MOD_ID, "random_block"),
            new RandomBlockItem(ModBlocks.RANDOM_BLOCK, new Item.Settings()));
    public static final ItemGroup WE_NEED = FabricItemGroup.builder()
            .icon(() -> new ItemStack(Items.AIR))
            .displayName(Text.translatable("itemGroup.wheat-mix.weNeed"))
            .entries((context, entries) -> {
                entries.add(FISH_PUFFER);
                entries.add(REDDEN_APPLE);
                entries.add(DOLPHIN_LIQUID_BOTTLE);
                entries.add(CREATIVE_TRAIL_CARD_LEVEL_1);
                entries.add(CREATIVE_TRAIL_CARD_LEVEL_2);
                entries.add(CREATIVE_TRAIL_CARD_LEVEL_3);
                entries.add(REMOTE_CONNECTOR);
                entries.add(RANDOM_ITEM);
                entries.add(RANDOM_BLOCK);
            })
            .build();
}
