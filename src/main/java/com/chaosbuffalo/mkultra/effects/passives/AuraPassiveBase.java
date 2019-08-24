package com.chaosbuffalo.mkultra.effects.passives;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

public abstract class AuraPassiveBase extends PeriodicPassiveAbilityPotionBase {
    protected AuraPassiveBase(int period) {
        super(period);
    }

    public AreaEffectBuilder prepareAreaEffect(EntityPlayer source, IPlayerData playerData, int level, AreaEffectBuilder builder) {
        return builder;
    }

    public abstract float getAuraDistance(int level);


    @Override
    public void doEffect(Entity source, Entity indirectSource, EntityLivingBase target, int amplifier, SpellCast cast) {
        if (source instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) source;
            IPlayerData pData = MKUPlayerData.get(player);
            if (pData == null)
                return;
            AreaEffectBuilder builder = AreaEffectBuilder.Create(player, player)
                    .instant()
                    .radius(getAuraDistance(amplifier), true);
            prepareAreaEffect(player, pData, amplifier, builder).spawn();
        }
    }
}
