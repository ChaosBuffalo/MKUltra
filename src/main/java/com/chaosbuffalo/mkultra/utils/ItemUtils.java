package com.chaosbuffalo.mkultra.utils;

import net.minecraft.item.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

/**
 * Created by Jacob on 7/13/2018.
 */


public class ItemUtils {

    public static final ArrayList<ItemCriticalStats> CRITICAL_PRIORITY = new ArrayList<>();

    public static final float DEFAULT_CRIT_RATE = .0f;

    public static final float DEFAULT_CRIT_DAMAGE = 1.5f;

    public static void addCriticalStats(Class<? extends Item> itemIn, int priority, float criticalChance, float damageMultiplier){
        CRITICAL_PRIORITY.add(new ItemCriticalStats(itemIn, priority, criticalChance, damageMultiplier));
        Collections.sort(CRITICAL_PRIORITY);
    }

    static {
        addCriticalStats(ItemSword.class, 0, .05f, 2.0f);
        addCriticalStats(ItemAxe.class, 0, .15f, 2.0f);
        addCriticalStats(ItemPickaxe.class, 0, .05f, 1.5f);
        addCriticalStats(ItemSpade.class, 0, .05f, 1.5f);
    }

    public static boolean itemHasCriticalChance(Item item){
        for (ItemCriticalStats stat : CRITICAL_PRIORITY){
            if (matchesThisCritStats(stat, item)){
                return true;
            }
        }
        return false;
    }

    private static boolean matchesThisCritStats(ItemCriticalStats stat, Item item){
        Class<? extends Item> itemClass = stat.item;
        if (itemClass.isInstance(item)){
            return true;
        } else {
            return false;
        }
    }

    public static boolean isItemInstance(Class<? extends Item> itemClass, Item toCheck){
        return itemClass.isInstance(toCheck);
    }


    public static float getCritChanceForItem(ItemStack itemInHand) {
        if (itemInHand.equals(ItemStack.EMPTY)){
            return DEFAULT_CRIT_RATE;
        }
        Item item = itemInHand.getItem();
        for (ItemCriticalStats stat : CRITICAL_PRIORITY){
            if (matchesThisCritStats(stat, item)){
                return stat.chance;
            }
        }
        return DEFAULT_CRIT_RATE;
    }

    public static float getCritDamageForItem(ItemStack itemInHand){
        if (itemInHand.equals(ItemStack.EMPTY)){
            return DEFAULT_CRIT_DAMAGE;
        }
        Item item = itemInHand.getItem();
        for (ItemCriticalStats stat : CRITICAL_PRIORITY){
            if (matchesThisCritStats(stat, item)){
                return stat.damageMultiplier;
            }
        }
        return DEFAULT_CRIT_DAMAGE;

    }
}
