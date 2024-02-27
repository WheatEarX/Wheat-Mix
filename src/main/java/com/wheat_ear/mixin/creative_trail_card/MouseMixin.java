package com.wheat_ear.mixin.creative_trail_card;

import com.wheat_ear.item.CreativeTrailCardItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Mouse.class)
public class MouseMixin {
    @Shadow @Final private MinecraftClient client;

    @SuppressWarnings("InvalidInjectorMethodSignature")
    @Inject(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    public void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci, boolean bl, double d, double e, double f, int i, int j, int k) {
        assert this.client.player != null;
        if (client.player.getMainHandStack().getItem() instanceof CreativeTrailCardItem cardItem) {
            if (cardItem.level <= 2) {
                client.player.getInventory().scrollInHotbar(0);
            }
        }
    }
}
