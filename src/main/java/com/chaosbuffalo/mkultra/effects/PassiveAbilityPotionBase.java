package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;


public class PassiveAbilityPotionBase extends SpellPotionBase {
    static int PASSIVE_DURATION = 620;

    protected PassiveAbilityPotionBase() {
        super(false, 0);
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.SELF;
    }


    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

    }

    @Override
    public void onPotionAdd(SpellCast cast, EntityLivingBase target, AbstractAttributeMap attributes, int amplifier) {
        target.addPotionEffect(cast.toPotionEffect(PASSIVE_DURATION, amplifier));
        super.onPotionAdd(cast, target, attributes, amplifier);

    }

    @Override
    public boolean isInstant() {
        return false;
    }
}
