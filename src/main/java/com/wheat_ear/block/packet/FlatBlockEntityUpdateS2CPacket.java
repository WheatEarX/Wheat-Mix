package com.wheat_ear.block.packet;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.ChunkPos;

public class FlatBlockEntityUpdateS2CPacket extends BlockEntityUpdateS2CPacket {
    public ChunkPos chunkPos;

    public FlatBlockEntityUpdateS2CPacket(PacketByteBuf buf) {
        super(buf);
    }

    public static BlockEntityUpdateS2CPacket create(BlockEntity blockEntity) {
        FlatBlockEntityUpdateS2CPacket packet = (FlatBlockEntityUpdateS2CPacket) BlockEntityUpdateS2CPacket.create(blockEntity);
        packet.chunkPos = blockEntity.getWorld().getWorldChunk(blockEntity.getPos()).getPos();

        return packet;
    }

    @Override
    public void write(PacketByteBuf buf) {
        super.write(buf);

        buf.writeChunkPos(chunkPos);
    }
}
