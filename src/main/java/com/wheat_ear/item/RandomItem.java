package com.wheat_ear.item;

import com.wheat_ear.others.ModUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class RandomItem extends Item {

    public RandomItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack1 = user.getStackInHand(hand);
        if (!world.isClient) {
            Item item = ModUtil.getRandomFromRegistry(Registries.ITEM);
            ItemStack stack2 = new ItemStack(item, stack1.getCount());

            user.setStackInHand(hand, stack2);
            return TypedActionResult.success(stack2);
        }
        return TypedActionResult.pass(stack1);
    }
}
