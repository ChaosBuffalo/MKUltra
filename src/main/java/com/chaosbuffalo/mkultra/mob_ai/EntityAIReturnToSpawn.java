package com.chaosbuffalo.mkultra.mob_ai;

import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.log.Log;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.util.math.BlockPos;

public class EntityAIReturnToSpawn extends EntityAIBase {
    private final EntityCreature creature;
    private final double movementSpeed;
    private final IMobData mobData;
    private static final double LEASH_RANGE = 50.0;
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
            if (distFromSpawn > LEASH_RANGE * LEASH_RANGE || creature.getAttackTarget() == null){
                return true;
            }
        }
        return false;
    }

    public boolean shouldContinueExecuting() {
        Log.debug("Should Continue: Return to Spawn %b", !this.creature.getNavigator().noPath());
        return !this.creature.getNavigator().noPath() ;
    }

    public void startExecuting() {
        Log.debug("Start Execute: Return to Spawn");
        BlockPos spawnPoint = mobData.getSpawnPoint();
        this.creature.getNavigator().tryMoveToXYZ(spawnPoint.getX(), spawnPoint.getY(),
                spawnPoint.getZ(), this.movementSpeed);
        this.creature.setAttackTarget(null);
    }

}
