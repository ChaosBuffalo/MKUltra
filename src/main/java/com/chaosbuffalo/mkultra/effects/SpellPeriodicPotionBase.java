package com.chaosbuffalo.mkultra.effects;


public abstract class SpellPeriodicPotionBase extends SpellPotionBase {

    private int period;

    protected SpellPeriodicPotionBase(int period, boolean isBadEffectIn, int liquidColorIn) {
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