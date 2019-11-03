package com.chaosbuffalo.mkultra.utils;

import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Jacob on 7/1/2018.
 */
public class EnvironmentUtils {

    public static final Set<Block> FIRE_BLOCKS = new HashSet<>();

    public static void addFireBlock(Block block) {
        FIRE_BLOCKS.add(block);
    }

    public static void putOutFiresForEntities(EntityLivingBase caster, BlockPos center, Vec3i size){
        Vec3i half = new Vec3i(size.getX() / 2, size.getY() / 2,
                size.getZ() / 2);
        AxisAlignedBB boundingBox = new AxisAlignedBB(center.subtract(half), center.add(half));
        List<EntityLivingBase> entities = caster.getEntityWorld().getEntitiesWithinAABB(EntityLivingBase.class,
                boundingBox, e -> Targeting.isValidTarget(Targeting.TargetType.FRIENDLY,
                        caster, e, false));
        for (EntityLivingBase entity : entities){
            entity.extinguish();
        }
    }

    public static void putoutFiresForEntitiesInLine(EntityLivingBase caster, Vec3d start, Vec3d end){
        List<EntityLivingBase> entities = RayTraceUtils.getEntitiesInLine(
                EntityLivingBase.class, caster,
                start, end, Vec3d.ZERO, 1.0f,
                e -> Targeting.isValidTarget(Targeting.TargetType.FRIENDLY,
                        caster, e, false));
        for (EntityLivingBase entity : entities){
            entity.extinguish();
        }
    }

    public static void putOutFires(EntityLivingBase caster, BlockPos center, Vec3i size) {
        World world = caster.getEntityWorld();
        BlockPos start_look = center.subtract(new Vec3i(size.getX() / 2,
                size.getY() / 2, size.getZ() / 2));
        for (int x = 0; x < size.getX(); x++) {
            for (int y = 0; y < size.getY(); y++) {
                for (int z = 0; z < size.getZ(); z++) {
                    BlockPos new_pos = start_look.add(x, y, z);
                    if (FIRE_BLOCKS.contains(world.getBlockState(new_pos).getBlock())) {
                        world.setBlockToAir(new_pos);
                    }
                }
            }
        }
        putOutFiresForEntities(caster, center, size);
    }

    public static void putOutFiresInLine(EntityLivingBase caster, Vec3d start, Vec3d end) {
        World world = caster.getEntityWorld();
        Vec3d diff = end.subtract(start);
        Vec3d step = diff.normalize().scale(.5);
        for (int i = 0; i <= Math.ceil(diff.length()) * 2; i++) {
            Vec3d loc = start.add(step.scale(i));
            BlockPos pos = new BlockPos(loc);
            if (FIRE_BLOCKS.contains(world.getBlockState(pos).getBlock())) {
                world.setBlockToAir(pos);
            }
        }
        putoutFiresForEntitiesInLine(caster, start, end);
    }
}
