package com.chaosbuffalo.mkultra.utils;

import com.chaosbuffalo.mkultra.core.stats.CriticalStats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.math.AxisAlignedBB;

/**
 * Created by Jacob on 7/27/2018.
 */
public class EntityUtils {

    private static final float DEFAULT_CRIT_RATE = 0.0f;
    private static final float DEFAULT_CRIT_DAMAGE = 1.0f;
    // Based on Skeleton Volume
    private static final double LARGE_VOLUME = 3.0 * .6 * .6 * 1.8;
    public static CriticalStats<Entity> ENTITY_CRIT = new CriticalStats<>(DEFAULT_CRIT_RATE, DEFAULT_CRIT_DAMAGE);

    public static void addCriticalStats(Class<? extends Entity> entityIn, int priority, float criticalChance, float damageMultiplier){
        ENTITY_CRIT.addCriticalStats(entityIn, priority, criticalChance, damageMultiplier);
    }

    static {
        addCriticalStats(EntityArrow.class, 0, .1f, 2.0f);
        addCriticalStats(EntityTippedArrow.class, 1, .1f, 2.0f);
        addCriticalStats(EntitySpectralArrow.class, 1, .15f, 2.0f);
        addCriticalStats(EntityThrowable.class, 0, .05f, 2.0f);
    }

    public static double calculateBoundingBoxVolume(EntityLivingBase entityIn){
        AxisAlignedBB box = entityIn.getEntityBoundingBox();
        return (box.maxX - box.minX) * (box.maxY - box.minY) * (box.maxZ - box.minZ);
    }

    public static boolean isLargeEntity(EntityLivingBase entityIn){
        double vol = calculateBoundingBoxVolume(entityIn);
        return vol >= LARGE_VOLUME;
    }
}
