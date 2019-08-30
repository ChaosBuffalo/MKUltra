package com.chaosbuffalo.mkultra.mob_ai;

import com.chaosbuffalo.mkultra.entities.mobs.EntityMobBase;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.item.ItemBow;
import net.minecraft.util.EnumHand;

public class EntityAIAttackRangedBowMK<T extends EntityMobBase & IRangedAttackMob> extends EntityAIBase {
    private final T entity;
    private final double moveSpeedAmp;
    private int attackCooldown;
    private final float maxAttackDistance;
    private int attackTime = -1;
    private int seeTime;
    private boolean strafingClockwise;
    private boolean strafingBackwards;
    private int strafingTime = -1;

    public EntityAIAttackRangedBowMK(T entity, double moveSpeed, int cooldown, float maxDistance) {
        this.entity = entity;
        this.moveSpeedAmp = moveSpeed;
        this.attackCooldown = cooldown;
        this.maxAttackDistance = maxDistance * maxDistance;
        this.setMutexBits(3);
    }

    public void setAttackCooldown(int cooldown) {
        this.attackCooldown = cooldown;
    }

    public boolean shouldExecute() {
        return this.entity.getAttackTarget() != null && this.isBowInMainhand();
    }

    protected boolean isBowInMainhand() {
        return !this.entity.getHeldItemMainhand().isEmpty() &&
                this.entity.getHeldItemMainhand().getItem() instanceof ItemBow;
    }

    public boolean shouldContinueExecuting() {
        return (this.shouldExecute() || !this.entity.getNavigator().noPath()) && this.isBowInMainhand();
    }

    public void startExecuting() {
        super.startExecuting();
        this.entity.setSwingingArms(true);
    }

    public void resetTask() {
        super.resetTask();
        this.entity.setSwingingArms(false);
        this.seeTime = 0;
        this.attackTime = -1;
        this.entity.resetActiveHand();
    }

    public void updateTask() {
        EntityLivingBase target = this.entity.getAttackTarget();
        if (target != null) {
            double dist2 = this.entity.getDistanceSq(target.posX, target.getEntityBoundingBox().minY, target.posZ);
            boolean canSee = this.entity.getEntitySenses().canSee(target);
            boolean seeTime = this.seeTime > 0;
            if (canSee != seeTime) {
                this.seeTime = 0;
            }

            if (canSee) {
                ++this.seeTime;
            } else {
                --this.seeTime;
            }

            if (dist2 <= (double) this.maxAttackDistance && this.seeTime >= 20) {
                this.entity.getNavigator().clearPath();
                ++this.strafingTime;
            } else {
                this.entity.getNavigator().tryMoveToEntityLiving(target, this.moveSpeedAmp);
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
                if (dist2 > (double) (this.maxAttackDistance * 0.75F)) {
                    this.strafingBackwards = false;
                } else if (dist2 < (double) (this.maxAttackDistance * 0.25F)) {
                    this.strafingBackwards = true;
                }

                this.entity.getMoveHelper().strafe(this.strafingBackwards ? -0.5F : 0.5F, this.strafingClockwise ? 0.5F : -0.5F);
                this.entity.faceEntity(target, 30.0F, 30.0F);
            } else {
                this.entity.getLookHelper().setLookPositionWithEntity(target, 30.0F, 30.0F);
            }

            if (this.entity.isHandActive()) {
                if (!canSee && this.seeTime < -60) {
                    this.entity.resetActiveHand();
                } else if (canSee) {
                    int maxCount = this.entity.getItemInUseMaxCount();
                    if (maxCount >= 20) {
                        this.entity.resetActiveHand();
                        this.entity.attackEntityWithRangedAttack(target, ItemBow.getArrowVelocity(maxCount));
                        this.attackTime = this.attackCooldown;
                    }
                }
            } else if (--this.attackTime <= 0 && this.seeTime >= -60) {
                this.entity.setActiveHand(EnumHand.MAIN_HAND);
            }

        }
    }
}
