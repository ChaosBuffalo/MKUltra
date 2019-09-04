package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.MKUltra;
import com.google.common.collect.Sets;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

public class ArmorClass {
    public static final ArmorClass ROBES = new ArmorClass(new ResourceLocation(MKUltra.MODID, "armor_class.robes"));
    public static final ArmorClass LIGHT = new ArmorClass(new ResourceLocation(MKUltra.MODID, "armor_class.light")).inherit(ROBES);
    public static final ArmorClass MEDIUM = new ArmorClass(new ResourceLocation(MKUltra.MODID, "armor_class.medium")).inherit(LIGHT);
    public static final ArmorClass HEAVY = new ArmorClass(new ResourceLocation(MKUltra.MODID, "armor_class.heavy")).inherit(MEDIUM);
    public static final ArmorClass ALL = new AllowAllArmorClass(new ResourceLocation(MKUltra.MODID, "armor_class.all")).inherit(HEAVY);
    public static final ArmorClass NOT_CLASSIFIED = new ArmorClass(new ResourceLocation(MKUltra.MODID, "armor_class.undefined"));

    private final ResourceLocation location;

    public static void clearArmorClasses() {
        HEAVY.materials.clear();
        MEDIUM.materials.clear();
        LIGHT.materials.clear();
        ROBES.materials.clear();
    }

    private final Set<ItemArmor.ArmorMaterial> materials = Sets.newHashSet();
    private ArmorClass ancestor;
    private ArmorClass next;
    private final Set<ItemArmor> itemOverrides = Sets.newHashSet();

    private static final ArrayList<ArmorClass> CHECK_ORDER = new ArrayList<>();


    static {
        CHECK_ORDER.add(ROBES);
        CHECK_ORDER.add(LIGHT);
        CHECK_ORDER.add(MEDIUM);
        CHECK_ORDER.add(HEAVY);
    }

    public ArmorClass(ResourceLocation location) {
        this.location = location;
    }

    @SideOnly(Side.CLIENT)
    public String getName() {
        return I18n.format(String.format("%s.%s.name", location.getNamespace(), location.getPath()));
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public boolean canWearMaterial(ItemArmor.ArmorMaterial material) {
        return materials.contains(material) || (ancestor != null && ancestor.canWearMaterial(material));
    }

    public static ArmorClass getArmorClassForArmorMat(ItemArmor.ArmorMaterial material) {
        if (ROBES.materials.contains(material)) {
            return ROBES;
        } else if (LIGHT.materials.contains(material)) {
            return LIGHT;
        } else if (MEDIUM.materials.contains(material)) {
            return MEDIUM;
        } else if (HEAVY.materials.contains(material)) {
            return HEAVY;
        } else {
            return NOT_CLASSIFIED;
        }
    }

    public static ArmorClass getArmorClassForArmorItem(ItemArmor item){
        for (ArmorClass armorClass : CHECK_ORDER){
              if (armorClass.itemIsOverriden(item)){
                  return armorClass;
              }
        }
        return getArmorClassForArmorMat(item.getArmorMaterial());
    }

    public ArmorClass getSuccessor() {
        if (next != null)
            return next;
        return this;
    }

    public boolean canWear(ItemArmor item){
        return canWearItem(item) || canWearMaterial(item.getArmorMaterial());
    }

    public boolean itemIsOverriden(ItemArmor item){
        return itemOverrides.contains(item);
    }

    public boolean canWearItem(ItemArmor item){
        return itemOverrides.contains(item) || (ancestor != null && ancestor.canWearItem(item));
    }

    protected ArmorClass inherit(ArmorClass armorClass) {
        armorClass.next = this;
        ancestor = armorClass;
        return this;
    }

    public ArmorClass registerItem(ItemArmor item){
        itemOverrides.add(item);
        return this;
    }

    public ArmorClass register(ItemArmor.ArmorMaterial material) {
        materials.add(material);
        return this;
    }

    private static class AllowAllArmorClass extends ArmorClass {
        public AllowAllArmorClass(ResourceLocation location) {
            super(location);
        }

        @Override
        public boolean canWearMaterial(ItemArmor.ArmorMaterial material) {
            return true;
        }
    }
}
