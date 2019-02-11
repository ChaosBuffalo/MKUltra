package com.chaosbuffalo.mkultra.spawner;

import net.minecraft.entity.EntityLivingBase;

public abstract class MobOption {

    public int level;
    public int maxLevel;

    public abstract void apply(EntityLivingBase entity, int level, int maxLevel);
}
