package com.wheat_ear;

import com.wheat_ear.command.NotepadCommand;
import com.wheat_ear.enchantment.ModEnchantments;
import com.wheat_ear.entity.ModEntityType;
import com.wheat_ear.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WheatMix implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("wheat_mix");
    public static final String MOD_ID = "wheat_mix";

    private static void registerModCommands() {
        NotepadCommand.register();
    }

    private static void registerModItemGroups() {
        Registry.register(Registries.ITEM_GROUP,
                new Identifier(MOD_ID, "we_need"),
                ModItems.WE_NEED);
    }

    @Override
    public void onInitialize() {
        ModEntityType.registerModEntityTypes();
        registerModItemGroups();
        registerModCommands();
        ModEnchantments.RegisterModEnchantments();

        LOGGER.info("Mod Wheat-Mix has been initialized");
    }
}