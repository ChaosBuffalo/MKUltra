package com.chaosbuffalo.mkultra.item;

import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.HashSet;
import java.util.UUID;

/**
 * Created by Jacob on 3/23/2018.
 */
public class ItemAttributeArmor extends ItemArmor {
    private final HashSet<ItemAttributeEntry> attributes;

    public ItemAttributeArmor(String unlocalizedName, ItemArmor.ArmorMaterial material, int renderIndex,
                              EntityEquipmentSlot armorType, HashSet<ItemAttributeEntry> attributes) {
        super(material, renderIndex, armorType);
        this.attributes = attributes;
        this.setUnlocalizedName(unlocalizedName);
    }

    public ItemAttributeArmor(String unlocalizedName, ItemArmor.ArmorMaterial material, int renderIndex,
    EntityEquipmentSlot armorType, ItemAttributeEntry attribute) {
        super(material, renderIndex, armorType);
        this.attributes = new HashSet<>();
        this.attributes.add(attribute);
        this.setUnlocalizedName(unlocalizedName);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        Multimap<String, AttributeModifier> mods = super.getAttributeModifiers(slot, stack);
        if (slot == this.armorType) {
            for (ItemAttributeEntry attr_entry : attributes){
                AttributeModifier mod = new AttributeModifier(attr_entry.modifier_id, attr_entry.name,
                    attr_entry.amount, attr_entry.operation)
                    .setSaved(false);
                mods.put(attr_entry.attr_name, mod);
            }
        }
        return mods;
    }

}
