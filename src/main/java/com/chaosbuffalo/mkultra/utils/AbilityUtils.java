package com.chaosbuffalo.mkultra.utils;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AbilityUtils {

    public static boolean canTeleportEntity(EntityLivingBase entity){
        // TO DO more stuff, entity class blacklist, callback registration for things like lycanites interfaces
        return !EntityUtils.isLargeEntity(entity);
    }

    public static void safeTeleportEntity(World theWorld, EntityLivingBase targetEntity, Vec3d teleLoc){
        RayTraceResult colTrace = RayTraceUtils.rayTraceBlocks(theWorld, targetEntity.getPositionVector(),
                teleLoc, false);
        if (colTrace != null && colTrace.typeOfHit == RayTraceResult.Type.BLOCK){
            teleLoc = colTrace.hitVec;
        }
        targetEntity.setPositionAndUpdate(teleLoc.x, teleLoc.y, teleLoc.z);
    }
}
