package com.chaosbuffalo.mkultra.spawner;

import net.minecraft.item.ItemStack;

public class ItemChoice {
    public final ItemStack item;
    public final int weight;
    public final int minLevel;

    public ItemChoice(ItemStack item, int weight, int minLevel){
        this.item = item;
        this.weight = weight;
        this.minLevel = minLevel;
    }
}
