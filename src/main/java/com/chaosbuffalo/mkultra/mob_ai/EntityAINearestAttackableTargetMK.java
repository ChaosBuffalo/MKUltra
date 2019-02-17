package com.chaosbuffalo.mkultra.mob_ai;

import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MKUMobData;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.IAttributeInstance;

public class EntityAINearestAttackableTargetMK extends EntityAINearestAttackableTarget {


    public EntityAINearestAttackableTargetMK(EntityCreature creature, Class classTarget, boolean checkSight) {
        super(creature, classTarget, checkSight);
    }

    @Override
    protected double getTargetDistance(){
        if (target != null){
            IAttributeInstance iattributeinstance = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
            return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
        } else {
            IMobData mobData = MKUMobData.get(this.taskOwner);
            return mobData.getAggroRange();
        }
    }
}
