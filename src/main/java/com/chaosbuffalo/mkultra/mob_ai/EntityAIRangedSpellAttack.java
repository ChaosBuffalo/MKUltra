package com.chaosbuffalo.mkultra.mob_ai;

import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MobAbilityTracker;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;

public class EntityAIRangedSpellAttack extends EntityAISpellCastingBase {

    public EntityAIRangedSpellAttack(EntityLivingBase entity, IMobData mobData, int cooldown) {
        super(entity, mobData, cooldown);
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
                        if (tracker.getAbility().getAbilityType() == MobAbility.AbilityType.ATTACK &&
                                isInRange(target, tracker)) {
                            currentAbility = tracker;
                            targetEntity = target;
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
}

