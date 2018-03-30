package com.chaosbuffalo.mkultra.effects;

/**
 * Created by Jacob on 3/31/2016.
 */

public abstract class SpellDOTPotionBase extends SpellPotionBase {

    private int period;

    protected SpellDOTPotionBase(int period, boolean isBadEffectIn, int liquidColorIn) {
        super(isBadEffectIn, liquidColorIn);
        this.period = period;
    }

    @Override
    public boolean isReady(int duration, int amplitude) {
        return super.isReady(duration, amplitude) && duration % period == 0;
    }

    @Override
    public boolean isInstant() {
        return false;
    }
}