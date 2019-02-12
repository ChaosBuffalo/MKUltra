package com.chaosbuffalo.mkultra.spawner;

import net.minecraft.entity.ai.EntityAIBase;

public class BehaviorChoice {
    public final int minLevel;
    public final Class<? extends EntityAIBase> aiClass;

    public BehaviorChoice(Class<? extends EntityAIBase> aiClass, int minLevel){
        this.aiClass = aiClass;
        this.minLevel = minLevel;
    }
}
