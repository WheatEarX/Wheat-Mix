package com.wheat_ear.others;

import net.minecraft.util.math.BlockPos;

public class TrackedBlockPos {
    public BlockPos blockPos;
    public long time;

    public TrackedBlockPos(BlockPos blockPos, long time) {
        this.blockPos = blockPos;
        this.time = time;
    }
}
