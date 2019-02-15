package com.chaosbuffalo.mkultra.mob_ai;

import com.chaosbuffalo.mkultra.core.BaseMobAbility;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MobAbilityTracker;
import com.chaosbuffalo.mkultra.log.Log;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.EnumHand;

public class EntityAIRangedSpellAttack extends EntityAIBase {
    private final EntityLivingBase entity;
    private int attackCooldown;
    private int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;
    private MobAbilityTracker currentAbility;
    protected IMobData mobData;

    public EntityAIRangedSpellAttack(EntityLivingBase entity, int attackCooldown, IMobData mobData) {
        this.entity = entity;
        this.attackCooldown = attackCooldown;
        this.mobData = mobData;
        this.setMutexBits(3);
    }

    public void setAttackCooldown(int cooldownIn) {
        this.attackCooldown = cooldownIn;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (mobData.hasAbilities() && entity instanceof EntityLiving) {
            for (MobAbilityTracker tracker : mobData.getAbilityTrackers()) {
                if (!tracker.isAbilityOnCooldown()) {
                    if (tracker.getAbility().getAbilityType() == BaseMobAbility.AbilityType.ATTACK &&
                            ((EntityLiving) entity).getAttackTarget() != null) {
                        currentAbility = tracker;
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        if (!(entity instanceof EntityLiving)){
            return false;
        }
        return (currentAbility != null && !currentAbility.isAbilityOnCooldown() || !((EntityLiving)entity).getNavigator().noPath());
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        super.startExecuting();
        Log.info("Start executing shadow dash");
        if (entity instanceof IRangedAttackMob){
            ((IRangedAttackMob)this.entity).setSwingingArms(true);
        }

    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        super.resetTask();
        if (entity instanceof IRangedAttackMob){
            ((IRangedAttackMob)this.entity).setSwingingArms(true);
        }
        this.seeTime = 0;
        this.attackTime = -1;
        this.entity.resetActiveHand();
        this.currentAbility = null;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public double getMaxDistance(int level) {
        double d = this.currentAbility.getAbility().getDistance(level);
        return d * d;
    }

    @Override
    public void updateTask() {
        if (entity instanceof EntityLiving){
            EntityLiving entLiv = (EntityLiving)entity;
            EntityLivingBase entitylivingbase = entLiv.getAttackTarget();
            if (entitylivingbase != null) {

                double d0 = this.entity.getDistanceSq(entitylivingbase.posX, entitylivingbase.getEntityBoundingBox().minY, entitylivingbase.posZ);
                boolean canSee = entLiv.getEntitySenses().canSee(entitylivingbase);
                boolean hasSeen = this.seeTime > 0;
                double maxAttackDistance = getMaxDistance(mobData.getMobLevel());
                if (canSee != hasSeen) {
                    this.seeTime = 0;
                }

                if (canSee) {
                    ++this.seeTime;
                } else {
                    --this.seeTime;
                }

                if (d0 <= maxAttackDistance && this.seeTime >= 20) {
                    entLiv.getNavigator().clearPath();
                    ++this.strafingTime;
                } else {
                    entLiv.getNavigator().tryMoveToEntityLiving(entitylivingbase, 1.0);
                    this.strafingTime = -1;
                }

                if (this.strafingTime >= 20) {
                    if ((double) this.entity.getRNG().nextFloat() < 0.3D) {
                        this.strafingClockwise = !this.strafingClockwise;
                    }

                    if ((double) this.entity.getRNG().nextFloat() < 0.3D) {
                        this.strafingBackwards = !this.strafingBackwards;
                    }

                    this.strafingTime = 0;
                }

                if (this.strafingTime > -1) {
                    if (d0 > maxAttackDistance * 0.75F) {
                        this.strafingBackwards = false;
                    } else if (d0 < maxAttackDistance * 0.25F) {
                        this.strafingBackwards = true;
                    }
                    entLiv.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    entLiv.faceEntity(entitylivingbase, 30.0F, 30.0F);
                } else {
                    entLiv.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
                }

                if (this.entity.isHandActive()) {
                    if (!canSee && this.seeTime < -60) {
                        Log.info("Cant see");
                        this.entity.resetActiveHand();
                    } else if (canSee) {
                        Log.info("Casting ability");
                        this.entity.resetActiveHand();
                        currentAbility.useAbility(entitylivingbase);
                        this.attackTime = this.attackCooldown;
                    }
                } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                    Log.info("setting main hand");
                    this.entity.setActiveHand(EnumHand.MAIN_HAND);
                } else {
                    Log.info("Over here");
                }
            }
        }
    }
}

