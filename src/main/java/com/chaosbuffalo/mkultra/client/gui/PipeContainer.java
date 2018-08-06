package com.chaosbuffalo.mkultra.client.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraftforge.items.IItemHandler;

/**
 * Created by Jacob on 8/5/2018.
 */
public class PipeContainer extends Container {

    public PipeContainer(IItemHandler i, EntityPlayer p ) {

        int xPos = 80;
        int yPos = 20;
        int iid = 0;
        int num_slots = 1;
        int slot_size = 18;

        for( int x = 0; x < num_slots; ++x ) {
            addSlotToContainer( new PipeSlotHandler(i, iid, xPos + x * slot_size, yPos));
            iid++;
        }

        xPos = 8;
        yPos = 51;

        for( int y = 0; y < 3; ++y ) {
            for( int x = 0; x < 9; ++x ) {
                addSlotToContainer( new Slot(p.inventory, x + y * 9 + 9, xPos + x * 18, yPos + y * 18 ));
            }
        }

        yPos = 109;
        for( int x = 0; x < 9; ++x ) {
            addSlotToContainer( new Slot(p.inventory, x, xPos + x * 18, yPos));
        }

    }

    @Override
    public boolean canInteractWith( EntityPlayer p ) {
        return true;
    }
}
