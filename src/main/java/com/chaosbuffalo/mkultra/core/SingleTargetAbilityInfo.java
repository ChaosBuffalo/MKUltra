package com.chaosbuffalo.mkultra.core;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;

public class SingleTargetAbilityInfo extends PlayerAbilityInfo {

    private EntityLivingBase target;

    public SingleTargetAbilityInfo(ResourceLocation abilityId) {
        super(abilityId);
    }

    public SingleTargetAbilityInfo(ResourceLocation abilityId, int rank) {
        super(abilityId, rank);
    }

    @Nullable
    public EntityLivingBase getTarget(){
        return target;
    }

    public void setTarget(EntityLivingBase target){
        this.target = target;
    }
}
