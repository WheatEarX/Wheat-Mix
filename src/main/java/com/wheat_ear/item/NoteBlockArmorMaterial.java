package com.wheat_ear.item;

import net.minecraft.block.Blocks;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public class NoteBlockArmorMaterial implements ArmorMaterial {
    public static NoteBlockArmorMaterial INSTANCE = new NoteBlockArmorMaterial();
    @Override
    public int getDurability(ArmorItem.Type type) {
        return 250;
    }

    @Override
    public int getProtection(ArmorItem.Type type) {
        return 4;
    }

    @Override
    public int getEnchantability() {
        return 10;
    }

    @Override
    public SoundEvent getEquipSound() {
        return SoundEvents.ITEM_ARMOR_EQUIP_DIAMOND;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.ofItems(Blocks.NOTE_BLOCK);
    }

    @Override
    public String getName() {
        return "note_block";
    }

    @Override
    public float getToughness() {
        return 0;
    }

    @Override
    public float getKnockbackResistance() {
        return 0;
    }
}
