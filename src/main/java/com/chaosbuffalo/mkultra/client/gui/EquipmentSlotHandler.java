package com.chaosbuffalo.mkultra.client.gui;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
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
    public boolean isItemValid(@Nonnull ItemStack stack)
    {
        if (slot.equals(EntityEquipmentSlot.MAINHAND)){
            return stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemBow;
        } else if (slot.equals(EntityEquipmentSlot.OFFHAND)){
            return true;
        } else {
            if (stack.getItem() instanceof ItemArmor){
                ItemArmor armor = (ItemArmor) stack.getItem();
                return armor.getEquipmentSlot().equals(slot);
            } else {
                return false;
            }
        }
    }


}
