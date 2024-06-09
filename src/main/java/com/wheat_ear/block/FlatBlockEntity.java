package com.wheat_ear.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class FlatBlockEntity extends BlockEntity {
    public BlockState blockState = Blocks.STONE.getDefaultState();
    private static final String NBT_KEY = "BlockState";

    public FlatBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityType.FLAT_BLOCK_ENTITY, pos, state);
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        nbt.put(NBT_KEY, NbtHelper.fromBlockState(blockState));
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if (getWorld() != null) {
            blockState = NbtHelper.toBlockState(getWorld().createCommandRegistryWrapper(RegistryKeys.BLOCK), nbt.getCompound(NBT_KEY));
        }
    }

    @Nullable
    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
