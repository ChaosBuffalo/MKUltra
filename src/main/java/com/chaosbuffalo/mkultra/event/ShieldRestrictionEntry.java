package com.chaosbuffalo.mkultra.event;

import net.minecraft.item.Item;

/**
 * Created by Jacob on 7/21/2018.
 */
public class ShieldRestrictionEntry implements Comparable<ShieldRestrictionEntry> {
    public final Class<? extends Item> item;

    public final int priority;

    public ShieldRestrictionEntry(Class<? extends Item> itemClass, int priorityIn){
        item = itemClass;
        priority = priorityIn;
    }

    public boolean matches(Item other) {
        return  item.isInstance(other);
    }

    @Override
    public int compareTo(ShieldRestrictionEntry o) {
        return o.priority - priority;
    }
}
