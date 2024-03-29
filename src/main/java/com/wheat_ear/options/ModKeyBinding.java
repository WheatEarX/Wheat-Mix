package com.wheat_ear.options;

import com.wheat_ear.item.PeaShooterPreviewItem;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ModKeyBinding {
    public static final KeyBinding SHOOT = KeyBindingHelper.registerKeyBinding(new KeyBinding("key.shoot", 82, KeyBinding.GAMEPLAY_CATEGORY));

    public static void registerEvents() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (SHOOT.isPressed()) {
                if (client.player != null) {
                    ClientPlayerEntity player = client.player;
                    ItemStack stack = player.getEquippedStack(EquipmentSlot.HEAD);
                    World world = player.getWorld();

                    if (stack.getItem() instanceof PeaShooterPreviewItem) {
                    }
                }
            }
        });
    }
}
