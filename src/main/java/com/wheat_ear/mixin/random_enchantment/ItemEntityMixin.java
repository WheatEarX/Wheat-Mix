package com.wheat_ear.mixin.random_enchantment;

import com.wheat_ear.enchantment.ModEnchantments;
import com.wheat_ear.others.ModUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
    @Shadow public abstract ItemStack getStack();

    @Shadow public abstract void setStack(ItemStack stack);

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        ItemStack stack = getStack();
        if (EnchantmentHelper.getLevel(ModEnchantments.RANDOM, stack) > 0) {
            Enchantment enchantment = ModUtil.getRandomFromRegistry(Registries.ENCHANTMENT);
            int level = new Random().nextInt(enchantment.getMinLevel(), enchantment.getMaxLevel());

            stack.addEnchantment(enchantment, level);
            setStack(stack);
        }
    }
}
