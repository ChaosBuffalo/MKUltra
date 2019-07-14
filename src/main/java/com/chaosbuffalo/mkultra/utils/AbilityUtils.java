package com.chaosbuffalo.mkultra.utils;

import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MKUMobData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AbilityUtils {

    public static boolean canTeleportEntity(EntityLivingBase entity){
        // TO DO more stuff, entity class blacklist, callback registration for things like lycanites interfaces
        IMobData mobData = MKUMobData.get(entity);
        if (mobData == null){
            return !EntityUtils.isLargeEntity(entity);
        }
        return !EntityUtils.isLargeEntity(entity) && !mobData.isBoss();
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
