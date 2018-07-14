package com.chaosbuffalo.mkultra.utils;
import net.minecraft.item.Item;


/**
 * Created by Jacob on 7/13/2018.
 */
public class ItemCriticalStats implements Comparable<ItemCriticalStats> {

    public final Class<? extends Item> item;

    public final int priority;

    public final float chance;

    public final float damageMultiplier;

    public ItemCriticalStats(Class<? extends Item> itemClass, int priorityIn, float chanceIn, float damageMult){
        item = itemClass;
        priority = priorityIn;
        chance = chanceIn;
        damageMultiplier = damageMult;
    }

    @Override
    public int compareTo(ItemCriticalStats o) {
        return o.priority - priority;
    }

}
