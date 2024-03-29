package com.wheat_ear.others;

import com.wheat_ear.enchantment.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import java.lang.reflect.Field;
import java.util.Random;

public class ModUtil {

    public static final Random random = new Random();

    @SuppressWarnings("unchecked")
    public static <T> T getValue(Class<?> classType, Class<T> ignoredReturnType, Object object, String fieldName) {
        try {
            Field field = classType.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(object);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setValue(Class<?> classType, Object object, String fieldName, Object value) {
        try {
            Field field = classType.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(object, value);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getRandomFromRegistry(Registry<T> registry) {
        return registry.get(random.nextInt(registry.size()));
    }

    public static void setGravityCancelled(Entity entity) {
        setValue(Entity.class, entity, "gravityCancelled", true);
    }

    public static boolean canSetGravityCancelled(ItemStack stack) {
        return EnchantmentHelper.getLevel(ModEnchantments.GRAVITY_CANCELLATION, stack) > 0;
    }

    public static int getEnchantmentLevelFromNbt(NbtCompound nbt, Enchantment enchantment) {
        NbtList nbtList = nbt.getList("Enchantments", 10);

        for (int i = 0; i < nbtList.size(); ++i) {
            NbtCompound nbtCompound = nbtList.getCompound(i);
            Identifier identifier = EnchantmentHelper.getIdFromNbt(nbtCompound);

            if (identifier != null && identifier.equals(Registries.ENCHANTMENT.getId(enchantment))) {
                return EnchantmentHelper.getLevelFromNbt(nbtCompound);
            }
        }

        return 0;
    }
}
