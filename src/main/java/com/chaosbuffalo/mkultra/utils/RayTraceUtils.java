/*
 * VariousUtils.java (VariousUtils)
 * Package minersbasic.internal
 *
 * Project: Miners Basic
 *
 * Author: _Bedrock_Miner_ (minerbedrock@gmail.com)
 *
 * Last Modified: 15.04.2015
 */
package com.chaosbuffalo.mkultra.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Predicate;


public class RayTraceUtils {

    public static RayTraceResult getLookingAt(final Entity mainEntity, double distance) {
        return getLookingAt(mainEntity, distance, e -> e != mainEntity);
    }

    private static Predicate<Entity> defaultFilter = e -> EntitySelectors.IS_ALIVE.apply(e) && EntitySelectors.NOT_SPECTATING.apply(e);


    public static Vec3d getPerpendicular(Vec3d vec){
        Vec3d cVec;
        if (vec.y != 0 || vec.z != 0){
            cVec = new Vec3d(1, 0, 0);
        } else{
            cVec = new Vec3d(0, 1, 0);
        }
        return vec.crossProduct(cVec);
    }

    public static List<Entity> getEntitiesInLine(final Entity mainEntity, Vec3d from, Vec3d to, Vec3d expansion,
                                                 float growth, final Predicate<Entity> filter) {
        Predicate<Entity> predicate = input -> defaultFilter.test(input) && filter.test(input);
        AxisAlignedBB bb = new AxisAlignedBB(new BlockPos(from), new BlockPos(to))
                .expand(expansion.x, expansion.y, expansion.z)
                .grow(growth);
        List<Entity> entities = mainEntity.getEntityWorld().getEntitiesWithinAABB(Entity.class, bb, predicate::test);
        return entities;
    }

    public static RayTraceResult getLookingAt(final Entity mainEntity, double distance, final Predicate<Entity> entityPredicate) {

        Predicate<Entity> finalFilter = e -> e != mainEntity &&
                defaultFilter.test(e) &&
                e.canBeCollidedWith() &&
                entityPredicate.test(e);

        RayTraceResult position = null;

        if (mainEntity.world != null) {
            Vec3d look = mainEntity.getLookVec().scale(distance);
            Vec3d from = mainEntity.getPositionVector().addVector(0, mainEntity.getEyeHeight(), 0);
            Vec3d to = from.add(look);
            position = rayTraceBlocksAndEntities(mainEntity.world, from, to, false, finalFilter);
        }
        return position;
    }

    public static RayTraceResult rayTraceBlocks(World world, Vec3d from, Vec3d to, boolean stopOnLiquid) {
        return world.rayTraceBlocks(from, to, stopOnLiquid, true, false);
    }


    public static RayTraceResult rayTraceEntities(World world, Vec3d from, Vec3d to, Vec3d aaExpansion, float aaGrowth,
                                                  float entityExpansion, final Predicate<Entity> filter) {

        Predicate<Entity> predicate = input -> defaultFilter.test(input) && filter.test(input);

        Entity nearest = null;
        double distance = 0;

        AxisAlignedBB bb = new AxisAlignedBB(new BlockPos(from), new BlockPos(to))
                .expand(aaExpansion.x, aaExpansion.y, aaExpansion.z)
                .grow(aaGrowth);
        List<Entity> entities = world.getEntitiesWithinAABB(Entity.class, bb, predicate::test);
        for (Entity entity : entities) {
            AxisAlignedBB entityBB = entity.getEntityBoundingBox().grow(entityExpansion);
            RayTraceResult intercept = entityBB.calculateIntercept(from, to);
            if (intercept != null) {
                double dist = from.distanceTo(intercept.hitVec);
                if (dist < distance || distance == 0.0D) {
                    nearest = entity;
                    distance = dist;
                }
            }
        }
        if (nearest != null)
            return new RayTraceResult(nearest);
        return null;
    }

    private static RayTraceResult rayTraceBlocksAndEntities(World world, Vec3d from, Vec3d to, boolean stopOnLiquid,
                                                            final Predicate<Entity> entityFilter) {
        RayTraceResult block = rayTraceBlocks(world, from, to, stopOnLiquid);
        if (block != null)
            to = block.hitVec;
        RayTraceResult entity = rayTraceEntities(world, from, to, new Vec3d(0, 0, 0), 1.5f, .5f, entityFilter);

        if (block == null) {
            if (entity == null) {
                return null;
            } else {
                return entity;
            }
        } else {
            if (entity == null) {
                return block;
            } else {
                double blockDist = block.hitVec.distanceTo(from);
                double entityDist = entity.hitVec.distanceTo(from);
                if (blockDist < entityDist) {
                    return block;
                } else {
                    return entity;
                }
            }
        }
    }
}
