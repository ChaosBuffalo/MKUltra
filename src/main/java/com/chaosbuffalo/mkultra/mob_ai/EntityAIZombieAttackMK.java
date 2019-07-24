package com.chaosbuffalo.mkultra.mob_ai;

import net.minecraft.entity.monster.EntityZombie;

public class EntityAIZombieAttackMK extends EntityAIAttackMeleeMK {
    private final EntityZombie zombie;
    private int raiseArmTicks;

    public EntityAIZombieAttackMK(EntityZombie entityIn, double moveSpeed, boolean longMemory) {
        super(entityIn, moveSpeed, longMemory);
        this.zombie = entityIn;
    }

    public void startExecuting() {
        super.startExecuting();
        this.raiseArmTicks = 0;
    }

    public void resetTask() {
        super.resetTask();
        this.zombie.setArmsRaised(false);
    }

    public void updateTask() {
        super.updateTask();
        ++this.raiseArmTicks;
        if (this.raiseArmTicks >= 5 && this.attackTick < 10) {
            this.zombie.setArmsRaised(true);
        } else {
            this.zombie.setArmsRaised(false);
        }

    }
}