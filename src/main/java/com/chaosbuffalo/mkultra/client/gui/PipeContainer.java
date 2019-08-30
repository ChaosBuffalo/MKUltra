package com.chaosbuffalo.mkultra.client.gui;

import com.chaosbuffalo.mkultra.utils.SmokeUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by Jacob on 8/5/2018.
 */
public class PipeContainer extends Container {

    public PipeContainer(IItemHandler i, EntityPlayer p) {

        int xPos = 80;
        int yPos = 20;
        int iid = 0;
        int num_slots = 1;
        int slot_size = 18;

        for (int x = 0; x < num_slots; ++x) {
            addSlotToContainer(new PipeSlotHandler(i, iid, xPos + x * slot_size, yPos));
            iid++;
        }

        xPos = 8;
        yPos = 51;

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                addSlotToContainer(new Slot(p.inventory, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
            }
        }

        yPos = 109;
        for (int x = 0; x < 9; ++x) {
            addSlotToContainer(new Slot(p.inventory, x, xPos + x * 18, yPos));
        }

    }

    @Override
    public boolean canInteractWith(EntityPlayer p) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index == 0) {
                if (!this.mergeItemStack(itemstack1, 1, 37, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (SmokeUtils.isSmokeable(itemstack1)) {
                if (!this.mergeItemStack(itemstack1, 0, 1, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (((Slot) this.inventorySlots.get(0)).getHasStack() || !((Slot) this.inventorySlots.get(0)).isItemValid(itemstack1)) {
                    return ItemStack.EMPTY;
                }

                if (itemstack1.hasTagCompound())// Forge: Fix MC-17431
                {
                    ((Slot) this.inventorySlots.get(0)).putStack(itemstack1.splitStack(1));
                } else if (!itemstack1.isEmpty()) {
                    ((Slot) this.inventorySlots.get(0)).putStack(new ItemStack(itemstack1.getItem(), 1, itemstack1.getMetadata()));
                    itemstack1.shrink(1);
                }
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }
}
