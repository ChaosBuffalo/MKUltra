package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jacob on 4/21/2018.
 */

public abstract class SongPotionBase extends SpellPotionBase {

    private int period;
    private boolean isVisible;
    private boolean isSelfOnly;

    protected SongPotionBase(int period, boolean isSelfOnly, boolean isVisible, boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
        this.period = period;
        this.isVisible = isVisible;
        this.isSelfOnly = isSelfOnly;
    }

    public Set<Potion> getPotionsToApply(Entity source) { return new HashSet<Potion>(); }

    public Set<SpellCast> getSpellCasts(Entity source) { return new HashSet<SpellCast>(); }

    public abstract float getDistance(int level);

    @Override
    public boolean isReady(int duration, int amplitude) {
        return super.isReady(duration, amplitude) && duration % period == 0;
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    @Override
    public boolean canSelfCast() { return true; }

    @Override
    public boolean shouldRenderHUD(PotionEffect effect)
    {
        return isVisible;
    }

    @Override
    public boolean shouldRenderInvText(PotionEffect effect)
    {
        return isVisible;
    }

    @Override
    public boolean shouldRender(PotionEffect effect) { return isVisible; }

    @Override
    public void doEffect(Entity source, Entity indirectSource, EntityLivingBase target, int amplifier, SpellCast cast) {

        if (!isSelfOnly){
            AreaEffectBuilder builder = AreaEffectBuilder.Create(target, target)
                    .instant()
                    .particle(EnumParticleTypes.NOTE)
                    .color(16762905).radius(getDistance(amplifier), true);

            for (Potion effect : getPotionsToApply(target)) {
                builder.effect(new PotionEffect(effect, period, amplifier), getTargetType());
            }
            for (SpellCast toCast : getSpellCasts(target)){
                builder.spellCast(toCast, amplifier, getTargetType());
            }
            builder.spawn();
        } else {
            for (Potion effect : getPotionsToApply(target)) {
                PotionEffect newEffect = new PotionEffect(effect, period, amplifier);
                target.addPotionEffect(newEffect);
            }
            for (SpellCast toCast : getSpellCasts(target)){
                SpellCast.registerTarget(toCast, target);
                target.addPotionEffect(toCast.toPotionEffect(period, amplifier));
            }
        }


        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.NOTE.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 12, 4,
                        target.posX, target.posY + 1.0f,
                        target.posZ, .25, .25, .25, .5,
                        target.getLookVec()),
                target.dimension, target.posX,
                target.posY, target.posZ, 50.0f);
    }
}
