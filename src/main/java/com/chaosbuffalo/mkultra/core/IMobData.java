package com.chaosbuffalo.mkultra.core;

import net.minecraft.nbt.NBTTagCompound;

public interface IMobData {

    boolean isMKSpawned();

    void setMKSpawned(boolean isSpawned);

    void serialize(NBTTagCompound tag);

    void deserialize(NBTTagCompound tag);
}
