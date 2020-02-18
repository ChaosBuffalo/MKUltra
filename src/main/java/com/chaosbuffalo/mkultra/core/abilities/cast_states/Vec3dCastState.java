package com.chaosbuffalo.mkultra.core.abilities.cast_states;

import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class Vec3dCastState extends CastState {

    private Vec3d location;

    public Vec3dCastState(int castTime) {
        super(castTime);
    }

    public void setLocation(Vec3d in){
        location = in;
    }

    public Optional<Vec3d> getLocation() {
        return Optional.ofNullable(location);
    }
}

