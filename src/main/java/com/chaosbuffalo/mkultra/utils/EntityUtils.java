package com.chaosbuffalo.mkultra.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.*;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Jacob on 7/27/2018.
 */
public class EntityUtils {
    public static final ArrayList<EntityCriticalStats> CRITICAL_PRIORITY = new ArrayList<>();

    public static final float DEFAULT_CRIT_RATE = 0.0f;

    public static final float DEFAULT_CRIT_DAMAGE = 1.0f;

    public static void addCriticalStats(Class<? extends Entity> entityIn, int priority, float criticalChance, float damageMultiplier){
        CRITICAL_PRIORITY.add(new EntityCriticalStats(entityIn, priority, criticalChance, damageMultiplier));
        Collections.sort(CRITICAL_PRIORITY);
    }

    static {
        addCriticalStats(EntityArrow.class, 0, .05f, 2.5f);
        addCriticalStats(EntityTippedArrow.class, 1, .1f, 2.5f);
        addCriticalStats(EntitySpectralArrow.class, 1, .15f, 2.5f);
        addCriticalStats(EntityThrowable.class, 0, .05f, 2.0f);
    }

    public static boolean entityHasCriticalChance(Entity entity){
        for (EntityCriticalStats stat : CRITICAL_PRIORITY){
            if (matchesThisCritStats(stat, entity)){
                return true;
            }
        }
        return false;
    }

    private static boolean matchesThisCritStats(EntityCriticalStats stat, Entity entity){
        Class<? extends Entity> entityClass = stat.entity;
        if (entityClass.isInstance(entity)){
            return true;
        } else {
            return false;
        }
    }

    public static boolean isEntityInstance(Class<? extends Entity> entityClass, Entity toCheck){
        return entityClass.isInstance(toCheck);
    }


    public static float getCritChanceForEntity(Entity entity) {
        if (entity == null) {
            return DEFAULT_CRIT_RATE;
        }
        for (EntityCriticalStats stat : CRITICAL_PRIORITY){
            if (matchesThisCritStats(stat, entity)){
                return stat.chance;
            }
        }
        return DEFAULT_CRIT_RATE;
    }

    public static float getCritDamageForEntity(Entity entity){
        if (entity == null) {
            return DEFAULT_CRIT_DAMAGE;
        }
        for (EntityCriticalStats stat : CRITICAL_PRIORITY){
            if (matchesThisCritStats(stat, entity)){
                return stat.damageMultiplier;
            }
        }
        return DEFAULT_CRIT_DAMAGE;

    }
}
