package com.chaosbuffalo.mkultra.mob_ai;

import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MobAbilityTracker;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIBuffSelf extends EntityAIBase {
    protected IMobData mobData;
    protected EntityLivingBase entity;
    private float healThreshold;


    public EntityAIBuffSelf(EntityLivingBase entity, IMobData mobData, float healThreshold){
        this.mobData = mobData;
        this.entity = entity;
        this.healThreshold = healThreshold;
        setMutexBits(4);

    }

    private boolean isSelfBuff(MobAbilityTracker tracker){
        return tracker.getAbility().canSelfCast() &&
                tracker.getAbility().getAbilityType() == MobAbility.AbilityType.BUFF;
    }

    private boolean isSelfHeal(MobAbilityTracker tracker){
        return tracker.getAbility().canSelfCast() &&
                tracker.getAbility().getAbilityType() == MobAbility.AbilityType.HEAL;

    }

    @Override
    public boolean shouldExecute() {
        if (mobData.hasAbilities()){
            for (MobAbilityTracker tracker : mobData.getAbilityTrackers()){
                if (!tracker.isAbilityOnCooldown()){
                    if (isSelfBuff(tracker)){
                        return true;
                    } else if (isSelfHeal(tracker) && entity.getHealth() <= entity.getMaxHealth() * healThreshold){
                        return true;
                    }
                }

            }
        }
        return false;
    }

    @Override
    public void startExecuting(){
        if (mobData.hasAbilities()){
            for (MobAbilityTracker tracker : mobData.getAbilityTrackers()){
                if (!tracker.isAbilityOnCooldown() && (isSelfBuff(tracker) || isSelfHeal(tracker))){
                    tracker.useAbility(entity);
                }
            }
        }
    }
}
