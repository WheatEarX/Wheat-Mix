package com.wheat_ear.mixin.creative_trail_card;

import com.wheat_ear.item.CreativeTrailCardItem;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Unique BlockPos nowPos;

    @Unique int messageTicks = 0;

    @Shadow public abstract void sendMessage(Text message, boolean overlay);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void tick(CallbackInfo ci) {
        ItemStack stack = this.getMainHandStack();
        if (stack.getItem() instanceof CreativeTrailCardItem cardItem) {
            if (cardItem.level <= 2) {
                ++messageTicks;
                if (!cardItem.firstUse) {
                    nowPos = this.getBlockPos();
                    cardItem.firstUse = false;
                }
                if (cardItem.onUse && nowPos != null) {
                    this.refreshPositionAfterTeleport(nowPos.down().toCenterPos());
                    if (messageTicks >= 5) {
                        this.sendMessage(Text.translatable("text.wheat-mix.do_not_move"));
                        messageTicks = 0;
                    }
                }
            }
        }
    }
}
