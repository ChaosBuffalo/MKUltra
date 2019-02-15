package com.chaosbuffalo.mkultra.core;

import net.minecraft.entity.EntityLivingBase;

public class MobAbilityTracker {

    private BaseMobAbility ability;
    private int cooldownLeft;
    private IMobData mobData;

    public MobAbilityTracker(BaseMobAbility abilityIn, IMobData mobData){
        cooldownLeft = 0;
        this.mobData = mobData;
        ability = abilityIn;
    }

    public void update(){
        if (cooldownLeft > 0){
            cooldownLeft--;
        }
    }

    public BaseMobAbility getAbility(){
        return ability;
    }

    public boolean isAbilityOnCooldown(){
        return cooldownLeft > 0;
    }

    public void useAbility(EntityLivingBase target){
        ability.execute(mobData.getEntity(), mobData, target, mobData.getEntity().getEntityWorld());
        cooldownLeft = ability.getCooldown(mobData.getMobLevel());
    }
}
