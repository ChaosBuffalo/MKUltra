package com.chaosbuffalo.mkultra.utils;

import com.chaosbuffalo.mkultra.core.stats.CriticalStats;
import com.chaosbuffalo.mkultra.entities.projectiles.EntityBaseProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntitySpectralArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jacob on 7/27/2018.
 */
public class EntityUtils {

    private static final float DEFAULT_CRIT_RATE = 0.0f;
    private static final float DEFAULT_CRIT_DAMAGE = 1.0f;
    // Based on Skeleton Volume
    private static final double LARGE_VOLUME = 3.0 * .6 * .6 * 1.8;
    public static CriticalStats<Entity> ENTITY_CRIT = new CriticalStats<>(DEFAULT_CRIT_RATE, DEFAULT_CRIT_DAMAGE);

    public static final Set<ResourceLocation> DISABLED_MOBS = new HashSet<>();

    public static void addCriticalStats(Class<? extends Entity> entityIn, int priority, float criticalChance, float damageMultiplier) {
        ENTITY_CRIT.addCriticalStats(entityIn, priority, criticalChance, damageMultiplier);
    }

    public static void addDisabledMob(ResourceLocation name){
        DISABLED_MOBS.add(name);
    }

    public static void clearDisabledMobs(){
        DISABLED_MOBS.clear();
    }

    static {
        addCriticalStats(EntityArrow.class, 0, .1f, 2.0f);
        addCriticalStats(EntityTippedArrow.class, 1, .1f, 2.0f);
        addCriticalStats(EntitySpectralArrow.class, 1, .15f, 2.0f);
        addCriticalStats(EntityThrowable.class, 0, .05f, 2.0f);
    }

    public static double calculateBoundingBoxVolume(EntityLivingBase entityIn) {
        AxisAlignedBB box = entityIn.getEntityBoundingBox();
        return (box.maxX - box.minX) * (box.maxY - box.minY) * (box.maxZ - box.minZ);
    }

    public static boolean isLargeEntity(EntityLivingBase entityIn) {
        double vol = calculateBoundingBoxVolume(entityIn);
        return vol >= LARGE_VOLUME;
    }

    public static double getDrag(EntityBaseProjectile projectile) {
        if (projectile.isInWater()) {
            return projectile.getWaterDrag();
        } else {
            return projectile.getFlightDrag();
        }
    }

    public static int travelTimeForDistance(double distance, double speed, double drag, float gravity) {
        int ticks = 0;
        while (distance > 0) {
            distance -= speed * Math.pow(drag, ticks);
            ticks++;
        }
        return ticks;
    }

    public static void shootProjectileAtTarget(EntityBaseProjectile projectile, EntityLivingBase target,
                                               float speed, float accuracy) {

        double distance = projectile.getPositionVector().distanceTo(target.getPositionVector());
        double drag = getDrag(projectile);
        int travelTimeInTicks = travelTimeForDistance(distance, speed, drag, projectile.getGravityVelocity());
        double gravityOffset = 0.0;
        float gravity = projectile.getGravityVelocity();
        // WARNING BIG BRAIN ALGORITHM, ACCOUNTS FOR GRAVITY-ISH
        for (int i = 0; i < travelTimeInTicks; i++) {
            int multiplier = ((i / 10) * 2) + 2;
            gravityOffset += gravity * multiplier;
        }
        double d1 = target.posX - projectile.posX;
        double d2 = (target.posY + gravityOffset + (target.getEyeHeight() / 2.0)) - projectile.posY;
        double d3 = target.posZ - projectile.posZ;
        projectile.shoot(d1, d2, d3, speed, accuracy);
    }
}
