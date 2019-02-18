package com.chaosbuffalo.mkultra.core;

import net.minecraft.entity.EntityLivingBase;

public class MobAbilityTracker {

    private MobAbility ability;
    private int cooldownLeft;
    private IMobData mobData;

    public MobAbilityTracker(MobAbility abilityIn, IMobData mobData){
        cooldownLeft = 0;
        this.mobData = mobData;
        ability = abilityIn;
    }

    public void update(){
        if (cooldownLeft > 0){
            cooldownLeft--;
        }
    }

    public MobAbility getAbility(){
        return ability;
    }

    public boolean isAbilityOnCooldown(){
        return cooldownLeft > 0;
    }

    public boolean isCooldownGreaterThanAttackTime(int attackTime){
        if (attackTime < 0){
            return false;
        }

        return cooldownLeft > attackTime;
    }

    public boolean isEngageTimeGreaterThanCastTime(int engageTime){
        return engageTime >= ability.getCastTime();
    }

    public void useAbility(EntityLivingBase target){
        ability.execute(mobData.getEntity(), mobData, target, mobData.getEntity().getEntityWorld());
        cooldownLeft = ability.getCooldown();
    }
}
