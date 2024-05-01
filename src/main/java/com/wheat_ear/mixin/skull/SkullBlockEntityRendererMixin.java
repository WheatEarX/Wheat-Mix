package com.wheat_ear.mixin.skull;

import com.google.common.collect.ImmutableMap;
import com.wheat_ear.block.VillagerHeadSkullType;
import net.minecraft.block.SkullBlock;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModelLoader;
import net.minecraft.client.render.entity.model.SkullEntityModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(SkullBlockEntityRenderer.class)
public abstract class SkullBlockEntityRendererMixin {

    @Shadow @Final private static Map<SkullBlock.SkullType, Identifier> TEXTURES;

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(BlockEntityRendererFactory.Context ctx, CallbackInfo ci) {
        TEXTURES.put(VillagerHeadSkullType.INSTANCE, new Identifier("textures/entity/villager/villager.png"));
    }

    @Inject(method = "getModels", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
    private static void getModels(EntityModelLoader modelLoader, CallbackInfoReturnable<Map<SkullBlock.SkullType, SkullBlockEntityModel>> cir, ImmutableMap.Builder<SkullBlock.SkullType, SkullBlockEntityModel> builder) {
        builder.put(VillagerHeadSkullType.INSTANCE, new SkullEntityModel(modelLoader.getModelPart(EntityModelLayers.VILLAGER)));
        cir.setReturnValue(builder.build());
    }
}
