package com.chaosbuffalo.mkultra.item.test;

import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.google.common.collect.Multimap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class MagicDamageTestItem extends Item {
    private final static int AMOUNT = 1;

    private static final UUID MODIFIER_ID = UUID.fromString("4e92c641-82cf-40d9-9f1c-076314508307");

    private static AttributeModifier MODIFIER =
            new AttributeModifier(MODIFIER_ID, "Magic Damage Bonus", AMOUNT, PlayerAttributes.OP_INCREMENT)
                    .setSaved(false);

    public MagicDamageTestItem(String unlocalizedName) {
        super();
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.COMBAT);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {

        Multimap<String, AttributeModifier> mods = super.getAttributeModifiers(slot, stack);

        if (slot == EntityEquipmentSlot.OFFHAND) {
            mods.put(PlayerAttributes.MAGIC_ATTACK_DAMAGE.getName(), MODIFIER);
        }

        return mods;
    }
}
