package com.chaosbuffalo.mkultra.effects.songs;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;

import java.util.HashSet;
import java.util.Set;

public abstract class SongApplicator extends SongPotionBase {
    protected SongApplicator(int period, boolean isBadEffectIn, int liquidColorIn) {
        super(period, false, isBadEffectIn, liquidColorIn);
    }

    public Set<PotionEffect> getPotionsToApply(Entity source, int level) { return new HashSet<>(); }

    public Set<SpellCast> getSpellCasts(Entity source) { return new HashSet<>(); }

    @Override
    public void doEffect(Entity source, Entity indirectSource, EntityLivingBase target, int amplifier, SpellCast cast) {
        if (source instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) source;
            IPlayerData pData = MKUPlayerData.get(player);
            if (pData == null)
                return;
            PlayerAbility ability = MKURegistry.getAbility(PlayerToggleAbility.getToggleAbilityIdForPotion(this));
            if (ability == null)
                return;

            if (!pData.consumeMana(ability.getManaCost(amplifier))) {
                player.removePotionEffect(this);
            }

            getPotionsToApply(player, amplifier).forEach(player::addPotionEffect);

            for (SpellCast toCast : getSpellCasts(player)) {
                player.addPotionEffect(toCast.setTarget(player).toPotionEffect(getPeriod(), amplifier));
            }
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.NOTE.getParticleID(),
                            ParticleEffects.CIRCLE_MOTION, 12, 4,
                            target.posX, target.posY + 1.0f,
                            target.posZ, .25, .25, .25, .5,
                            target.getLookVec()),
                    target, 50.0f);

        }
    }
}
