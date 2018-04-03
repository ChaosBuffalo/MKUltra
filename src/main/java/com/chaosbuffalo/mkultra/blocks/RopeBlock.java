package com.chaosbuffalo.mkultra.blocks;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.ModBlocks;
import com.chaosbuffalo.mkultra.item.ItemHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Jacob on 4/9/2016.
 */


public class RopeBlock extends Block {
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    protected static final AxisAlignedBB ROPE_NO_ANCHOR_AABB = new AxisAlignedBB(
            0.375, 0.0, 0.375,
            0.625, 1.0, 0.625);
    protected static final AxisAlignedBB ROPE_NORTH_AABB = new AxisAlignedBB(
            0.375, 0.0, 0.375,
            0.625, 0.625, 1.0);
    protected static final AxisAlignedBB ROPE_SOUTH_AABB = new AxisAlignedBB(
            0.375, 0.0, 0.0,
            0.625, 0.625, 0.625);
    protected static final AxisAlignedBB ROPE_WEST_AABB = new AxisAlignedBB(
            0.375, 0.0, 0.375,
            1.0, 0.625, 0.625);
    protected static final AxisAlignedBB ROPE_EAST_AABB = new AxisAlignedBB(
            0.0, 0.0, 0.375,
            0.625, 0.625, 0.625);
    protected static final AxisAlignedBB ROPE_UP_ANCHOR_AABB = new AxisAlignedBB(
            0.375, 0.0, 0.375,
            0.625, 1.0, 0.625);

    public RopeBlock() {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.DECORATIONS);
        this.setUnlocalizedName("ropeBlock");
        this.setRegistryName(MKUltra.MODID, "ropeBlock");
    }


    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        switch (state.getValue(FACING)) {
            case EAST:
                return ROPE_EAST_AABB;
            case WEST:
                return ROPE_WEST_AABB;
            case SOUTH:
                return ROPE_SOUTH_AABB;
            case NORTH:
                return ROPE_NORTH_AABB;
            case DOWN:
                return ROPE_UP_ANCHOR_AABB;
            case UP:
            default:
                return ROPE_NO_ANCHOR_AABB;
        }
    }


    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity) {
        return true;
    }

    private boolean canPlaceOn(World worldIn, BlockPos pos) {
        IBlockState state = worldIn.getBlockState(pos.up());
        return state.isSideSolid(worldIn, pos.up(), EnumFacing.DOWN);
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        for (EnumFacing enumfacing : FACING.getAllowedValues()) {
            if (this.canPlaceAt(worldIn, pos, enumfacing)) {
                return true;
            }
        }

        return false;
    }

    public boolean isRopeAbove(World worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.up()).getBlock().equals(ModBlocks.ropeBlock);
    }

    private boolean canPlaceAt(World worldIn, BlockPos pos, EnumFacing facing) {

        BlockPos blockpos = pos.offset(facing.getOpposite());
        boolean flag = facing.getAxis().isHorizontal();
        return flag && worldIn.isSideSolid(blockpos, facing, true) || canPlaceOn(worldIn, pos)
                || isRopeAbove(worldIn, pos);
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (!worldIn.isRemote) {
            ItemStack heldItem = playerIn.getHeldItem(hand);
            Block blockAt = state.getBlock();

            if (Block.getBlockFromItem(heldItem.getItem()).equals(ModBlocks.ropeBlock)) {
                int i = 0;
                while (blockAt.equals(ModBlocks.ropeBlock)) {
                    i += 1;
                    blockAt = worldIn.getBlockState(pos.down(i)).getBlock();
                }
                if (worldIn.getBlockState(pos.down(i)).getBlock().equals(Blocks.AIR)) {
                    worldIn.setBlockState(pos.down(i), getDefaultState());

                    ItemHelper.shrinkStack(playerIn, heldItem, 1);
                }
            }
        }
        return true;
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {

        if (isRopeAbove(worldIn, pos)) {
            return this.getDefaultState().withProperty(FACING, EnumFacing.UP);
        } else if (this.canPlaceAt(worldIn, pos, facing)) {
            return this.getDefaultState().withProperty(FACING, facing);
        } else {
            for (EnumFacing enumfacing : EnumFacing.Plane.HORIZONTAL) {
                if (worldIn.isSideSolid(pos.offset(enumfacing.getOpposite()), enumfacing, true)) {
                    return this.getDefaultState().withProperty(FACING, enumfacing);
                }
            }

            return this.getDefaultState();
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.checkForDrop(worldIn, pos, state);
    }

    /**
     * Called when a neighboring block changes.
     */
    @Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
        this.onNeighborChangeInternal(worldIn, pos, worldIn.getBlockState(pos));
    }

    protected boolean onNeighborChangeInternal(World worldIn, BlockPos pos, IBlockState state) {
        if (!this.checkForDrop(worldIn, pos, state)) {
            return true;
        } else {
            EnumFacing enumfacing = state.getValue(FACING);
            EnumFacing.Axis axis = enumfacing.getAxis();
            boolean flag = false;

            if (axis.isHorizontal() && !worldIn.isSideSolid(pos.offset(enumfacing.getOpposite()), enumfacing, true)) {
                flag = true;
            } else if (axis.isVertical() && !this.canPlaceAt(worldIn, pos, enumfacing)) {
                flag = true;
            }

            if (flag) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
                return true;
            } else {
                return false;
            }
        }
    }

    protected boolean checkForDrop(World worldIn, BlockPos pos, IBlockState state) {
        if (state.getBlock() == this && this.canPlaceAt(worldIn, pos, state.getValue(FACING))) {
            return true;
        } else {
            if (worldIn.getBlockState(pos).getBlock() == this) {
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }

            return false;
        }
    }


    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState iblockstate = this.getDefaultState();

        switch (meta) {
            case 1:
                iblockstate = iblockstate.withProperty(FACING, EnumFacing.EAST);
                break;
            case 2:
                iblockstate = iblockstate.withProperty(FACING, EnumFacing.WEST);
                break;
            case 3:
                iblockstate = iblockstate.withProperty(FACING, EnumFacing.SOUTH);
                break;
            case 4:
                iblockstate = iblockstate.withProperty(FACING, EnumFacing.NORTH);
                break;
            case 5:
                iblockstate = iblockstate.withProperty(FACING, EnumFacing.DOWN);
                break;
            case 6:
            default:
                iblockstate = iblockstate.withProperty(FACING, EnumFacing.UP);
        }

        return iblockstate;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(IBlockState state) {
        int i = 0;

        switch (state.getValue(FACING)) {
            case EAST:
                i = i | 1;
                break;
            case WEST:
                i = i | 2;
                break;
            case SOUTH:
                i = i | 3;
                break;
            case NORTH:
                i = i | 4;
                break;
            case DOWN:
                i = i | 5;
                break;
            case UP:
            default:
                i = i | 6;
        }

        return i;
    }

    /**
     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    /**
     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
     * blockstate.
     */
    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn) {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING);
    }
}