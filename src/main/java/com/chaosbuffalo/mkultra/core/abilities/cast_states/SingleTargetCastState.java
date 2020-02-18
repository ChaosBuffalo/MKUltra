package com.chaosbuffalo.mkultra.core.abilities.cast_states;

import net.minecraft.entity.EntityLivingBase;

import java.lang.ref.WeakReference;
import java.util.Optional;

public class SingleTargetCastState extends CastState {

    private WeakReference<EntityLivingBase> target;

    public SingleTargetCastState(int castTime) {
        super(castTime);
    }

    public Optional<EntityLivingBase> getTarget() {
        return Optional.ofNullable(target.get()).filter(EntityLivingBase::isEntityAlive);
    }

    public void setTarget(EntityLivingBase target) {
        this.target = new WeakReference<>(target);
    }
}
