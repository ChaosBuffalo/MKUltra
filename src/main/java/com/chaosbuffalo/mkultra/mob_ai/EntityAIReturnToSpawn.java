package com.chaosbuffalo.mkultra.mob_ai;

import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.log.Log;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;

public class EntityAIReturnToSpawn extends EntityAIBase {
    private final EntityCreature creature;
    private final double movementSpeed;
    private final IMobData mobData;
    private static final double LEASH_RANGE = 40.0;
    private boolean doReturn;
    private int ticks_returning;

    public EntityAIReturnToSpawn(EntityCreature entity, IMobData mobData, double movementSpeed) {
        this.creature = entity;
        this.movementSpeed = movementSpeed;
        this.setMutexBits(1);
        this.mobData = mobData;
        this.ticks_returning = 0;
    }

    public boolean shouldExecute() {
        if (mobData.hasSpawnPoint()){
            double distFromSpawn = creature.getDistanceSq(mobData.getSpawnPoint());
            if (distFromSpawn <= 4.0){
                return false;
            }
            if (distFromSpawn > LEASH_RANGE * LEASH_RANGE || creature.getAttackTarget() == null){
                return true;
            }
        }
        return false;
    }

    public boolean shouldContinueExecuting() {
        Log.info("Should Continue: Return to Spawn %b", !this.creature.getNavigator().noPath());
        if (this.creature.getNavigator().getPath() == null){
            Log.info("navigator path null");
        } else {
            Log.info("is finished: %b",this.creature.getNavigator().getPath().isFinished());
        }
        return !this.creature.getNavigator().noPath();
    }

    public void startExecuting() {
        Log.info("Start Execute: Return to Spawn %s", creature.toString());
        BlockPos spawnPoint = mobData.getSpawnPoint();
        double distFromSpawn = creature.getDistanceSq(spawnPoint);
        IAttributeInstance followRange = creature.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        if (distFromSpawn >= followRange.getAttributeValue() * followRange.getAttributeValue()){
//            Log.info("Should teleport home.");
            creature.setPositionAndUpdate(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ());
        }
        this.creature.getNavigator().tryMoveToXYZ(spawnPoint.getX(), spawnPoint.getY(),
                spawnPoint.getZ(), this.movementSpeed);
        this.creature.setAttackTarget(null);
        this.creature.setRevengeTarget(null);

    }

}
