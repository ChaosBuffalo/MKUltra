package com.chaosbuffalo.mkultra.item;

import com.chaosbuffalo.mkultra.item.interfaces.IExtendedReach;
import com.google.common.collect.Multimap;
import com.mcmoddev.lib.item.ItemMMDSword;
import com.mcmoddev.lib.material.MMDMaterial;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemSword;


public class ItemRangeSword extends ItemMMDSword implements IExtendedReach {
    private float reach;
    private float realAttackDamage;

    public ItemRangeSword(String unlocalizedName, MMDMaterial mat, float reach) {
        super(mat);
        setUnlocalizedName(unlocalizedName);
        this.reach = reach;
        // TODO: verify this is right for 1.12
        this.realAttackDamage = 4.0F + mat.getBaseAttackDamage();
        //this.realAttackDamage = 4.0F + mat.getDamageVsEntity();
    }

    @Override
    public float getReach() {
        return this.reach;
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);

        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", (double) this.realAttackDamage, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -3.2000000953674316D, 0));
        }

        return multimap;
    }
}

