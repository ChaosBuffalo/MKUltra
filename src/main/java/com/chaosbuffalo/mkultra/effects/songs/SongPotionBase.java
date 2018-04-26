package com.chaosbuffalo.mkultra.effects.songs;

import com.chaosbuffalo.mkultra.effects.SpellPeriodicPotionBase;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Jacob on 4/21/2018.
 */

public abstract class SongPotionBase extends SpellPeriodicPotionBase {

    private boolean isVisible;

    protected SongPotionBase(int period, boolean isVisible, boolean isBadEffectIn, int liquidColorIn) {
        super(period, isBadEffectIn, liquidColorIn);
        this.isVisible = isVisible;
    }

    public abstract ResourceLocation getAssociatedAbilityId();

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

}
