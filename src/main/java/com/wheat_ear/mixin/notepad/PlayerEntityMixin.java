package com.wheat_ear.mixin.notepad;

import com.wheat_ear.item.ModItems;
import com.wheat_ear.item.NotepadItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity {
    @Unique
    private static final String MODE = "text.wheat-mix.notepad.mode";

    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "jump", at = @At("TAIL"))
    public void jump(CallbackInfo ci) {
        if (((PlayerEntity) (Entity) this).getMainHandStack().isOf(ModItems.NOTEPAD)) {
            if (!getWorld().isClient) {
                NotepadItem.changeMode(((PlayerEntity) (Entity) this).getMainHandStack());
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        if (((PlayerEntity) (Entity) this).getMainHandStack().isOf(ModItems.NOTEPAD)) {
            if ((Entity) this instanceof ServerPlayerEntity serverPlayer) {
                NotepadItem item = (NotepadItem) ((PlayerEntity) (Entity) this).getMainHandStack().getItem();
                MutableText text = Text.translatable(MODE, item.state.getText());

                serverPlayer.networkHandler.sendPacket(new OverlayMessageS2CPacket(text));
            }

        }
    }
}
