package com.chaosbuffalo.mkultra.core.abilities.cast_states;

import net.minecraft.util.math.Vec3d;

public class Vec3dCastState extends CastState {

    private Vec3d location;

    public Vec3dCastState(int castTime) {
        super(castTime);
    }

    public boolean hasLocation(){
        return location != null;
    }

    public void setLocation(Vec3d in){
        location = in;
    }

    public Vec3d getLocation(){
        return location;
    }
}

