package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.init.ModItems;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.item.ItemArmor;

import java.util.List;
import java.util.Set;

public class ArmorClass {

    public static ArmorClass HEAVY = new ArmorClass();
    public static ArmorClass MEDIUM = new ArmorClass();
    public static ArmorClass LIGHT = new ArmorClass();
    public static ArmorClass ROBES = new ArmorClass();


    public static void registerDefaults() {
        ROBES.register(ModItems.ROBEMAT)
                .register(ModItems.COPPER_THREADED_MAT)
                .register(ModItems.IRON_THREADED_MAT);


        LIGHT.inherit(ROBES)
                .register(ItemArmor.ArmorMaterial.LEATHER);


        MEDIUM.inherit(LIGHT)
                .register(ItemArmor.ArmorMaterial.GOLD)
                .register(ItemArmor.ArmorMaterial.CHAIN)
                .register(ModItems.CHAINMAT);


        HEAVY.inherit(MEDIUM)
                .register(ItemArmor.ArmorMaterial.IRON);
    }

    private Set<ItemArmor.ArmorMaterial> materials = Sets.newHashSet();
    private List<ArmorClass> ancestors = Lists.newArrayList();

    public ArmorClass() {
    }

    public boolean canWear(ItemArmor.ArmorMaterial material) {
        return materials.contains(material) ||
                ancestors.stream().anyMatch(a -> a.canWear(material));
    }

    public ArmorClass inherit(ArmorClass armorClass) {
        ancestors.add(armorClass);
        return this;
    }

    public ArmorClass register(ItemArmor.ArmorMaterial material) {
        materials.add(material);
        return this;
    }
}
