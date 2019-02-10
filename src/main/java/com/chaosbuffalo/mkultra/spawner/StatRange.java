package com.chaosbuffalo.mkultra.spawner;

import net.minecraft.entity.EntityLivingBase;
import java.util.function.BiFunction;


public class StatRange {

    private final BiFunction<EntityLivingBase, StatRange, Boolean> applyFunc;
    public double start;
    public double stop;

    public StatRange(BiFunction<EntityLivingBase, StatRange, Boolean> applyFunc, double startIn, double stopIn) {
        this.applyFunc = applyFunc;
        start = startIn;
        stop = stopIn;
    }

    public void applyRange(EntityLivingBase entity){
        this.applyFunc.apply(entity, this);
    }
}
