package com.chaosbuffalo.mkultra.spawner;

import net.minecraft.entity.EntityLivingBase;
import java.util.function.BiFunction;


public class AttributeRange extends MobOption {

    private final BiFunction<EntityLivingBase, AttributeRange, Boolean> applyFunc;
    public double start;
    public double stop;


    public AttributeRange(BiFunction<EntityLivingBase, AttributeRange, Boolean> applyFunc, double start, double stop) {
        this.applyFunc = applyFunc;
        this.start = start;
        this.stop = stop;
    }

    @Override
    public void apply(EntityLivingBase entity, int level, int maxLevel) {
        this.level = level;
        this.maxLevel = maxLevel;
        this.applyFunc.apply(entity, this);
    }
}
