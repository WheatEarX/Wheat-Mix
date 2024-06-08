package com.wheat_ear.block;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.BlockModelRenderer;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.BakedQuad;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.BitSet;
import java.util.List;
import java.util.Objects;

public class FlatBlockEntityRenderer implements BlockEntityRenderer<FlatBlockEntity> {
    public FlatBlockEntityRenderer(BlockEntityRendererFactory.Context ignoredCtx) {
    }

    @Override
    public void render(FlatBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        MinecraftClient client = MinecraftClient.getInstance();
        BlockRenderManager blockRenderManager = client.getBlockRenderManager();
        BlockModelRenderer blockModelRenderer = blockRenderManager.blockModelRenderer;

        World world = Objects.requireNonNull(entity.getWorld());
        BlockPos pos = entity.getPos();

        int l = WorldRenderer.getLightmapCoordinates(world, pos);
        BitSet flags = new BitSet(3);

        BlockState state = entity.blockState;
        List<BakedQuad> quads = blockRenderManager.getModel(state).getQuads(state, Direction.UP, Random.create(0L));

        blockModelRenderer.renderQuadsFlat(entity.getWorld(), state, pos, l, OverlayTexture.DEFAULT_UV, true, matrices, vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state)), quads, flags);
    }
}
