package com.chaosbuffalo.mkultra.spawn;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;

import java.util.function.BiConsumer;

public class ItemAssigners {

    private static void livingEquipmentAssign(EntityLivingBase entity, EntityEquipmentSlot slot, ItemChoice choice) {
        entity.setItemStackToSlot(slot, choice.item);
        if (entity instanceof EntityLiving) {
            EntityLiving entLiv = (EntityLiving) entity;
            entLiv.setDropChance(slot, choice.dropChance);
        }
    }

    public static BiConsumer<EntityLivingBase, ItemChoice> MAINHAND = (entity, choice) ->
            livingEquipmentAssign(entity, EntityEquipmentSlot.MAINHAND, choice);

    public static BiConsumer<EntityLivingBase, ItemChoice> OFFHAND = (entity, choice) ->
            livingEquipmentAssign(entity, EntityEquipmentSlot.OFFHAND, choice);

    public static BiConsumer<EntityLivingBase, ItemChoice> HEAD = (entity, choice) ->
            livingEquipmentAssign(entity, EntityEquipmentSlot.HEAD, choice);

    public static BiConsumer<EntityLivingBase, ItemChoice> CHEST = (entity, choice) ->
            livingEquipmentAssign(entity, EntityEquipmentSlot.CHEST, choice);

    public static BiConsumer<EntityLivingBase, ItemChoice> FEET = (entity, choice) ->
            livingEquipmentAssign(entity, EntityEquipmentSlot.FEET, choice);

    public static BiConsumer<EntityLivingBase, ItemChoice> LEGS = (entity, choice) ->
            livingEquipmentAssign(entity, EntityEquipmentSlot.LEGS, choice);
}
