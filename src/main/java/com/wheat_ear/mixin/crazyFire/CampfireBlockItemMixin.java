package com.wheat_ear.mixin.crazyFire;

import com.wheat_ear.enchantment.ModEnchantments;
import com.wheat_ear.others.ModUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BlockItem.class)
public abstract class CampfireBlockItemMixin {
    @Shadow public abstract Block getBlock();

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;getSoundGroup()Lnet/minecraft/sound/BlockSoundGroup;"), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void place(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir, ItemPlacementContext itemPlacementContext, BlockState blockState, BlockPos blockPos, World world, PlayerEntity playerEntity, ItemStack itemStack, BlockState blockState2) {
        if (this.getBlock() instanceof CampfireBlock campfireBlock) {
            int loyaltyNegativeLevel = EnchantmentHelper.getLevel(ModEnchantments.LOYALTY_NEGATIVE, context.getStack());
            if (loyaltyNegativeLevel > 0) {
                ModUtil.setValue(CampfireBlock.class, campfireBlock, "crazy", true);
                System.out.println(114514);
            }
        }
    }
}
