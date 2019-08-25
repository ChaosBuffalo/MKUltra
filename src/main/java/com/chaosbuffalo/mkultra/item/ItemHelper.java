package com.chaosbuffalo.mkultra.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

import java.util.function.Predicate;

public class ItemHelper {

    private static final Predicate<ItemStack> IS_EMPTY = i -> !i.isEmpty();

    public static ItemStack find(EntityPlayer player, Predicate<ItemStack> filter) {
        filter = IS_EMPTY.and(filter);
        if (filter.test(player.getHeldItem(EnumHand.OFF_HAND))) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        } else if (filter.test(player.getHeldItem(EnumHand.MAIN_HAND))) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (filter.test(itemstack)) {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }

    public static void unequip(EntityPlayer player, EntityEquipmentSlot slot) {
        ItemStack off = player.getItemStackFromSlot(slot);
        if (!player.inventory.addItemStackToInventory(off)) {
            player.dropItem(off, true);
        }
        player.setItemStackToSlot(slot, ItemStack.EMPTY);
    }

    public static void damageStack(EntityPlayer player, ItemStack stack, int amount) {
        stack.damageItem(amount, player);
        if (stack.getItemDamage() >= stack.getMaxDamage()) {
            player.inventory.deleteStack(stack);
        }
    }

    public static boolean shrinkStack(EntityPlayer player, ItemStack stack, int amount) {
        if (stack.getCount() < amount) {
            return false;
        }

        stack.shrink(amount);
        if (stack.getCount() == 0) {
            player.inventory.deleteStack(stack);
        }

        return true;
    }
}
