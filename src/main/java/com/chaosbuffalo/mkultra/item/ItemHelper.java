package com.chaosbuffalo.mkultra.item;

import com.chaosbuffalo.mkultra.init.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ItemHelper {

    public static boolean isArrow(ItemStack stack)
    {
        return !stack.isEmpty() && stack.getItem() instanceof ItemArrow;
    }

    public static boolean isSmokeable(ItemStack stack) {
        return !stack.isEmpty() && stack.getItem().equals(ModItems.hempLeaves);
    }

    public static ItemStack findSmokeable(EntityPlayer player)
    {
        if (isSmokeable(player.getHeldItem(EnumHand.OFF_HAND))){
            return player.getHeldItem((EnumHand.OFF_HAND));
        } else if (isSmokeable(player.getHeldItem(EnumHand.MAIN_HAND))){
            return player.getHeldItem(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (isSmokeable(itemstack))
                {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }

    public static ItemStack findAmmo(EntityPlayer player)
    {
        if (isArrow(player.getHeldItem(EnumHand.OFF_HAND)))
        {
            return player.getHeldItem(EnumHand.OFF_HAND);
        }
        else if (isArrow(player.getHeldItem(EnumHand.MAIN_HAND)))
        {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        }
        else
        {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
            {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (isArrow(itemstack))
                {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }

    public static void unequip(EntityPlayer player, EntityEquipmentSlot slot) {
        ItemStack off = player.getItemStackFromSlot(slot);
        if (!player.inventory.addItemStackToInventory(off))
        {
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

    public static void shrinkStack(EntityPlayer player, ItemStack stack, int amount) {
        stack.shrink(amount);
        if (stack.getCount() == 0) {
            player.inventory.deleteStack(stack);
        }
    }
}
