package com.chaosbuffalo.mkultra.utils;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jacob on 7/1/2018.
 */
public class EnvironmentUtils {

    public static final Set<Block> FIRE_BLOCKS = new HashSet<>();

    public static void addFireBlock(Block block) {
        FIRE_BLOCKS.add(block);
    }

    public static void putOutFires(World worldIn, BlockPos center, Vec3i size) {
        BlockPos start_look = center.subtract(new Vec3i(size.getX() / 2, size.getY() / 2, size.getZ() / 2));
        for (int x = 0; x < size.getX(); x++){
            for (int y = 0; y < size.getY(); y++){
                for (int z = 0; z < size.getZ(); z++){
                    BlockPos new_pos = start_look.add(x, y, z);
                    if (FIRE_BLOCKS.contains(worldIn.getBlockState(new_pos).getBlock())){
                        worldIn.setBlockToAir(new_pos);
                    }
                }
            }
        }
    }

    public static void putOutFiresInLine(World worldIn, Vec3d start, Vec3d end) {

        Vec3d diff = end.subtract(start);
        Vec3d step = diff.normalize().scale(.5);
        for (int i = 0; i <= Math.ceil(diff.length()) * 2; i++){
            Vec3d loc = start.add(step.scale(i));
            BlockPos pos = new BlockPos(loc);
            if (FIRE_BLOCKS.contains(worldIn.getBlockState(pos).getBlock())){
                worldIn.setBlockToAir(pos);
            }
        }
    }
}
