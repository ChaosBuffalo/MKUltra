package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.potion.PotionEffect;

import javax.annotation.Nonnull;

public class PassiveAbilityPotionBase extends SpellPotionBase {
    static int PASSIVE_DURATION = 599;
    static int MAX_PASSIVE_AMPLIFIER = 32767;


    protected PassiveAbilityPotionBase() {
        super(false, 0);
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.SELF;
    }

    public void markForCleanup(EntityLivingBase target){
        PotionEffect currentEffect = target.getActivePotionEffect(this);
        if (currentEffect != null){
            currentEffect.combine(newSpellCast(target).toPotionEffect(0, MAX_PASSIVE_AMPLIFIER));
        }
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {

    }

    @Override
    public boolean shouldPotionRemove(EntityLivingBase target, @Nonnull AbstractAttributeMap attributes, int amplifier){
        PotionEffect currentEffect = target.getActivePotionEffect(this);
        if (currentEffect != null){
            if (currentEffect.getAmplifier() == MAX_PASSIVE_AMPLIFIER){
                return true;
            } else {
                target.addPotionEffect(newSpellCast(target).toPotionEffect(PASSIVE_DURATION,
                        currentEffect.getAmplifier()));
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isInstant() {
        return false;
    }
}
