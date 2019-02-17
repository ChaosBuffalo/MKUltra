package com.chaosbuffalo.mkultra.core;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class MobDataStorage implements Capability.IStorage<IMobData> {

    @Override
    public NBTTagCompound writeNBT(Capability<IMobData> capability,
                                   IMobData instance, EnumFacing side) {
        NBTTagCompound nbt = new NBTTagCompound();
        instance.serialize(nbt);
        return nbt;
    }


    @Override
    public void readNBT(Capability<IMobData> capability, IMobData instance,
                        EnumFacing side, NBTBase nbtBase) {
        NBTTagCompound nbt = (NBTTagCompound) nbtBase;
        instance.deserialize(nbt);
    }
}
