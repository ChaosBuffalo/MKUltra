package com.chaosbuffalo.mkultra.mob_ai;

import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MKUMobData;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.Collections;
import java.util.List;

public class EntityAINearestAttackableTargetMK extends EntityAITarget {

    private int targetChance;


    public EntityAINearestAttackableTargetMK(EntityCreature creature, boolean checkSight, int targetChance) {
        super(creature, checkSight);
        this.targetChance = targetChance;
        this.setMutexBits(1);
    }

    public EntityAINearestAttackableTargetMK(EntityCreature creature, boolean checkSight) {
        this(creature, checkSight, 10);
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

    protected AxisAlignedBB getTargetableArea(double distance) {
        return this.taskOwner.getEntityBoundingBox().grow(distance, distance, distance);
    }

    public int compareDistance(Entity a, Entity b){
        double aDist = taskOwner.getDistanceSq(a);
        double bDist = taskOwner.getDistanceSq(b);
        if (aDist < bDist) {
            return -1;
        } else {
            return aDist > bDist ? 1 : 0;
        }
    }

    @Override
    public boolean shouldExecute() {
        if (this.taskOwner.getAttackTarget() != null || (this.targetChance > 0 && this.taskOwner.getRNG().nextInt(this.targetChance) != 0)) {
            return false;
        } else {
            List<Entity> list = taskOwner.world.getEntitiesInAABBexcluding(taskOwner,
                    getTargetableArea(getTargetDistance()),
                    e -> Targeting.isValidTarget(Targeting.TargetType.ENEMY, taskOwner, e, true));
            if (list.size() > 0){
                Entity min = Collections.min(list, this::compareDistance);
                if (min instanceof EntityLivingBase){
                    target = (EntityLivingBase) min;
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }

    public void startExecuting() {
//        Log.info("Start Executing Attack Nearest: %s", taskOwner.toString());
        this.taskOwner.setAttackTarget(this.target);
        super.startExecuting();
    }
}
