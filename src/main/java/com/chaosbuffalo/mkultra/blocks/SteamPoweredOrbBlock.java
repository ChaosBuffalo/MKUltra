package com.chaosbuffalo.mkultra.blocks;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.tiles.SteamPoweredOrbTileEntity;
import cyano.poweradvantage.api.ConduitType;
import cyano.poweradvantage.api.ITypedConduit;
import cyano.poweradvantage.api.PowerConnectorContext;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * Created by Jacob on 8/8/2016.
 */

public class SteamPoweredOrbBlock extends Block implements ITypedConduit, ITileEntityProvider {
    public static final PropertyInteger POWERED_STATE = PropertyInteger.create("poweredstate", 0, 7);
    public static final int GROUNDED = 0;
    public static final int CHARGING_1 = 1;
    public static final int CHARGING_2 = 2;
    public static final int CHARGING_3 = 3;
    public static final int CHARGING_4 = 4;
    public static final int CHARGING_5 = 5;
    public static final int CHARGING_6 = 6;
    public static final int CHARGING_7 = 7;

    protected static final AxisAlignedBB AABB_PEDASTAL_BOT = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, .0625D, 1.0D);
    protected static final AxisAlignedBB AABB_PEDASTAL_MID = new AxisAlignedBB(.0625D, .0625D, .0625D, .9375D, .125D, .9375D);
    protected static final AxisAlignedBB AABB_ORB_BOT = new AxisAlignedBB(.375D, .125D, .375D, .625D, .1875D, .625D);
    protected static final AxisAlignedBB AABB_ORB_MIDBOT = new AxisAlignedBB(.3125D, .1875D, .3125D, .6875D, .25D, .6875D);
    protected static final AxisAlignedBB AABB_ORB_MID = new AxisAlignedBB(.25D, .25D, .25D, .75D, .5D, .75D);
    protected static final AxisAlignedBB AABB_ORB_MIDTOP = new AxisAlignedBB(.3125D, .5D, .3125D, .6875D, .5625D, .6875D);
    protected static final AxisAlignedBB AABB_ORB_TOP = new AxisAlignedBB(.375D, .5625D, .375D, .625D, .625D, .625D);

    private final ConduitType[] type = {new ConduitType("steam")};

    public SteamPoweredOrbBlock(String unlocalizedName, Material material, float hardness, float resistance) {
        super(material);
        this.setDefaultState(this.blockState.getBaseState().withProperty(this.getPoweredStateProperty(),
                0));
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.MISC);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setRegistryName(MKUltra.MODID, "steamPoweredOrbBlock");
    }

    public SteamPoweredOrbBlock(String unlocalizedName, float hardness, float resistance) {
        this(unlocalizedName, Material.IRON, hardness, resistance);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    private double getHeightFromState(IBlockState state) {
        return getPoweredState(state) * .0625;
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
        double offset = getHeightFromState(state);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_PEDASTAL_BOT);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_PEDASTAL_MID);
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_ORB_BOT.offset(0.0, offset, 0.0));
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_ORB_MIDBOT.offset(0.0, offset, 0.0));
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_ORB_MID.offset(0.0, offset, 0.0));
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_ORB_MIDTOP.offset(0.0, offset, 0.0));
        addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB_ORB_TOP.offset(0.0, offset, 0.0));
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return FULL_BLOCK_AABB;
    }


    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World world, BlockPos pos, Random rand) {
        SteamPoweredOrbTileEntity portalEntity = (SteamPoweredOrbTileEntity) world.getTileEntity(pos);
        float steamEnergy = portalEntity.getEnergy(this.getTypes()[0]);


        if (steamEnergy >= 800.0f && world.rand.nextInt(5) >= 4) {

            this.spawnParticles(world, pos, stateIn);
        }
    }

    private void spawnParticles(World worldIn, BlockPos pos, IBlockState state) {
        ParticleEffects.spawnParticleEffect(
                EnumParticleTypes.FIREWORKS_SPARK.getParticleID(), ParticleEffects.CIRCLE_MOTION, 0, 0.25, 10,
                new Vec3d(pos.getX(), pos.getY(), pos.getZ()).add(new Vec3d(0.5, getHeightFromState(state), 0.5)),
                new Vec3d(.25, .25, .25), new Vec3d(1.0, 1.0, 1.0), worldIn);
    }


    @Override
    public SteamPoweredOrbTileEntity createNewTileEntity(World worldIn, int meta) {
        return new SteamPoweredOrbTileEntity();
    }

    @Override
    public boolean canAcceptConnection(PowerConnectorContext powerConnectorContext) {
        ConduitType[] myTypes = this.getTypes();

        for (ConduitType myType : myTypes) {
            if (ConduitType.areSameType(myType, powerConnectorContext.powerType) &&
                    powerConnectorContext.otherBlockSide == EnumFacing.UP) {
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

    protected PropertyInteger getPoweredStateProperty() {
        return POWERED_STATE;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.withPoweredState(meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return this.getPoweredState(state);
    }


    protected int getPoweredState(IBlockState state) {
        return state.getValue(this.getPoweredStateProperty());
    }

    public IBlockState withPoweredState(int poweredState) {
        return this.getDefaultState().withProperty(this.getPoweredStateProperty(), poweredState);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, POWERED_STATE);
    }
}

