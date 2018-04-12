package com.chaosbuffalo.mkultra.item;

import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.UUID;

/**
 * Created by Jacob on 4/11/2018.
 */
public class ItemCDRArmor extends ItemArmor {
    private final float bonus;
    private final UUID modifier_id;

    public ItemCDRArmor(String unlocalizedName, ItemArmor.ArmorMaterial material, int renderIndex,
                                EntityEquipmentSlot armorType, float bonusIn, UUID modIdIn) {
        super(material, renderIndex, armorType);
        this.bonus = bonusIn;
        this.modifier_id = modIdIn;
        this.setUnlocalizedName(unlocalizedName);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {

        Multimap<String, AttributeModifier> mods = super.getAttributeModifiers(slot, stack);
        if (slot == this.armorType) {
            if (this.bonus > 0) {
                AttributeModifier mod =
                        new AttributeModifier(this.modifier_id, "Cooldown Reduction", this.bonus, PlayerAttributes.OP_SCALE_ADDITIVE)
                                .setSaved(false);
                mods.put(PlayerAttributes.COOLDOWN.getName(), mod);
            }
        }


        return mods;
    }

}
