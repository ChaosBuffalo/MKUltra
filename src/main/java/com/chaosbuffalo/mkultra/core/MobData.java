package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.log.Log;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;

public class MobData implements IMobData {

    private final static DataParameter<Boolean> IS_MK_SPAWNED = EntityDataManager.createKey(
            EntityLiving.class, DataSerializers.BOOLEAN);


    private final EntityLivingBase entity;
    private final EntityDataManager privateData;
    private  boolean isMKSpawned;

    public MobData(EntityLivingBase entity) {
        this.entity = entity;
        privateData = entity.getDataManager();
        setupWatcher();
    }

    private void setupWatcher() {
    }

    private void markEntityDataDirty() {
    }

    @Override
    public boolean isMKSpawned() {
        return isMKSpawned;
    }

    @Override
    public void setMKSpawned(boolean isSpawned) {
        isMKSpawned = isSpawned;
    }


    @Override
    public void serialize(NBTTagCompound tag) {
        tag.setBoolean("isMKSpawned", isMKSpawned());
    }

    @Override
    public void deserialize(NBTTagCompound tag) {
        if (tag.hasKey("isMKSpawned")) {
            setMKSpawned(tag.getBoolean("isMKSpawned"));
        }
    }
}
