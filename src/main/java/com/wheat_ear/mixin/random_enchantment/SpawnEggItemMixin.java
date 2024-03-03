package com.wheat_ear.mixin.random_enchantment;

import com.wheat_ear.enchantment.ModEnchantments;
import com.wheat_ear.others.ModUtil;
import net.minecraft.entity.EntityType;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnEggItem.class)
public class SpawnEggItemMixin {
    @Inject(method = "getEntityType", at = @At("HEAD"), cancellable = true)
    public void getEntityType(NbtCompound nbt, CallbackInfoReturnable<EntityType<?>> cir) {
        if (ModUtil.getEnchantmentLevelFromNbt(nbt, ModEnchantments.RANDOM) > 0) {
            EntityType<?> entityType = ModUtil.getRandomFromRegistry(Registries.ENTITY_TYPE);
            cir.setReturnValue(entityType);
        }
    }
}
