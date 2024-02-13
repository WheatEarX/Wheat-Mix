package com.wheat_ear;

import com.wheat_ear.enchantment.ModEnchantments;
import com.wheat_ear.item.ModItems;
import net.fabricmc.api.ModInitializer;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WheatMix implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("wheat-mix");
    public static final String MOD_ID = "wheat-mix";

    private static void registerCommands() {
    }

    @Override
    public void onInitialize() {
        Registry.register(Registries.ITEM_GROUP,
                new Identifier(MOD_ID, "we_need"),
                ModItems.WE_NEED);
        ModEnchantments.RegisterModEnchantments();
        registerCommands();

        LOGGER.info("Hello Fabric world!");
    }
}