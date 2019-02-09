package com.chaosbuffalo.mkultra.item;

import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

/**
 * Created by Jacob on 3/23/2018.
 */
public class ItemAttributeArmor extends ItemArmor {
    private final HashSet<ItemAttributeEntry> attributes;

    public static final UUID CHEST_UUID = UUID.fromString("77ab4b54-5885-4f7f-ab41-71af536309d1");
    public static final UUID LEGGINGS_UUID = UUID.fromString("8d827c58-8f61-4c77-8dcd-f62f0e69121b");
    public static final UUID HELMET_UUID = UUID.fromString("fb16408c-0421-4138-a283-8da7038e5970");
    public static final UUID FEET_UUID = UUID.fromString("fb16408c-0421-4138-a283-8da7038e5972");

    public final static HashMap<EntityEquipmentSlot, UUID> uuids = new HashMap<>();

    static {
        uuids.put(EntityEquipmentSlot.CHEST, CHEST_UUID);
        uuids.put(EntityEquipmentSlot.FEET, FEET_UUID);
        uuids.put(EntityEquipmentSlot.HEAD, HELMET_UUID);
        uuids.put(EntityEquipmentSlot.LEGS, LEGGINGS_UUID);
    }

    public ItemAttributeArmor(String unlocalizedName, ItemArmor.ArmorMaterial material, int renderIndex,
                              EntityEquipmentSlot armorType, ItemAttributeEntry... attributes) {
        super(material, renderIndex, armorType);
        this.attributes = Sets.newHashSet(attributes);
        this.setTranslationKey(unlocalizedName);
    }

    public ItemAttributeArmor addAttribute(ItemAttributeEntry entry){
        this.attributes.add(entry);
        return this;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> mods = super.getAttributeModifiers(slot, stack);
        if (slot == this.armorType) {
            for (ItemAttributeEntry attr_entry : attributes){
                AttributeModifier mod = new AttributeModifier(uuids.get(this.armorType),
                        attr_entry.attr.getDescription(), attr_entry.amount, attr_entry.operation)
                    .setSaved(false);
                mods.put(attr_entry.attr.getName(), mod);
            }
        }
        return mods;
    }

}
