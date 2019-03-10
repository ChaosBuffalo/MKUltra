package com.chaosbuffalo.mkultra.effects.songs;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;

public abstract class SongEffect extends SongPotionBase {
    protected SongEffect(int period, boolean isBadEffectIn, int liquidColorIn) {
        super(period, true, isBadEffectIn, liquidColorIn);
    }

    public AreaEffectBuilder prepareAreaEffect(Entity source, int level, AreaEffectBuilder builder) {
        return builder;
    }

    public abstract float getSongDistance(int level);

    public EnumParticleTypes getSongParticle() { return EnumParticleTypes.NOTE; }

    @Override
    public void doEffect(Entity source, Entity indirectSource, EntityLivingBase target, int amplifier, SpellCast cast) {
        if (source instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) source;
            IPlayerData pData = MKUPlayerData.get(player);
            if (pData == null)
                return;

            if (!pData.consumeMana(amplifier)) {
                player.removePotionEffect(this);
            }

            AreaEffectBuilder builder = AreaEffectBuilder.Create(player, player)
                    .instant()
                    .particle(getSongParticle())
                    .color(16762905)
                    .radius(getSongDistance(amplifier), true);
            prepareAreaEffect(player, amplifier, builder).spawn();
        }
    }
}
