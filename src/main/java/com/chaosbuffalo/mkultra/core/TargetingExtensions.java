package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.function.BiFunction;

public class TargetingExtensions {

    public static void init(){
        BiFunction<Entity, Entity, Boolean> isMobSameFaction = (entity, other) -> {
            if (entity instanceof EntityLivingBase && other instanceof EntityLivingBase &&
                    !(other instanceof EntityPlayer)){
                IMobData mobData = MKUMobData.get((EntityLivingBase) entity);
                if (mobData != null){
                    return mobData.isSameFaction((EntityLivingBase)other);
                } else {
                    return false;
                }
            } else {
                return false;
            }

        };
        Targeting.registerFriendlyCallback(isMobSameFaction);
    }
}
