package com.chaosbuffalo.mkultra.core;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.List;

public interface IClassProvider {
    ResourceLocation getIdentity();
    String getClassSelectionText();

    default boolean teachesClass(PlayerClass playerClass) {
        return getClasses().contains(playerClass.getClassId());
    }

    @Nonnull
    default List<ResourceLocation> getClasses() {
        return ClassLists.getOrCreate(getIdentity()).getClasses();
    }

    static IClassProvider getProvider(ItemStack stack) {
        if (stack.isEmpty())
            return null;

        if (stack.getItem() instanceof IClassProvider) {
            return (IClassProvider) stack.getItem();
        }
        return null;
    }

    static IClassProvider getProvider(TileEntity tileEntity) {
        if (tileEntity instanceof IClassProvider) {
            return (IClassProvider) tileEntity;
        }
        return null;
    }
}
