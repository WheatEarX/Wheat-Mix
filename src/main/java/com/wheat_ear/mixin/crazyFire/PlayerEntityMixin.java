package com.wheat_ear.mixin.crazyFire;

import com.wheat_ear.others.TrackedBlockPos;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity {
    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique
    ArrayList<TrackedBlockPos> posArrayList;

    @Inject(method = "tick", at = @At(value = "RETURN"))
    public void tick(CallbackInfo ci) {
        if (posArrayList != null) {
            for (TrackedBlockPos pos: posArrayList) {
                BlockPos blockPos = pos.blockPos;
                if (this.getWorld().getBlockState(blockPos).isOf(Blocks.CAMPFIRE)) {
                    if (pos.time <= 0) {
                        pos.time = 100;
                        this.refreshPositionAfterTeleport(blockPos.getX() + 0.5, blockPos.getY() + 1, blockPos.getZ() + 0.5);
                    }
                }
                else {
                    posArrayList.remove(pos);
                }
                pos.time -= 1;
            }
        }
    }
}
