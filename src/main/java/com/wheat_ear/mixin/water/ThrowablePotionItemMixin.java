package com.wheat_ear.mixin.water;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ThrowablePotionItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ThrowablePotionItem.class)
public abstract class ThrowablePotionItemMixin extends Item {
    public ThrowablePotionItemMixin(Settings settings) {
        super(settings);
    }

    @ModifyArg(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"), index = 0)
    public int decrement(int amount) {
        return 0;
    }

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;decrement(I)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir, ItemStack itemStack) {
        if (itemStack != null) {
            if (EnchantmentHelper.getLevel(Enchantments.INFINITY, itemStack) <= 0) {
                itemStack.decrement(1);
            }
        }
    }
}
