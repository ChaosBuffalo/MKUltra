package com.chaosbuffalo.mkultra.mob_ai;

import com.chaosbuffalo.mkultra.core.IMobData;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAIReturnToSpawn extends EntityAIBase {
        private final EntityCreature creature;
        private final double movementSpeed;
        private final IMobData mobData;
        private static final double LEASH_RANGE = 30.0;

        public EntityAIReturnToSpawn(EntityCreature entity, IMobData mobData, double movementSpeed) {
            this.creature = entity;
            this.movementSpeed = movementSpeed;
            this.setMutexBits(1);
            this.mobData = mobData;
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
            return !this.creature.getNavigator().noPath();
        }

        public void startExecuting() {
            BlockPos spawnPoint = mobData.getSpawnPoint();
            this.creature.getNavigator().tryMoveToXYZ(spawnPoint.getX(), spawnPoint.getY(),
                    spawnPoint.getZ(), this.movementSpeed);
        }
}
