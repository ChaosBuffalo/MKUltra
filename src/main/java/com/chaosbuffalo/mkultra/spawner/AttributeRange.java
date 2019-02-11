package com.chaosbuffalo.mkultra.spawner;

import net.minecraft.entity.EntityLivingBase;
import java.util.function.BiFunction;


public class AttributeRange extends MobOption {

    private final BiFunction<EntityLivingBase, AttributeRange, Boolean> applyFunc;
    public double start;
    public double stop;
    public int level;
    public int maxLevel;

    public AttributeRange(BiFunction<EntityLivingBase, AttributeRange, Boolean> applyFunc, double startIn, double stopIn) {
        this.applyFunc = applyFunc;
        start = startIn;
        stop = stopIn;
    }

    @Override
    public void apply(EntityLivingBase entity, int level, int maxLevel) {
        this.level = level;
        this.maxLevel = maxLevel;
        this.applyFunc.apply(entity, this);
    }
}
