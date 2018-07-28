package com.chaosbuffalo.mkultra.utils;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;

/**
 * Created by Jacob on 7/27/2018.
 */
public class EntityCriticalStats implements Comparable<EntityCriticalStats> {

    public final Class<? extends Entity> entity;

    public final int priority;

    public final float chance;

    public final float damageMultiplier;

    public EntityCriticalStats(Class<? extends Entity> entityClass, int priorityIn, float chanceIn, float damageMult){
        entity = entityClass;
        priority = priorityIn;
        chance = chanceIn;
        damageMultiplier = damageMult;
    }

    @Override
    public int compareTo(EntityCriticalStats o) {
        return o.priority - priority;
    }

}


