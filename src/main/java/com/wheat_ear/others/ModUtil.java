package com.wheat_ear.others;

import com.wheat_ear.enchantment.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;

@SuppressWarnings("JavaReflectionMemberAccess")
public class ModUtil {
    public static final Class<Entity> entityClass = Entity.class;

    public static void setGravityCancelled(Entity entity) {
        try {
            Field field = entityClass.getDeclaredField("gravityCancelled");
            field.setAccessible(true);
            field.setBoolean(entity, true);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean canSetGravityCancelled(ItemStack stack) {
        return EnchantmentHelper.getLevel(ModEnchantments.GRAVITY_CANCELLATION, stack) > 0;
    }

    public static int getEnchantmentFromNbt(NbtCompound nbt) {
        NbtList nbtList = nbt.getList("Enchantments", 10);

        for (int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            Identifier identifier = EnchantmentHelper.getIdFromNbt(nbtCompound);

            if (identifier != null && identifier.equals(Registries.ENCHANTMENT.getId(ModEnchantments.GRAVITY_CANCELLATION))) {
                return EnchantmentHelper.getLevelFromNbt(nbtCompound);
            }
        }

        return 0;
    }
}
