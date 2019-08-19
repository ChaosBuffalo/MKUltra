package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.PotionEffect;

import javax.annotation.Nonnull;


public class PassiveAbilityPotionBase extends SpellPotionBase {

    // This is arbitrary and can be much higher
    private static int PASSIVE_DURATION = 2000;
    // This should stay above 600 so the client is refreshed before hitting 0
    private static int REFRESH_DURATION = 610;

    protected PassiveAbilityPotionBase() {
        super(false, 0);
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.SELF;
    }

    @Override
    public void performEffect(@Nonnull EntityLivingBase target, int amplifier) {
        if (!target.world.isRemote)
            refreshEffect(target);
    }

    @Override
    public void affectEntity(Entity applier, Entity caster, @Nonnull EntityLivingBase target, int amplifier, double health) {
        if (!target.world.isRemote)
            refreshEffect(target);
    }

    @Override
    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {
        refreshEffect(target);
    }

    @Override
    public boolean isReady(int duration, int amplitude) {
        // Don't do anything until it's time to refresh
        return duration <= REFRESH_DURATION;
    }

    private void refreshEffect(EntityLivingBase target) {

//        Log.debug("passive refresh %s %s", target.toString(), this.toString());
        PotionEffect effect = target.getActivePotionEffect(this);
//        if (effect != null)
//            Log.debug("effect %s", effect.toString());
        if (effect != null && effect.getDuration() <= REFRESH_DURATION) {
            // Create a duplicate effect, and then combine them to extend the original
            PotionEffect extend = new PotionEffect(this, PASSIVE_DURATION, effect.getAmplifier(), effect.getIsAmbient(), effect.doesShowParticles());
            extend.setCurativeItems(effect.getCurativeItems());
            effect.combine(extend);
        }
    }

    public PotionEffect createInstance(EntityLivingBase caster) {
        return newSpellCast(caster).setTarget(caster).toPotionEffect(PASSIVE_DURATION, 1);
    }

    @Override
    public boolean canSelfCast() {
        return true;
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier) {
        return modifier.getAmount() * (double) (amplifier);
    }
}
