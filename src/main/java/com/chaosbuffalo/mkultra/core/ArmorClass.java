package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.init.ModItems;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.data.MaterialNames;
import com.mcmoddev.basemetals.init.Materials;
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
                .register(ModItems.IRON_THREADED_MAT)
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.STARSTEEL)));


        LIGHT.inherit(ROBES)
                .register(ItemArmor.ArmorMaterial.LEATHER)
                .register(ModItems.BONEDLEATHERMAT)
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.AQUARIUM)))
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.TIN)))
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.PEWTER)))
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.BISMUTH)));


        MEDIUM.inherit(LIGHT)
                .register(ItemArmor.ArmorMaterial.GOLD)
                .register(ItemArmor.ArmorMaterial.CHAIN)
                .register(ModItems.CHAINMAT)
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.COPPER)))
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.BRASS)))
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.SILVER)))
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.ELECTRUM)))
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.NICKEL)))
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.MITHRIL)))
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.QUARTZ)))
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.CUPRONICKEL)))
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.ANTIMONY)))
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.MITHRIL)));


        HEAVY.inherit(MEDIUM)
                .register(ItemArmor.ArmorMaterial.IRON)
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.LEAD)))
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.STEEL)))
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.INVAR)))
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.COLDIRON)))
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.ADAMANTINE)))
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.OBSIDIAN)))
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.ZINC)))
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.EMERALD)))
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.PLATINUM)))
                .register(com.mcmoddev.basemetals.init.Materials.getArmorMaterialFor(
                        Materials.getMaterialByName(MaterialNames.BRONZE)));
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
