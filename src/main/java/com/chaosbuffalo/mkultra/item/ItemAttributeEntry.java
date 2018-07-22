package com.chaosbuffalo.mkultra.item;

import java.util.UUID;

/**
 * Created by Jacob on 7/22/2018.
 */
public class ItemAttributeEntry {

    public final UUID modifier_id;
    public final double amount;
    public final int operation;
    public final String name;
    public final String attr_name;

    public ItemAttributeEntry(UUID modifier_id, double amount, int operation, String name, String attr_name){
        this.modifier_id = modifier_id;
        this.amount = amount;
        this.operation = operation;
        this.name = name;
        this.attr_name = attr_name;

    }
}
