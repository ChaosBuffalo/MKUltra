package com.chaosbuffalo.mkultra.client.gui;

import com.chaosbuffalo.mkultra.tiles.TileEntityNPCSpawner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;

public class NPCEquipmentContainer extends Container {

    public static ArrayList<EntityEquipmentSlot> slotTypes = new ArrayList<>();

    static {
        slotTypes.add(EntityEquipmentSlot.MAINHAND);
        slotTypes.add(EntityEquipmentSlot.OFFHAND);
        slotTypes.add(EntityEquipmentSlot.HEAD);
        slotTypes.add(EntityEquipmentSlot.CHEST);
        slotTypes.add(EntityEquipmentSlot.LEGS);
        slotTypes.add(EntityEquipmentSlot.FEET);
    }

    private TileEntityNPCSpawner spawner;


    public NPCEquipmentContainer(TileEntityNPCSpawner spawner, EntityPlayer p) {
        this.spawner = spawner;
        int xPos = 26;
        int yPos = 20;
        int iid = 0;
        int num_slots = 6;
        int slot_size = 21;

        IItemHandler itemHandler = this.spawner.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        for (int x = 0; x < num_slots; ++x) {
            addSlotToContainer(new EquipmentSlotHandler(itemHandler, iid, xPos + x * slot_size, yPos, slotTypes.get(x)));
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

            if (index < TileEntityNPCSpawner.SIZE) {
                if (!this.mergeItemStack(itemstack1, TileEntityNPCSpawner.SIZE, this.inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, TileEntityNPCSpawner.SIZE, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }
}
