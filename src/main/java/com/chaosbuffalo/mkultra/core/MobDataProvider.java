package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.Capabilities;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;

public class MobDataProvider implements ICapabilitySerializable<NBTTagCompound> {

    private MobData data;

    public MobDataProvider(EntityLivingBase entity) {
        this.data = new MobData(entity);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, EnumFacing facing) {
        return capability == Capabilities.MOB_DATA_CAPABILITY;
    }

    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, EnumFacing facing) {
        if (hasCapability(capability, facing)) {
            return Capabilities.MOB_DATA_CAPABILITY.cast(data);
        }
        return null;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        Capabilities.MOB_DATA_CAPABILITY.readNBT(data, null, nbt);
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return (NBTTagCompound) Capabilities.MOB_DATA_CAPABILITY.writeNBT(data, null);
    }

}
