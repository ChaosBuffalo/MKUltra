package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.init.ModItems;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.item.ItemArmor;

import java.util.List;
import java.util.Set;

public class ArmorClass {

    public static final ArmorClass HEAVY = new ArmorClass();
    public static final ArmorClass MEDIUM = new ArmorClass();
    public static final ArmorClass LIGHT = new ArmorClass();
    public static final ArmorClass ROBES = new ArmorClass();


    public static void registerDefaults() {
        ROBES.register(ModItems.ROBEMAT)
                .register(ModItems.COPPER_THREADED_MAT)
                .register(ModItems.IRON_THREADED_MAT);


        LIGHT.inherit(ROBES)
                .register(ItemArmor.ArmorMaterial.LEATHER)
                .register(ModItems.BONEDLEATHERMAT)
                .register(ModItems.STEEL_INFUSED_BONE_MAT);


        MEDIUM.inherit(LIGHT)
                .register(ItemArmor.ArmorMaterial.GOLD)
                .register(ItemArmor.ArmorMaterial.CHAIN)
                .register(ModItems.CHAINMAT)
                .register(ModItems.OBSIDIAN_CHAIN);


        HEAVY.inherit(MEDIUM)
                .register(ItemArmor.ArmorMaterial.IRON)
                .register(ModItems.DIAMOND_DUSTED_INVAR_MAT);
    }

    private final Set<ItemArmor.ArmorMaterial> materials = Sets.newHashSet();
    private final List<ArmorClass> ancestors = Lists.newArrayList();

    public ArmorClass() {
    }

    public boolean canWear(ItemArmor.ArmorMaterial material) {
        return materials.contains(material) ||
                ancestors.stream().anyMatch(a -> a.canWear(material));
    }

    private ArmorClass inherit(ArmorClass armorClass) {
        ancestors.add(armorClass);
        return this;
    }

    public ArmorClass register(ItemArmor.ArmorMaterial material) {
        materials.add(material);
        return this;
    }
}
