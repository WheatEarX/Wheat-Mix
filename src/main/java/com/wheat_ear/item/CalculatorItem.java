package com.wheat_ear.item;

import com.wheat_ear.others.ModUtil;
import com.wheat_ear.screen.CalculatorScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class CalculatorItem extends Item {
    public CalculatorItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (user instanceof ClientPlayerEntity clientPlayer) {
            MinecraftClient client = ModUtil.getValue(ClientPlayerEntity.class, MinecraftClient.class, clientPlayer, "client");
            client.setScreen(new CalculatorScreen(client));
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.pass(stack);
    }
}
