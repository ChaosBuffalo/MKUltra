package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.Capabilities;
import jline.internal.Nullable;
import net.minecraft.entity.EntityLivingBase;

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
