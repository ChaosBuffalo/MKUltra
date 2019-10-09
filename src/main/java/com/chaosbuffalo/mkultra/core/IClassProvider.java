package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.entity.player.EntityPlayer;
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

    default boolean meetsRequirements(EntityPlayer player, PlayerClass playerClass) {
        return teachesClass(playerClass);
    }

    default void onProviderUse(EntityPlayer player, PlayerClass playerClass) {
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

    IClassProvider TEACH_ALL = new IClassProvider() {
        @Override
        public ResourceLocation getIdentity() {
            return new ResourceLocation(MKUltra.MODID, "provider.all");
        }

        @Override
        public String getClassSelectionText() {
            return "Select your next class";
        }

        @Override
        public boolean teachesClass(PlayerClass playerClass) {
            return true;
        }

        @Nonnull
        @Override
        public List<ResourceLocation> getClasses() {
            return MKURegistry.getAllEnabledClasses();
        }
    };
}
