package com.chaosbuffalo.mkultra.client.gui;

import com.chaosbuffalo.mkultra.item.ItemHelper;
import com.chaosbuffalo.mkultra.utils.SmokeUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

/**
 * Created by Jacob on 8/5/2018.
 */
public class PipeSlotHandler extends SlotItemHandler {

    public PipeSlotHandler(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack)
    {
        if (!SmokeUtils.isSmokeable(stack)){
            return false;
        }
        return super.isItemValid(stack);
    }
}
