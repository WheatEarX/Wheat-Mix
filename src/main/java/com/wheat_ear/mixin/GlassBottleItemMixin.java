package com.wheat_ear.mixin;

import com.wheat_ear.item.ModItems;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.GlassBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(GlassBottleItem.class)
abstract class GlassBottleItemMixin extends Item {
    public GlassBottleItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    @Unique
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity instanceof DolphinEntity dolphinEntity) {
            if (dolphinEntity.isAlive()) {
                dolphinEntity.getWorld().playSoundFromEntity(user, dolphinEntity, SoundEvents.ITEM_BOTTLE_FILL,
                        SoundCategory.PLAYERS, 1.0F, 1.0F);
                if (!user.getAbilities().creativeMode) {
                    stack.increment(1);
                }
                user.getWorld().spawnEntity(new ItemEntity(user.getWorld(), user.getX(), user.getY(), user.getZ(),
                        new ItemStack(ModItems.DOLPHIN_LIQUID_BOTTLE)));
                dolphinEntity.damage(user.getWorld().getDamageSources().playerAttack(user), 5.0F);
                return ActionResult.success(user.getWorld().isClient);
            }
        }
        return ActionResult.PASS;
    }
}
