package com.wheat_ear.enchantment;

import com.wheat_ear.Make;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEnchantments {
    public static final Enchantment EXPLOSION = Registry.register(Registries.ENCHANTMENT,
            new Identifier(Make.MOD_ID, "explosion"),
            new ExplosionEnchantment());
    public static final Enchantment PROJECTILE = Registry.register(Registries.ENCHANTMENT,
            new Identifier(Make.MOD_ID, "projectile"),
            new ProjectileEnchantment());
    public static final Enchantment FIRE = Registry.register(Registries.ENCHANTMENT,
            new Identifier(Make.MOD_ID, "fire"),
            new FireEnchantment());
    public static final Enchantment UNDEAD = Registry.register(Registries.ENCHANTMENT,
            new Identifier(Make.MOD_ID, "undead"),
            new UndeadEnchantment());
    public static final Enchantment ARTHROPOD = Registry.register(Registries.ENCHANTMENT,
            new Identifier(Make.MOD_ID, "arthropod"),
            new ArthropodEnchantment());
    public static final Enchantment DISAPPEAR = Registry.register(Registries.ENCHANTMENT,
            new Identifier(Make.MOD_ID, "disappear"),
            new DisappearEnchantment());
    public static final Enchantment GRAVITY_CANCELLATION = Registry.register(Registries.ENCHANTMENT,
            new Identifier(Make.MOD_ID, "gravity_cancellation"),
            new GravityCancellationEnchantment());

    public static void RegisterModEnchantments() {
        Make.LOGGER.debug("Registering mod enchantments for" + Make.MOD_ID);
    }
}
