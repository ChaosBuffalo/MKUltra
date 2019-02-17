package com.chaosbuffalo.mkultra.spawn;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.util.EnumHand;
import java.util.function.BiConsumer;

public class ItemAssigners {

    public static BiConsumer<EntityLivingBase, ItemChoice> MAINHAND = (entity, choice) -> {
        entity.setHeldItem(EnumHand.MAIN_HAND, choice.item);
        if (entity instanceof EntityLiving) {
            EntityLiving entLiv = (EntityLiving)entity;
            entLiv.setDropChance(EntityEquipmentSlot.MAINHAND, choice.dropChance);
        }
    };

    public static BiConsumer<EntityLivingBase, ItemChoice> OFFHAND = (entity, choice) -> {
        entity.setHeldItem(EnumHand.OFF_HAND, choice.item);
        if (entity instanceof EntityLiving) {
            EntityLiving entLiv = (EntityLiving)entity;
            entLiv.setDropChance(EntityEquipmentSlot.OFFHAND, choice.dropChance);
        }
    };

    public static BiConsumer<EntityLivingBase, ItemChoice> HEAD = (entity, choice) -> {
        entity.setItemStackToSlot(EntityEquipmentSlot.HEAD, choice.item);
        if (entity instanceof EntityLiving) {
            EntityLiving entLiv = (EntityLiving)entity;
            entLiv.setDropChance(EntityEquipmentSlot.HEAD, choice.dropChance);
        }
    };

    public static BiConsumer<EntityLivingBase, ItemChoice> CHEST = (entity, choice) -> {
        entity.setItemStackToSlot(EntityEquipmentSlot.CHEST, choice.item);
        if (entity instanceof EntityLiving) {
            EntityLiving entLiv = (EntityLiving)entity;
            entLiv.setDropChance(EntityEquipmentSlot.CHEST, choice.dropChance);
        }
    };

    public static BiConsumer<EntityLivingBase, ItemChoice> FEET = (entity, choice) -> {
        entity.setItemStackToSlot(EntityEquipmentSlot.FEET, choice.item);
        if (entity instanceof EntityLiving) {
            EntityLiving entLiv = (EntityLiving)entity;
            entLiv.setDropChance(EntityEquipmentSlot.FEET, choice.dropChance);
        }
    };

    public static BiConsumer<EntityLivingBase, ItemChoice> LEGS = (entity, choice) -> {
        entity.setItemStackToSlot(EntityEquipmentSlot.LEGS, choice.item);
        if (entity instanceof EntityLiving) {
            EntityLiving entLiv = (EntityLiving)entity;
            entLiv.setDropChance(EntityEquipmentSlot.LEGS, choice.dropChance);
        }
    };
}
