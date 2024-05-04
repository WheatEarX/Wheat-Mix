package com.wheat_ear.item;

import com.wheat_ear.enchantment.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.GameMode;
import net.minecraft.world.World;

import java.util.Timer;
import java.util.TimerTask;

public class CreativeTrailCardItem extends Item {
    public final byte level;
    public int time;
    private int lastTime;
    public boolean onUse = false;
    public boolean firstUse = false;
    private final Timer timer = new Timer();

    public CreativeTrailCardItem(byte level, int time, Settings settings) {
        super(settings);
        this.level = level;
        this.time = time;
        this.lastTime = time;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);
        int cheaterLevel = EnchantmentHelper.getLevel(ModEnchantments.CHEATER, stack);

        if (cheaterLevel >= 10) {
            lastTime = 114514;
        }
        else if (cheaterLevel > 0) {
            lastTime = time + cheaterLevel * 5;
        }

        if (user instanceof ServerPlayerEntity serverPlayer && !serverPlayer.isCreative()) {
            user.getStackInHand(hand).decrement(1);
            onUse = true;
            firstUse = true;
            timer.schedule(new TimeControlTask(serverPlayer), 0, 1000);
            serverPlayer.changeGameMode(GameMode.CREATIVE);

            return TypedActionResult.success(stack);
        }
        return TypedActionResult.pass(stack);
    }

    class TimeControlTask extends TimerTask {

        private final ServerPlayerEntity serverPlayer;

        public TimeControlTask(ServerPlayerEntity serverPlayer) {
            this.serverPlayer = serverPlayer;
        }

        @Override
        public void run() {
            serverPlayer.networkHandler.sendPacket(new OverlayMessageS2CPacket(Text.literal("Left: " + lastTime)));
            --lastTime;

            if (lastTime <= 0) {
                serverPlayer.changeGameMode(GameMode.SURVIVAL);
                onUse = false;
                firstUse = false;
                lastTime = time;
                this.cancel();
            }
            serverPlayer.networkHandler.sendPacket(new OverlayMessageS2CPacket(Text.literal("Left: " + lastTime)));
        }
    }
}
