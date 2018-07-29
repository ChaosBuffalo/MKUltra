package com.chaosbuffalo.mkultra.utils;

import com.chaosbuffalo.mkultra.core.stats.CriticalStats;
import net.minecraft.item.*;

/**
 * Created by Jacob on 7/13/2018.
 */


public class ItemUtils {

    private static final float DEFAULT_CRIT_RATE = .0f;
    private static final float DEFAULT_CRIT_DAMAGE = 1.5f;
    public static CriticalStats<Item> CRIT = new CriticalStats<>(DEFAULT_CRIT_RATE, DEFAULT_CRIT_DAMAGE);

    public static void addCriticalStats(Class<? extends Item> itemIn, int priority, float criticalChance, float damageMultiplier){
        CRIT.addCriticalStats(itemIn, priority, criticalChance, damageMultiplier);
    }

    static {
        addCriticalStats(ItemSword.class, 0, .05f, 2.0f);
        addCriticalStats(ItemAxe.class, 0, .15f, 2.0f);
        addCriticalStats(ItemPickaxe.class, 0, .05f, 1.5f);
        addCriticalStats(ItemSpade.class, 0, .05f, 1.5f);
    }


    public static float getCritChanceForItem(ItemStack itemInHand) {
        if (itemInHand.equals(ItemStack.EMPTY)){
            return DEFAULT_CRIT_RATE;
        }
        Item item = itemInHand.getItem();
        return CRIT.getChance(item);
    }

    public static float getCritDamageForItem(ItemStack itemInHand){
        if (itemInHand.equals(ItemStack.EMPTY)){
            return DEFAULT_CRIT_DAMAGE;
        }
        Item item = itemInHand.getItem();
        return CRIT.getDamage(item);
    }

}
