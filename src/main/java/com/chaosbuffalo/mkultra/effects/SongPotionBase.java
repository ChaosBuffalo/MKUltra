package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;

import java.awt.geom.Area;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jacob on 4/21/2018.
 */

public abstract class SongPotionBase extends SpellPotionBase {

    private int period;
    private boolean isVisible;
    public boolean isHostSong;

    protected SongPotionBase(int period, boolean isHostSong, boolean isVisible, boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
        this.period = period;
        this.isVisible = isVisible;
        this.isHostSong = isHostSong;
    }

    public AreaEffectBuilder prepareAreaEffect(Entity source, int level, AreaEffectBuilder builder) {
        return builder;
    }

    public Set<PotionEffect> getPotionsToApply(Entity source, int level) { return new HashSet<>(); }

    public Set<SpellCast> getSpellCasts(Entity source) { return new HashSet<>(); }

    public abstract float getDistance(int level);

    public abstract ResourceLocation getAssociatedAbilityId();

    @Override
    public boolean isReady(int duration, int amplitude) {
        return super.isReady(duration, amplitude) && duration % period == 0;
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.SELF;
    }

    @Override
    public boolean canSelfCast() { return true; }

    public EnumParticleTypes getSongParticle() { return EnumParticleTypes.NOTE; }

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
    public void doEffect(Entity source, Entity indirectSource, EntityLivingBase target, int amplifier, SpellCast cast)
    {
        if (source instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) source;
            IPlayerData pData = MKUPlayerData.get(player);
            if (pData.getMana() > 1){
                pData.setMana(pData.getMana() - 1);
            } else {
                player.removePotionEffect(this);
            }
            if (!isHostSong){
                AreaEffectBuilder builder = AreaEffectBuilder.Create(player, player)
                        .instant()
                        .particle(getSongParticle())
                        .color(16762905).radius(getDistance(amplifier), true);
                prepareAreaEffect(player, amplifier, builder).spawn();

            } else {
                getPotionsToApply(player, amplifier).forEach(player::addPotionEffect);

                for (SpellCast toCast : getSpellCasts(player)) {
                    SpellCast.registerTarget(toCast, player);
                    player.addPotionEffect(toCast.toPotionEffect(period, amplifier));
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


    }

}
