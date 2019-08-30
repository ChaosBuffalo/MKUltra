package com.chaosbuffalo.mkultra.core.stats;

import java.util.ArrayList;
import java.util.Collections;

public class CriticalStats<T> {

    private float defaultRate;
    private float defaultDamage;
    private final ArrayList<CriticalEntry> criticalEntries = new ArrayList<>();

    public CriticalStats(float defaultRate, float defaultDamage) {
        this.defaultRate = defaultRate;
        this.defaultDamage = defaultDamage;
    }

    public void addCriticalStats(Class<? extends T> objClass, int priority, float criticalChance, float damageMultiplier) {
        criticalEntries.add(new CriticalEntry(objClass, priority, criticalChance, damageMultiplier));
        Collections.sort(criticalEntries);
    }

    private boolean objectMatches(CriticalEntry stat, T obj) {
        Class<? extends T> entityClass = stat.entity;
        return entityClass.isInstance(obj);
    }

    public float getChance(T obj) {
        if (obj == null) {
            return defaultRate;
        }
        for (CriticalEntry stat : criticalEntries) {
            if (objectMatches(stat, obj)) {
                return stat.chance;
            }
        }
        return defaultRate;
    }

    public float getDamage(T obj) {
        if (obj == null) {
            return defaultDamage;
        }
        for (CriticalEntry stat : criticalEntries) {
            if (objectMatches(stat, obj)) {
                return stat.damageMultiplier;
            }
        }
        return defaultDamage;
    }

    public boolean hasChance(T obj) {
        return criticalEntries.stream().anyMatch(stat -> objectMatches(stat, obj));
    }

    private class CriticalEntry implements Comparable<CriticalEntry> {

        public final Class<? extends T> entity;
        final int priority;
        final float chance;
        final float damageMultiplier;

        CriticalEntry(Class<? extends T> entityClass, int priorityIn, float chanceIn, float damageMult) {
            entity = entityClass;
            priority = priorityIn;
            chance = chanceIn;
            damageMultiplier = damageMult;
        }

        @Override
        public int compareTo(CriticalEntry o) {
            return o.priority - priority;
        }
    }
}
