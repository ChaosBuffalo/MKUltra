package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.MKUltra;
import com.google.common.collect.Sets;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.*;

public class ArmorClass {
    public static final ArmorClass ROBES = new ArmorClass(new ResourceLocation(MKUltra.MODID, "armor_class.robes"));
    public static final ArmorClass LIGHT = new ArmorClass(new ResourceLocation(MKUltra.MODID, "armor_class.light")).inherit(ROBES);
    public static final ArmorClass MEDIUM = new ArmorClass(new ResourceLocation(MKUltra.MODID, "armor_class.medium")).inherit(LIGHT);
    public static final ArmorClass HEAVY = new ArmorClass(new ResourceLocation(MKUltra.MODID, "armor_class.heavy")).inherit(MEDIUM);
    public static final ArmorClass ALL = new AllowAllArmorClass(new ResourceLocation(MKUltra.MODID, "armor_class.all")).inherit(HEAVY);
    private static final ArmorClass NOT_CLASSIFIED = new ArmorClass(new ResourceLocation(MKUltra.MODID, "armor_class.undefined"));
    private static final List<ArmorClass> CHECK_ORDER = Arrays.asList(ROBES, LIGHT, MEDIUM, HEAVY);

    private final ResourceLocation location;
    private final Set<ItemArmor.ArmorMaterial> materials = Sets.newHashSet();
    private ArmorClass ancestor;
    private ArmorClass next;
    private final Set<ItemArmor> itemOverrides = Sets.newHashSet();

    public static void clearArmorClasses() {
        ROBES.clear();
        LIGHT.clear();
        MEDIUM.clear();
        HEAVY.clear();
    }

    private static ArmorClass getArmorClassForMaterial(ItemArmor.ArmorMaterial material) {
        for (ArmorClass armorClass : CHECK_ORDER) {
            if (armorClass.hasMaterial(material))
                return armorClass;
        }
        return NOT_CLASSIFIED;
    }

    public static ArmorClass getArmorClassForArmorItem(ItemArmor item) {
        for (ArmorClass armorClass : CHECK_ORDER) {
            if (armorClass.hasItemOverride(item)) {
                return armorClass;
            }
        }
        return getArmorClassForMaterial(item.getArmorMaterial());
    }

    public ArmorClass(ResourceLocation location) {
        this.location = location;
    }

    private void clear() {
        materials.clear();
        itemOverrides.clear();
    }

    @SideOnly(Side.CLIENT)
    public String getName() {
        return I18n.format(String.format("%s.%s.name", location.getNamespace(), location.getPath()));
    }

    public ResourceLocation getLocation() {
        return location;
    }

    private boolean hasMaterial(ItemArmor.ArmorMaterial material) {
        return materials.contains(material);
    }

    private boolean canWearMaterial(ItemArmor.ArmorMaterial material) {
        return hasMaterial(material) || (ancestor != null && ancestor.canWearMaterial(material));
    }

    public ArmorClass getSuccessor() {
        if (next != null)
            return next;
        return this;
    }

    public boolean canWear(ItemArmor item) {
        return canWearItem(item) || canWearMaterial(item.getArmorMaterial());
    }

    private boolean hasItemOverride(ItemArmor item) {
        return itemOverrides.contains(item);
    }

    private boolean canWearItem(ItemArmor item) {
        return hasItemOverride(item) || (ancestor != null && ancestor.canWearItem(item));
    }

    ArmorClass inherit(ArmorClass armorClass) {
        armorClass.next = this;
        ancestor = armorClass;
        return this;
    }

    public ArmorClass registerItem(ItemArmor item) {
        itemOverrides.add(item);
        return this;
    }

    public ArmorClass register(ItemArmor.ArmorMaterial material) {
        materials.add(material);
        return this;
    }

    private static class AllowAllArmorClass extends ArmorClass {
        AllowAllArmorClass(ResourceLocation location) {
            super(location);
        }

        @Override
        public boolean canWear(ItemArmor armor) {
            return true;
        }
    }
}
