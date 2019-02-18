package com.chaosbuffalo.mkultra.mob_ai;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MobAbility.AbilityType;
import com.chaosbuffalo.mkultra.core.MobAbilityTracker;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import java.util.Collections;
import java.util.List;

import static com.chaosbuffalo.mkultra.core.MobAbility.AbilityType.HEAL;

public class EntityAIBuffTeammates extends EntityAIBase {
    protected IMobData mobData;
    protected EntityLivingBase entity;
    private float healThreshold;
    private int castTime;
    private int cooldown;
    private MobAbilityTracker currentAbility;
    private double MAX_BUFF_RANGE = 20.0;
    private boolean canCast;
    private EntityLivingBase targetEntity;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;


    public EntityAIBuffTeammates(EntityLivingBase entity, IMobData mobData, float healThreshold, int cooldown){
        this.mobData = mobData;
        this.entity = entity;
        this.healThreshold = healThreshold;
        this.cooldown = cooldown;
        setMutexBits(4);

    }

    private boolean isSelfBuff(MobAbilityTracker tracker){
        return tracker.getAbility().canSelfCast() &&
                tracker.getAbility().getAbilityType() == AbilityType.BUFF;
    }

    private boolean isSelfHeal(MobAbilityTracker tracker){
        return tracker.getAbility().canSelfCast() &&
                tracker.getAbility().getAbilityType() == HEAL;

    }

    public void resetTask() {
        super.resetTask();
        if (entity instanceof IRangedAttackMob){
            ((IRangedAttackMob)this.entity).setSwingingArms(true);
        }
        this.castTime = cooldown;
        this.currentAbility = null;
        canCast = false;
    }

    protected AxisAlignedBB getTargetableArea(double distance) {
        return entity.getEntityBoundingBox().grow(distance, distance, distance);
    }

    public int compareDistance(Entity a, Entity b){
        double aDist = entity.getDistanceSq(a);
        double bDist = entity.getDistanceSq(b);
        if (aDist < bDist) {
            return -1;
        } else {
            return aDist > bDist ? 1 : 0;
        }
    }

    public int compareHealth(Entity a, Entity b){
        float aVal = entity.getHealth();
        float bVal = entity.getHealth();
        if (aVal < bVal) {
            return -1;
        } else {
            return aVal > bVal ? 1 : 0;
        }
    }


    private List<Entity> getEntitiesInRange(double distance, boolean excludeCaster){
        List<Entity> list = entity.world.getEntitiesWithinAABB(EntityLivingBase.class,
                getTargetableArea(distance),
                e -> Targeting.isValidTarget(Targeting.TargetType.FRIENDLY, e, entity, excludeCaster));
        return list;
    }

    public boolean shouldContinueExecuting() {
        if (!(entity instanceof EntityLiving) || targetEntity == null){
            return false;
        }
        return (currentAbility != null && !currentAbility.isAbilityOnCooldown() || !((EntityLiving)entity).getNavigator().noPath());
    }

    @Override
    public boolean shouldExecute() {

        if (mobData.hasAbilities()){
            List<Entity> entities = getEntitiesInRange(MAX_BUFF_RANGE, false);
            if (entities.size() <= 0){
                return false;
            }
            for (MobAbilityTracker tracker : mobData.getAbilityTrackers()){
                if (!tracker.isAbilityOnCooldown()){
                    switch (tracker.getAbility().getAbilityType()){
                        case HEAL:
                            Entity min = Collections.min(entities, this::compareHealth);
                            if (min instanceof EntityLivingBase){
                                EntityLivingBase minLiv = (EntityLivingBase) min;
                                if (minLiv.getHealth() <= minLiv.getMaxHealth() * healThreshold){
                                    currentAbility = tracker;
                                    targetEntity = minLiv;
                                    return true;
                                }
                            }
                            break;
                        case BUFF:
                            min = Collections.min(entities, this::compareDistance);
                            if (min instanceof EntityLivingBase){
                                EntityLivingBase minLiv = (EntityLivingBase) min;
                                if (tracker.getAbility().getEffectPotion() != null){
                                    if (!minLiv.isPotionActive(tracker.getAbility().getEffectPotion())){
                                        currentAbility = tracker;
                                        targetEntity = minLiv;
                                        return true;
                                    }
                                }
                            }
                            break;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void startExecuting(){
        super.startExecuting();
        if (entity instanceof IRangedAttackMob){
            ((IRangedAttackMob)this.entity).setSwingingArms(true);
        }
    }

    public double getMaxDistance() {
        double d = this.currentAbility.getAbility().getDistance();
        return d * d;
    }

    @Override
    public void updateTask() {
        if (entity instanceof EntityLiving){
            EntityLiving entLiv = (EntityLiving)entity;
            if (targetEntity != null) {
                double d0 = this.entity.getDistanceSq(targetEntity.posX, targetEntity.getEntityBoundingBox().minY, targetEntity.posZ);
                boolean canSee = entLiv.getEntitySenses().canSee(targetEntity);
                boolean hasSeen = this.seeTime > 0;
                double maxDistance = getMaxDistance();
                if (canSee != hasSeen) {
                    this.seeTime = 0;
                }

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
                    if (d0 > maxDistance * 0.5F) {
                        this.strafingBackwards = false;
                    } else if (d0 < maxDistance * 0.1F) {
                        this.strafingBackwards = true;
                    }
                    entLiv.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                    entLiv.faceEntity(targetEntity, 30.0F, 30.0F);
                } else {
                    entLiv.getLookHelper().setLookPositionWithEntity(targetEntity, 30.0F, 30.0F);
                }

                if (canCast) {
                    if (!canSee && this.seeTime < -60) {
                        canCast = false;
                    } else if (canSee) {
                        currentAbility.useAbility(targetEntity);
                        castTime = cooldown;
                        canCast = false;
                    }
                } else if (--this.castTime <= 0 && this.seeTime >= -60) {
                    if (currentAbility.isEngageTimeGreaterThanCastTime(-1*castTime)){
                        canCast = true;
                    } else {
                        if (entity instanceof IRangedAttackMob){
                            ((IRangedAttackMob)this.entity).setSwingingArms(true);
                        }
                        Vec3d lookVec = entLiv.getLookVec();
                        if (this.castTime % 2 == 0){
                            int particleId;
                            switch (currentAbility.getAbility().getAbilityType()){
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
