package com.wheat_ear.mixin.crazyFire;

import com.wheat_ear.others.ModUtil;
import com.wheat_ear.others.TrackedBlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;

@Mixin(CampfireBlock.class)
public abstract class CampfireBlockMixin extends Block {
    public CampfireBlockMixin(Settings settings) {
        super(settings);
    }

    @Unique
    public final boolean crazy = false;

    @SuppressWarnings({"deprecation", "unchecked"})
    @Unique
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        System.out.println(11111);
        for (ServerPlayerEntity player: world.getPlayers()) {
            System.out.println(player);
            ArrayList<TrackedBlockPos> arrayList = ModUtil.getValue(ServerPlayerEntity.class, player, "posArrayList", ArrayList.class);
            arrayList.add(new TrackedBlockPos(pos, 0));
            ModUtil.setValue(ServerPlayerEntity.class, player, "posArrayList", arrayList);
        }
    }
}
