package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.Capabilities;
import net.minecraft.entity.EntityLivingBase;

import javax.annotation.Nullable;

public class MKUMobData {

    @Nullable
    public static IMobData get(EntityLivingBase entity)
    {
        if (entity.hasCapability(Capabilities.MOB_DATA_CAPABILITY, null)) {
            return entity.getCapability(Capabilities.MOB_DATA_CAPABILITY, null);
        }
        return null;
    }
}
