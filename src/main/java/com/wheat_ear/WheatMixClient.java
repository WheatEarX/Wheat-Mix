package com.wheat_ear;

import com.wheat_ear.block.FlatBlockEntityRenderer;
import com.wheat_ear.block.ModBlockEntityType;
import com.wheat_ear.entity.ModEntityType;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class WheatMixClient implements ClientModInitializer {

    public void registerRenderers() {
        EntityRendererRegistry.register(ModEntityType.ZOMBIE_EGG, FlyingItemEntityRenderer::new);
        BlockEntityRendererFactories.register(ModBlockEntityType.FLAT_BLOCK_ENTITY, FlatBlockEntityRenderer::new);
    }

    @Override
    public void onInitializeClient() {
        registerRenderers();
    }
}
