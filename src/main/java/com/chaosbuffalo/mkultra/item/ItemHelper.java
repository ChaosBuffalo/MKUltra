package com.chaosbuffalo.mkultra.item;

import com.chaosbuffalo.mkultra.init.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.HashSet;

public class ItemHelper {

    public static boolean isArrow(ItemStack stack)
    {
        return !stack.isEmpty() && stack.getItem() instanceof ItemArrow;
    }


    private static final HashSet<ItemStack> SMOKEABLES = new HashSet<>();

    static {
        registerSMokeable(new ItemStack(Items.BLAZE_POWDER));
    }

    public static void registerSMokeable(ItemStack item){
        SMOKEABLES.add(item);
    }

    public static boolean isSmokeable(ItemStack stack) {
        // TEMP
        if (!stack.isEmpty()){
            for (ItemStack smokeable : SMOKEABLES){
                if (ItemHandlerHelper.canItemStacksStack(smokeable, stack)){
                    return true;
                }
            }
        }
        return false;
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


                ItemStack smokeableStack = itemstack.copy();
                smokeableStack.setCount(1);
                if (isSmokeable(smokeableStack))
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
