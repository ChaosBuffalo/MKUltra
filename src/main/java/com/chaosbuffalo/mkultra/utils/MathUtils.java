package com.chaosbuffalo.mkultra.utils;

public class MathUtils {

    public static final double lerp_double(double start, double stop, int increment, int max_increments) {
        double range = stop - start;
        double incrementVal = range / max_increments;
        return start + (incrementVal * increment);
    }


}
