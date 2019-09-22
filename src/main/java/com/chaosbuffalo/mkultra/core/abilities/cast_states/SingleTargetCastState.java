package com.chaosbuffalo.mkultra.core.abilities.cast_states;

import net.minecraft.entity.EntityLivingBase;

import javax.annotation.Nullable;

public class SingleTargetCastState extends CastState {

    private EntityLivingBase target;

    public SingleTargetCastState(int castTime){
        super(castTime);
    }

    public boolean hasTarget(){
        return getTarget() != null && getTarget().isEntityAlive();
    }

    @Nullable
    public EntityLivingBase getTarget(){
        return target;
    }

    public void setTarget(EntityLivingBase target){
        this.target = target;
    }
}