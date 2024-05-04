package com.wheat_ear.item;

import com.wheat_ear.others.ModUtil;
import com.wheat_ear.gui.screen.BrowserScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

import java.util.ArrayList;

public class BrowserItem extends Item {
    private final ArrayList<String> histories = new ArrayList<>();
    private final ArrayList<String> favorites = new ArrayList<>();

    public BrowserItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (user instanceof ClientPlayerEntity clientPlayer) {
            MinecraftClient client = ModUtil.getValue(ClientPlayerEntity.class, MinecraftClient.class, clientPlayer, "client");
            client.setScreen(new BrowserScreen(client, this));
            return TypedActionResult.success(stack);
        }
        return TypedActionResult.pass(stack);
    }

    public ArrayList<String> getHistories() {
        return histories;
    }

    public ArrayList<String> getFavorites() {
        return favorites;
    }
}
