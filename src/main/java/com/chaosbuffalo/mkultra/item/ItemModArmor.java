package com.chaosbuffalo.mkultra.item;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;

/**
 * Created by Jacob on 3/16/2016.
 */
public class ItemModArmor extends ItemArmor {

    public ItemModArmor(String unlocalizedName, ItemArmor.ArmorMaterial material, int renderIndex, EntityEquipmentSlot armorType) {
        super(material, renderIndex, armorType);
        this.setUnlocalizedName(unlocalizedName);
    }

}
