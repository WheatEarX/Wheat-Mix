package com.wheat_ear.item;

import com.wheat_ear.Make;
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
            new Identifier(Make.MOD_ID, "fish_puffer"),
            new Item(new Item.Settings().food(FoodComponents.PUFFERFISH)));
    public static final Item REDDEN_APPLE = Registry.register(Registries.ITEM,
            new Identifier(Make.MOD_ID, "redden_apple"),
            new Item(new Item.Settings().food(ModFoodComponents.REDDEN_APPLE).rarity(Rarity.UNCOMMON)));
    public static final Item DOLPHIN_LIQUID_BOTTLE = Registry.register(Registries.ITEM,
            new Identifier(Make.MOD_ID, "dolphin_liquid_bottle"),
            new DolphinLiquidItem(new Item.Settings().maxCount(1)));
    public static final Item REMOTE_CONNECTOR = Registry.register(Registries.ITEM,
            new Identifier(Make.MOD_ID, "remote_connector"),
            new RemoteConnectorItem(new Item.Settings().maxCount(1)));
    public static final ItemGroup WE_NEED = FabricItemGroup.builder()
            .icon(() -> new ItemStack(Items.AIR))
            .displayName(Text.translatable("itemGroup.make.weNeed"))
            .entries((context, entries) -> {
                entries.add(FISH_PUFFER);
                entries.add(REDDEN_APPLE);
                entries.add(DOLPHIN_LIQUID_BOTTLE);
            })
            .build();
}
