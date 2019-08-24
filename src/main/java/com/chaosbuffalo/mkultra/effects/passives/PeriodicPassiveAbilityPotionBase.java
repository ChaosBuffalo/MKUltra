package com.chaosbuffalo.mkultra.effects.passives;


public abstract class PeriodicPassiveAbilityPotionBase extends PassiveAbilityPotionBase {
    private int period;

    protected PeriodicPassiveAbilityPotionBase(int period) {
        super();
        this.period = period;
    }

    @Override
    public boolean isReady(int duration, int amplitude) {
        return super.isReady(duration, amplitude) || duration % period == 0;
    }

    @Override
    public boolean isInstant() {
        return false;
    }

    public int getPeriod() {
        return period;
    }

}
