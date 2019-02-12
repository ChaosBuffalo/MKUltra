package com.chaosbuffalo.mkultra.spawner;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumHand;
import java.util.function.BiConsumer;

public class ItemAssigners {

    public static BiConsumer<EntityLivingBase, ItemChoice> MAINHAND = (entity, choice) -> {
        entity.setHeldItem(EnumHand.MAIN_HAND, choice.item);
    };

    public static BiConsumer<EntityLivingBase, ItemChoice> OFFHAND = (entity, choice) -> {
        entity.setHeldItem(EnumHand.OFF_HAND, choice.item);
    };

    public static BiConsumer<EntityLivingBase, ItemChoice> HEAD = (entity, choice) -> {
        entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, choice.item);
    };

    public static BiConsumer<EntityLivingBase, ItemChoice> CHEST = (entity, choice) -> {
        entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, choice.item);
    };

    public static BiConsumer<EntityLivingBase, ItemChoice> FEET = (entity, choice) -> {
        entity.setItemStackToSlot(EntityEquipmentSlot.FEET, choice.item);
    };

    public static BiConsumer<EntityLivingBase, ItemChoice> LEGS = (entity, choice) -> {
        entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, choice.item);
    };
}
