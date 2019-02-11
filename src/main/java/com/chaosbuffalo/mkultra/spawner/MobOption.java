package com.chaosbuffalo.mkultra.spawner;

import net.minecraft.entity.EntityLivingBase;

public abstract class MobOption {

    public abstract void apply(EntityLivingBase entity, int level, int maxLevel);
}
