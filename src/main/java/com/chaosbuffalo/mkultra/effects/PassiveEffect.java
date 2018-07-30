package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;

public abstract class PassiveEffect extends SpellPotionBase {
    protected PassiveEffect(boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

    }

    @Override
    public boolean canSelfCast() {
        return true;
    }

    @Override
    public boolean isReady(int duration, int amplitude) {
        return false;
    }

    @Override
    public boolean isInstant() {
        return false;
    }
}
