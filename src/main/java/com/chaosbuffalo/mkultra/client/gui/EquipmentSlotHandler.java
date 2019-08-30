package com.chaosbuffalo.mkultra.client.gui;

import net.minecraft.entity.EntityLiving;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class EquipmentSlotHandler extends SlotItemHandler {
    private final EntityEquipmentSlot slot;


    public EquipmentSlotHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition,
                                EntityEquipmentSlot equipmentSlot) {
        super(itemHandler, index, xPosition, yPosition);
        slot = equipmentSlot;
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
        if (slot.equals(EntityEquipmentSlot.MAINHAND)) {
            return stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemBow;
        } else if (slot.equals(EntityEquipmentSlot.OFFHAND)) {
            return true;
        } else {
            if (stack.getItem() instanceof ItemArmor) {
                return EntityLiving.getSlotForItemStack(stack).equals(slot);
            } else {
                return false;
            }
        }
    }


}
