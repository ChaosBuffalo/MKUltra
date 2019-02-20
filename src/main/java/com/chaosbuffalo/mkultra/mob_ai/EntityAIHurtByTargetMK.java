package com.chaosbuffalo.mkultra.mob_ai;

import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class EntityAIHurtByTargetMK extends EntityAITarget {
    private final boolean entityCallsForHelp;
    private int revengeTimerOld;
    private static final double CALL_FOR_HELP_RANGE = 3;

    public EntityAIHurtByTargetMK(EntityCreature entityIn, boolean callsForHelp) {
        super(entityIn, true);
        this.entityCallsForHelp = callsForHelp;
        this.setMutexBits(1);
    }

    public boolean shouldExecute() {
        int revengeTimer = this.taskOwner.getRevengeTimer();
        EntityLivingBase revengeTarget = this.taskOwner.getRevengeTarget();
        return revengeTimer != this.revengeTimerOld && Targeting.isValidTarget(Targeting.TargetType.ENEMY,
                this.taskOwner, revengeTarget, true);
    }

    public void startExecuting() {
        Log.debug("Start Executing Revenge Target");
        this.taskOwner.setAttackTarget(this.taskOwner.getRevengeTarget());
        this.target = this.taskOwner.getAttackTarget();
        this.revengeTimerOld = this.taskOwner.getRevengeTimer();
        this.unseenMemoryTicks = 300;
        if (this.entityCallsForHelp) {
            this.alertOthers();
        }
        super.startExecuting();
    }
    protected AxisAlignedBB getTargetableArea(double distance) {
        return taskOwner.getEntityBoundingBox().grow(distance, distance, distance);
    }


    protected void alertOthers() {
        List<Entity> nearbyFriendlies = taskOwner.world.getEntitiesWithinAABB(EntityLivingBase.class,
                getTargetableArea(CALL_FOR_HELP_RANGE),
                e -> Targeting.isValidTarget(Targeting.TargetType.FRIENDLY, taskOwner, e, true));
        for (Entity friendly : nearbyFriendlies){
            if (friendly instanceof EntityCreature){
                if (Targeting.isValidTarget(Targeting.TargetType.ENEMY, friendly, taskOwner.getRevengeTarget(),
                        true)){
                    ((EntityCreature) friendly).setAttackTarget(taskOwner.getRevengeTarget());
                }
            }
        }
    }

}