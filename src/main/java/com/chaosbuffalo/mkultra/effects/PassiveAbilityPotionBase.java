package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.PotionEffect;


public class PassiveAbilityPotionBase extends SpellPotionBase {
    static final int CLEANUP_AMPLIFIER = 127;

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

    public void markForCleanup(EntityLivingBase target){
        if (!target.world.isRemote || !isServerSideOnly()) {
            if (target.isPotionActive(this)){
                SpellCast cast = SpellManager.get(target, this);
                PotionEffect effect = target.getActivePotionEffect(this);
                if (cast != null && effect != null){
                    target.addPotionEffect(cast.toPotionEffect(effect.getDuration(), CLEANUP_AMPLIFIER));
                }
            }
        }
    }

    @Override
    public boolean shouldPotionRemove(EntityLivingBase target, int amplifier) {
        if (amplifier < CLEANUP_AMPLIFIER){
            return false;
        }
        return super.shouldPotionRemove(target, amplifier);
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    @Override
    public boolean canSelfCast() {
        return true;
    }

    @Override
    public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier) {
        return modifier.getAmount() * (double) (amplifier);
    }
}
