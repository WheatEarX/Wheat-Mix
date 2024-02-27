package com.wheat_ear.mixin.creative_trail_card;

import com.wheat_ear.item.CreativeTrailCardItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow @Nullable public Screen currentScreen;

    @Shadow @Nullable public ClientPlayerEntity player;

    @Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V", ordinal = 1, shift = At.Shift.AFTER))
    public void handleInputEvents(CallbackInfo ci) {
        if (player.getMainHandStack().getItem() instanceof CreativeTrailCardItem cardItem) {
            if (cardItem.level == 1 && cardItem.onUse && currentScreen != null && player != null) {
                currentScreen.close();
                player.sendMessage(Text.translatable("text.wheat-mix.do_not_open"));
            }
        }
    }
}
