package com.chaosbuffalo.mkultra.utils;

import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.core.abilities.cast_states.CastState;
import net.minecraft.client.audio.Sound;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class AbilityUtils {

    public static boolean canTeleportEntity(EntityLivingBase entity) {
        // TO DO more stuff, entity class blacklist, callback registration for things like lycanites interfaces
        IMobData mobData = MKUMobData.get(entity);
        if (mobData == null) {
            return !EntityUtils.isLargeEntity(entity);
        }
        return !EntityUtils.isLargeEntity(entity) && !mobData.isBoss();
    }

    public static void playSoundAtServerEntity(Entity entity, SoundEvent event, SoundCategory cat){
        playSoundAtServerEntity(entity, event, cat, 1.0f, 1.0f);
    }

    public static void playSoundAtServerEntity(Entity entity, SoundEvent event, SoundCategory cat, float volume, float pitch){
        if (event == null){
            return;
        }
        entity.world.playSound(null, entity.posX, entity.posY, entity.posZ, event,
                cat, volume, pitch);
    }

    public static void safeTeleportEntity(World theWorld, EntityLivingBase targetEntity, Vec3d teleLoc) {
        RayTraceResult colTrace = RayTraceUtils.rayTraceBlocks(theWorld, targetEntity.getPositionVector(),
                teleLoc, false);
        if (colTrace != null && colTrace.typeOfHit == RayTraceResult.Type.BLOCK) {
            teleLoc = colTrace.hitVec;
        }
        targetEntity.setPositionAndUpdate(teleLoc.x, teleLoc.y, teleLoc.z);
    }

    public static SoundCategory getSoundCategoryForEntity(EntityLivingBase entity){
        if (entity instanceof EntityPlayer){
            return SoundCategory.PLAYERS;
        } else {
            return SoundCategory.HOSTILE;
        }
    }

    @Nullable
    public static <T extends CastState> T getCastStateAsType(CastState state, Class<T> clazz){
        if (clazz.isInstance(state)){
            return (T) state;
        } else {
            return null;
        }
    }

}
