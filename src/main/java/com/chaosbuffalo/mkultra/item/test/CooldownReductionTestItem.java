package com.chaosbuffalo.mkultra.item.test;

import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.google.common.collect.Multimap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public class CooldownReductionTestItem extends Item {
    private static final UUID MODIFIER_ID = UUID.fromString("f13c8c87-f692-4779-bbea-9359fe711c12");

    private static AttributeModifier MODIFIER =
            new AttributeModifier(MODIFIER_ID, "Cooldown Rate Reduction", -0.2, PlayerAttributes.OP_INCREMENT)
                    .setSaved(false);

    public CooldownReductionTestItem(String unlocalizedName) {
        super();
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.COMBAT);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {

        Multimap<String, AttributeModifier> mods = super.getAttributeModifiers(slot, stack);

        if (slot == EntityEquipmentSlot.OFFHAND) {
            mods.put(PlayerAttributes.COOLDOWN.getName(), MODIFIER);
        }

        return mods;
    }
}
