package com.chaosbuffalo.mkultra.item;

import net.minecraft.entity.ai.attributes.RangedAttribute;

/**
 * Created by Jacob on 7/22/2018.
 */
public class ItemAttributeEntry {

    public final double amount;
    public final int operation;
    public final RangedAttribute attr;

    public ItemAttributeEntry(double amount, int operation, RangedAttribute attr) {
        this.amount = amount;
        this.operation = operation;
        this.attr = attr;

    }
}
