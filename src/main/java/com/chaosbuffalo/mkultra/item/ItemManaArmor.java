package com.chaosbuffalo.mkultra.item;

import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import java.util.UUID;

/**
 * Created by Jacob on 3/22/2018.
 */
public class ItemManaArmor extends ItemArmor {
    private final int bonus_mana;
    private final UUID modifier_id;

    public ItemManaArmor(String unlocalizedName, ItemArmor.ArmorMaterial material, int renderIndex,
                         EntityEquipmentSlot armorType, int manaIn, UUID modIdIn) {
        super(material, renderIndex, armorType);
        this.bonus_mana = manaIn;
        this.modifier_id = modIdIn;
        this.setUnlocalizedName(unlocalizedName);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {

        Multimap<String, AttributeModifier> mods = super.getAttributeModifiers(slot, stack);
        if (slot == this.armorType) {
            if (this.bonus_mana > 0) {
                AttributeModifier mod =
                        new AttributeModifier(this.modifier_id, "Bonus Mana", this.bonus_mana, PlayerAttributes.OP_INCREMENT)
                                .setSaved(false);
                mods.put(PlayerAttributes.MAX_MANA.getName(), mod);
            }
        }


        return mods;
    }

}
