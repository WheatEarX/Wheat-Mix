package com.wheat_ear.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
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
        BlockState state = entity.blockState;

        MinecraftClient client = MinecraftClient.getInstance();
        BlockRenderManager blockRenderManager = client.getBlockRenderManager();
        BlockModelRenderer blockModelRenderer = blockRenderManager.blockModelRenderer;

        World world = Objects.requireNonNull(entity.getWorld());
        BlockPos pos = entity.getPos();

        int l = WorldRenderer.getLightmapCoordinates(world, pos);
        List<BakedQuad> list = blockRenderManager.getModel(state).getQuads(state, Direction.UP, Random.create());
        BitSet flags = new BitSet(3);

        blockModelRenderer.renderQuadsFlat(entity.getWorld(), state, entity.getPos(), l, OverlayTexture.DEFAULT_UV, true, matrices, list, flags);

        //MinecraftClient.getInstance().getBlockRenderManager().renderBlock(state, entity.getPos(), entity.getWorld(), matrices, vertexConsumers.getBuffer(RenderLayers.getBlockLayer(state)), false, Random.create());
    }
}
