package com.wheat_ear.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class RemoteConnectorItem extends Item {

    private PlayerEntity object;
    private PlayerInventory inventory;

    public RemoteConnectorItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        if (this.object != null) {
            NamedScreenHandlerFactory factory = createFactory(user, this.object);
            if (!user.getWorld().isClient) {
                user.openHandledScreen(factory);
                return TypedActionResult.success(stack);
            }
        }
        return TypedActionResult.pass(stack);
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand) {
        if (entity instanceof PlayerEntity player) {
            NamedScreenHandlerFactory factory = createFactory(user, player);
            if (!user.getWorld().isClient) {
                this.object = player;
                user.openHandledScreen(factory);
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.FAIL;
    }

    private NamedScreenHandlerFactory createFactory(PlayerEntity user, PlayerEntity entity) {
        changeInventory(user, entity);
        return new NamedScreenHandlerFactory() {
            @Override
            public Text getDisplayName() {
                return Text.translatable("haha.remote_connector_screen");
            }

            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory playerInventory, PlayerEntity player) {
                return new GenericContainerScreenHandler(ScreenHandlerType.GENERIC_9X4, syncId, playerInventory, inventory, 4);
            }
        };
    }

    private void changeInventory(PlayerEntity user, PlayerEntity entity) {
        if (user != null && entity != null) {
            this.inventory = new PlayerInventory(user);
            PlayerInventory playerInventory = entity.getInventory();
            if (!this.inventory.isEmpty()) {
                for (int i = 0; i < playerInventory.main.size(); ++i) {
                    entity.getInventory().setStack(i, this.inventory.getStack(i));
                }
            }
            for (int i = 0; i < playerInventory.main.size(); ++i) {
                this.inventory.setStack(i, playerInventory.getStack(i));
            }
            for (int i = 0; i < playerInventory.main.size(); ++i) {
                entity.getInventory().setStack(i, this.inventory.getStack(i));
            }
        }
    }
}
