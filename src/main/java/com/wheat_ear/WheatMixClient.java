package com.wheat_ear;

import com.wheat_ear.entity.ModEntityType;
import com.wheat_ear.options.ModKeyBinding;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class WheatMixClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModKeyBinding.registerEvents();
        registerRenderers();
    }

    public static void registerRenderers() {
        EntityRendererRegistry.register(ModEntityType.PEA, FlyingItemEntityRenderer::new);
    }
}
