package com.chaosbuffalo.mkultra.blocks;

import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.tiles.PortalTileEntity;
//import cyano.poweradvantage.api.*;
import com.chaosbuffalo.mkultra.tiles.PortalTileEntity;
import cyano.poweradvantage.api.ConduitType;
import cyano.poweradvantage.api.ITypedConduit;
import cyano.poweradvantage.api.PowerConnectorContext;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * Created by Jacob on 4/3/2016.
 */

public class PortalBlock extends Block implements ITypedConduit, ITileEntityProvider {
    private final ConduitType[] type = {new ConduitType("steam")};

    public PortalBlock(String unlocalizedName, Material material, float hardness, float resistance) {
        super(material);
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.TRANSPORTATION);
        this.setHardness(hardness);
        this.setResistance(resistance);
        setRegistryName(MKUltra.MODID, "portalBlock");
    }

    public PortalBlock(String unlocalizedName, float hardness, float resistance) {
        this(unlocalizedName, Material.ROCK, hardness, resistance);
    }


    @Override
    public PortalTileEntity createNewTileEntity(World world, int metaDataValue) {
        return new PortalTileEntity();
    }


    public PortalBlock(String unlocalizedName) {
        this(unlocalizedName, 2.0f, 10.0f);
    }

    private boolean isBlockThatCounts(Block block) {
        return (block.equals(Blocks.COBBLESTONE) || block.equals(Blocks.OBSIDIAN) ||
                block.equals(Blocks.GLOWSTONE) || block.equals(Blocks.GLASS));
    }

    private int valueForBlock(Block block) {
        if (block.equals(Blocks.COBBLESTONE)) {
            return 8;
        } else if (block.equals(Blocks.OBSIDIAN)) {
            return 64;
        } else if (block.equals(Blocks.GLASS)) {
            return 1;
        } else if (block.equals(Blocks.GLOWSTONE)) {
            return 128;
        } else {
            return 0;
        }
    }

    private int calculateValueAtBlockPos(World world, BlockPos pos) {
        IBlockState state = world.getBlockState(pos);
        int count = 0;
        while (isBlockThatCounts(state.getBlock())) {
            count += valueForBlock(state.getBlock());
            pos = pos.add(0, 1, 0);
            state = world.getBlockState(pos);
        }
        return count;
    }

    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World world, BlockPos pos, Random rand) {
        PortalTileEntity portalEntity = (PortalTileEntity) world.getTileEntity(pos);
        float steamEnergy = portalEntity.getEnergy(this.getTypes()[0]);
        int eastVal = calculateValueAtBlockPos(world, pos.add(2, 0, 0));
        int westVal = calculateValueAtBlockPos(world, pos.add(-2, 0, 0));
        int northVal = calculateValueAtBlockPos(world, pos.add(1, 0, 2));
        int southVal = calculateValueAtBlockPos(world, pos.add(-1, 0, 2));
        int upVal = calculateValueAtBlockPos(world, pos.add(1, 0, -2));
        int downVal = calculateValueAtBlockPos(world, pos.add(-1, 0, -2));
        int yDist = upVal - downVal;
        int xDist = eastVal - westVal;
        int zDist = northVal - southVal;
        int cost = Math.abs(xDist) + Math.abs(yDist) + Math.abs(zDist) * 5;
        float steamCost = 50.0f + cost;
        if (steamEnergy >= steamCost) {

            this.spawnParticles(world, pos);
        }
    }

    private void spawnParticles(World worldIn, BlockPos pos) {
        Random random = worldIn.rand;
        double d0 = 0.0625D;

        for (int i = 0; i < 6; ++i) {
            double d1 = (double) ((float) pos.getX() + random.nextFloat());
            double d2 = (double) ((float) pos.getY() + random.nextFloat());
            double d3 = (double) ((float) pos.getZ() + random.nextFloat());

            if (i == 0 && !worldIn.getBlockState(pos.up()).isOpaqueCube()) {
                d2 = (double) pos.getY() + d0 + 1.0D;
            }

            if (i == 1 && !worldIn.getBlockState(pos.down()).isOpaqueCube()) {
                d2 = (double) pos.getY() - d0;
            }

            if (i == 2 && !worldIn.getBlockState(pos.south()).isOpaqueCube()) {
                d3 = (double) pos.getZ() + d0 + 1.0D;
            }

            if (i == 3 && !worldIn.getBlockState(pos.north()).isOpaqueCube()) {
                d3 = (double) pos.getZ() - d0;
            }

            if (i == 4 && !worldIn.getBlockState(pos.east()).isOpaqueCube()) {
                d1 = (double) pos.getX() + d0 + 1.0D;
            }

            if (i == 5 && !worldIn.getBlockState(pos.west()).isOpaqueCube()) {
                d1 = (double) pos.getX() - d0;
            }

            if (d1 < (double) pos.getX() || d1 > (double) (pos.getX() + 1) || d2 < 0.0D || d2 > (double) (pos.getY() + 1) || d3 < (double) pos.getZ() || d3 > (double) (pos.getZ() + 1)) {
                worldIn.spawnParticle(EnumParticleTypes.PORTAL, d1, d2, d3, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
                                    EntityPlayer player, EnumHand hand,
                                    EnumFacing side,
                                    float hitX, float hitY, float hitZ) {
        if (!world.isRemote) {
            PortalTileEntity portalEntity = (PortalTileEntity) world.getTileEntity(pos);
            float steamEnergy = portalEntity.getEnergy(this.getTypes()[0]);
            int eastVal = calculateValueAtBlockPos(world, pos.add(2, 0, 0));
            int westVal = calculateValueAtBlockPos(world, pos.add(-2, 0, 0));
            int northVal = calculateValueAtBlockPos(world, pos.add(1, 0, 2));
            int southVal = calculateValueAtBlockPos(world, pos.add(-1, 0, 2));
            int upVal = calculateValueAtBlockPos(world, pos.add(1, 0, -2));
            int downVal = calculateValueAtBlockPos(world, pos.add(-1, 0, -2));
            int yDist = upVal - downVal;
            int xDist = eastVal - westVal;
            int zDist = northVal - southVal;
            int cost = Math.abs(xDist) + Math.abs(yDist) + Math.abs(zDist) * 5;
            float steamCost = 50.0f + cost;
            if (steamEnergy >= steamCost) {
                portalEntity.subtractEnergy(steamCost, this.getTypes()[0]);
                player.setPositionAndUpdate(pos.getX() + xDist, pos.getY() + yDist + 1,
                        pos.getZ() + zDist);
            }


            return true;
        }

        return true;
    }

    @Override
    public boolean canAcceptConnection(PowerConnectorContext powerConnectorContext) {
        ConduitType[] myTypes = this.getTypes();

        for (ConduitType myType : myTypes) {
            if (ConduitType.areSameType(myType, powerConnectorContext.powerType)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public ConduitType[] getTypes() {
        return this.type;
    }

    @Override
    public boolean isPowerSink(ConduitType conduitType) {
        ConduitType[] types = this.getTypes();
        for (ConduitType type1 : types) {
            if (ConduitType.areSameType(type1, conduitType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isPowerSource(ConduitType conduitType) {
        return false;
    }
}