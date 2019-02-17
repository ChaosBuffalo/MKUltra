package com.chaosbuffalo.mkultra.mob_ai;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.BaseMobAbility;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MobAbilityTracker;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;

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
    private boolean canAttack;

    public EntityAIRangedSpellAttack(EntityLivingBase entity, int attackCooldown, IMobData mobData) {
        this.entity = entity;
        this.attackCooldown = attackCooldown;
        this.mobData = mobData;
        this.setMutexBits(3);
        canAttack = false;
    }

    public void setAttackCooldown(int cooldownIn) {
        this.attackCooldown = cooldownIn;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute() {
        if (mobData.hasAbilities() && entity instanceof EntityLiving) {
            EntityLivingBase target = ((EntityLiving) entity).getAttackTarget();
            if (target != null){
                for (MobAbilityTracker tracker : mobData.getAbilityTrackers()) {
                    if (!tracker.isAbilityOnCooldown()) {
                        if (tracker.getAbility().getAbilityType() == BaseMobAbility.AbilityType.ATTACK &&
                                isInRange(target, tracker)) {
                            currentAbility = tracker;
                            return true;
                        }
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
        this.attackTime = attackCooldown;
        this.currentAbility = null;
        canAttack = false;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public double getMaxDistance() {
        double d = this.currentAbility.getAbility().getDistance();
        return d * d;
    }

    public boolean isInRange(Entity entity, MobAbilityTracker tracker){
        double d0 = this.entity.getDistanceSq(entity.posX, entity.getEntityBoundingBox().minY, entity.posZ);
        double d1 = tracker.getAbility().getDistance();
        return d0 <= d1 * d1;
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
                double maxAttackDistance = getMaxDistance();
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

                if (canAttack) {
                    if (!canSee && this.seeTime < -60) {
                        canAttack = false;
                    } else if (canSee) {
                        currentAbility.useAbility(entitylivingbase);
                        this.attackTime = this.attackCooldown;
                        canAttack = false;
                    }
                } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                    if (currentAbility.isEngageTimeGreaterThanCastTime(-1*this.attackTime)){
                        this.canAttack = true;
                    } else {
                        if (entity instanceof IRangedAttackMob){
                            ((IRangedAttackMob)this.entity).setSwingingArms(true);
                        }
                        Vec3d lookVec = entLiv.getLookVec();
                        if (this.attackTime % 2 == 0){
                            int particleId;
                            switch (currentAbility.getAbility().getAbilityType()){
                                case ATTACK:
                                    particleId = EnumParticleTypes.SPELL_WITCH.getParticleID();
                                    break;
                                case BUFF:
                                    particleId = EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID();
                                    break;
                                case HEAL:
                                    particleId = EnumParticleTypes.SPELL_INSTANT.getParticleID();
                                    break;
                                default:
                                    particleId = EnumParticleTypes.SPELL_WITCH.getParticleID();
                            }
                            MKUltra.packetHandler.sendToAllAround(
                                    new ParticleEffectSpawnPacket(
                                            particleId,
                                            ParticleEffects.CIRCLE_MOTION, 6, 3,
                                            entLiv.posX, entLiv.posY + 1.0,
                                            entLiv.posZ, 1.0, 1.0, 1.0, .5,
                                            lookVec),
                                    entity.dimension, entity.posX,
                                    entity.posY, entity.posZ, 25.0f);
                        }

                }

                }
            }
        }
    }
}

