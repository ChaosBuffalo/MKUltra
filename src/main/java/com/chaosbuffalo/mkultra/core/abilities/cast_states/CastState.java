package com.chaosbuffalo.mkultra.core.abilities.cast_states;

import net.minecraft.util.ResourceLocation;

public class CastState {
    private int castTime;

    public CastState(int castTime){
        this.castTime = castTime;
    }

    public int getCastTime(){
        return castTime;
    }
}
