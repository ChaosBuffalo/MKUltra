package com.chaosbuffalo.mkultra.mob_ai;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.chaosbuffalo.mkultra.core.MobAbilityTracker;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class EntityAISpellCastingBase extends EntityAIBase {
    protected EntityLivingBase entity;
    protected IMobData mobData;
    int cooldown;
    int castTime;
    boolean canCast;
    private final int CAST_TIME_MAX = 15 * GameConstants.TICKS_PER_SECOND;
    EntityLivingBase targetEntity;
    int seeTime;
    boolean strafingClockwise;
    boolean strafingBackwards;
    int strafingTime = -1;
    MobAbilityTracker currentAbility;
    public float strafeRangeStart;
    public float strafeRangeEnd;
    public Targeting.TargetType desiredTargetType;
    boolean doStrafe;
    int attemptingCastTicks;

    public EntityAISpellCastingBase(EntityLivingBase entity, IMobData mobData, int cooldown) {
        this(entity, mobData, cooldown, .25f, .75f);
    }

    public EntityAISpellCastingBase(EntityLivingBase entity, IMobData mobData, int cooldown,
                                    float strafeRangeStart, float strafeRangeEnd) {
        this.mobData = mobData;
        this.entity = entity;
        this.cooldown = cooldown;
        this.strafeRangeStart = strafeRangeStart;
        this.strafeRangeEnd = strafeRangeEnd;
        this.doStrafe = true;
        setMutexBits(4);
        this.attemptingCastTicks = 0;
    }

    public EntityAISpellCastingBase setStrafeRange(float rangeStart, float rangeEnd) {
        strafeRangeStart = rangeStart;
        strafeRangeEnd = rangeEnd;
        return this;
    }

    public EntityAISpellCastingBase setStrafe(boolean doStrafe) {
        this.doStrafe = doStrafe;
        return this;
    }

    public boolean isInRange(Entity entity, MobAbilityTracker tracker) {
        double d0 = this.entity.getDistanceSq(entity.posX, entity.getEntityBoundingBox().minY, entity.posZ);
        double d1 = tracker.getAbility().getDistance();
        return d0 <= d1 * d1;
    }

    @Override
    public boolean shouldExecute() {
        return false;
    }

    protected AxisAlignedBB getTargetableArea(double distance) {
        return entity.getEntityBoundingBox().grow(distance, distance, distance);
    }

    public int compareDistance(Entity a, Entity b) {
        double aDist = entity.getDistanceSq(a);
        double bDist = entity.getDistanceSq(b);
        if (aDist < bDist) {
            return -1;
        } else {
            return aDist > bDist ? 1 : 0;
        }
    }

    public int compareHealth(Entity a, Entity b) {
        float aVal = entity.getHealth();
        float bVal = entity.getHealth();
        if (aVal < bVal) {
            return -1;
        } else {
            return aVal > bVal ? 1 : 0;
        }
    }

    List<Entity> getEntitiesInRange(double distance, boolean excludeCaster, Targeting.TargetType type) {
        List<Entity> list = entity.world.getEntitiesWithinAABB(EntityLivingBase.class,
                getTargetableArea(distance),
                e -> Targeting.isValidTarget(type, entity, e, excludeCaster));
        return list;
    }

    public void doStrafeBehavior(EntityLiving entLiv, boolean canSee, double d0, double maxDistance) {
        if (canSee) {
            ++this.seeTime;
        } else {
            --this.seeTime;
        }
        if (d0 <= maxDistance && this.seeTime >= 20) {
            entLiv.getNavigator().clearPath();
            ++this.strafingTime;
        } else {
            entLiv.getNavigator().tryMoveToEntityLiving(targetEntity, 1.0);
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
            if (d0 > maxDistance * strafeRangeEnd) {
                this.strafingBackwards = false;
            } else if (d0 < maxDistance * strafeRangeStart) {
                this.strafingBackwards = true;
            }
            entLiv.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
            entLiv.faceEntity(targetEntity, 30.0F, 30.0F);
        } else {
            entLiv.getLookHelper().setLookPositionWithEntity(targetEntity,
                    30.0F, 30.0F);
        }
    }

    public double getMaxDistance() {
        double d = this.currentAbility.getAbility().getDistance();
        return d * d;
    }


    @Override
    public void startExecuting() {
        super.startExecuting();
        if (entity instanceof IRangedAttackMob) {
            ((IRangedAttackMob) this.entity).setSwingingArms(true);
        }
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.castTime = cooldown;
        this.currentAbility = null;
        targetEntity = null;
        attemptingCastTicks = 0;
        canCast = false;
        if (entity instanceof IRangedAttackMob) {
            ((IRangedAttackMob) this.entity).setSwingingArms(false);
        }
        endMobCastingState();
    }

    public void startMobCastingState(ResourceLocation casting){
        mobData.setCastingAbility(casting);
        mobData.setCastTicks(0);
    }

    public void endMobCastingState(){
        mobData.setCastingAbility(MKURegistry.INVALID_MOB_ABILITY);
        mobData.setCastTicks(0);
    }

    @Override
    public boolean shouldContinueExecuting() {
        if (targetEntity == null) {
            return false;
        }
        if (!(entity instanceof EntityLiving)) {
            return false;
        }
        if (targetEntity.isDead) {
            return false;
        }
        if (!Targeting.isValidTarget(desiredTargetType, entity, targetEntity, false)) {
            return false;
        }
        if (attemptingCastTicks >= CAST_TIME_MAX) {
            return false;
        }
        return (currentAbility != null && !currentAbility.isAbilityOnCooldown() || !((EntityLiving) entity).getNavigator().noPath());
    }


    @Override
    public void updateTask() {
//        Log.info("In update task spell casting %s", entity.toString());
        if (entity instanceof EntityLiving) {
            EntityLiving entLiv = (EntityLiving) entity;
            if (targetEntity != null && currentAbility != null) {
//                Log.info("Target is %s, ability: %s, cooldown is: %d", targetEntity.toString(), currentAbility.getAbility().getAbilityId().toString(), currentAbility.getCooldown());
                double d0 = this.entity.getDistanceSq(targetEntity.posX,
                        targetEntity.getEntityBoundingBox().minY, targetEntity.posZ);
                boolean canSee = entLiv.getEntitySenses().canSee(targetEntity);
                boolean hasSeen = this.seeTime > 0;
                double maxDistance = getMaxDistance();
                if (canSee != hasSeen) {
                    this.seeTime = 0;
                }
                if (doStrafe) {
                    doStrafeBehavior(entLiv, canSee, d0, maxDistance);
                }
                if (canCast) {
                    attemptingCastTicks++;
                    mobData.setCastTicks(attemptingCastTicks);
                    if (!canSee && this.seeTime < -60) {
                        canCast = false;
                    } else if (canSee) {
                        currentAbility.useAbility(targetEntity);
                        castTime = cooldown;
                        canCast = false;
                        attemptingCastTicks = 0;
                        endMobCastingState();
                        if (entity instanceof IRangedAttackMob) {
                            ((IRangedAttackMob) this.entity).setSwingingArms(true);
                        }
                        currentAbility = null;
                    }
                } else if (--this.castTime <= 0 && this.seeTime >= -60) {
                    if (currentAbility.isEngageTimeGreaterThanCastTime(-1 * castTime)) {
                        canCast = true;
                        if (entity instanceof IRangedAttackMob) {
                            ((IRangedAttackMob) this.entity).setSwingingArms(true);
                        }
                    } else {
                        Vec3d lookVec = entLiv.getLookVec();

                        int particleId;
                        switch (currentAbility.getAbility().getAbilityType()) {
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
