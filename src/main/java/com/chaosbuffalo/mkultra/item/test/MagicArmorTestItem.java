package com.chaosbuffalo.mkultra.item.test;

import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.google.common.collect.Multimap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class MagicArmorTestItem extends Item {
    private final static int AMOUNT = 1;

    private static final UUID MODIFIER_ID = UUID.fromString("99739ffb-58ee-4053-b43a-61f59730827f");

    private static AttributeModifier MODIFIER =
            new AttributeModifier(MODIFIER_ID, "Magic Armor Bonus", AMOUNT, PlayerAttributes.OP_INCREMENT)
                    .setSaved(false);

    public MagicArmorTestItem(String unlocalizedName) {
        super();
        this.setTranslationKey(unlocalizedName);
        this.setCreativeTab(CreativeTabs.COMBAT);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {

        Multimap<String, AttributeModifier> mods = super.getAttributeModifiers(slot, stack);

        if (slot == EntityEquipmentSlot.OFFHAND) {
            mods.put(PlayerAttributes.MAGIC_ARMOR.getName(), MODIFIER);
        }

        return mods;
    }
}
