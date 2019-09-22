package com.chaosbuffalo.mkultra.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class MathUtils {

    public static double lerp_double(double start, double stop, int increment, int max_increments) {
        double range = stop - start;
        double incrementVal = range / max_increments;
        return start + (incrementVal * increment);
    }

    public static boolean isBehind(Entity target, Entity source){
        Vec3d facing = target.getPositionVector().subtract(source.getPositionVector()).normalize();
        return facing.dotProduct(target.getForward()) > 0;
    }
}
