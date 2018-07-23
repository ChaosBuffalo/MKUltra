package com.chaosbuffalo.mkultra.blocks;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.ModBlocks;
import com.chaosbuffalo.mkultra.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;


public class HempBlock extends BlockCrops implements IGrowable {
    private static final PropertyInteger HEMP_AGE = PropertyInteger.create("age", 0, 2);
    private static final AxisAlignedBB HEMP_AABB = new AxisAlignedBB(
            0.125D, 0.0D, 0.125D, 0.875D, 1.0D, 0.875D);

    private final int MAX_HEIGHT = 4;

    public HempBlock() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(this.getAgeProperty(), 0));
        this.setTickRandomly(true);
        this.setCreativeTab(CreativeTabs.MATERIALS);
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
        this.disableStats();
        this.setUnlocalizedName("hempBlock");
        this.setRegistryName(MKUltra.MODID, "hempBlock");
    }


    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return HEMP_AABB;
    }

    @Override
    public int getMaxAge() {
        return 2;
    }

    @Nonnull
    @Override
    protected PropertyInteger getAgeProperty() {
        return HEMP_AGE;
    }

    private int calculateHeight(World worldIn, BlockPos pos) {
        int count = 0;

        while (worldIn.getBlockState(pos).getBlock() == this) {
            count++;
            pos = pos.down();
        }

        return count;
    }


    private boolean isFullGrown(IBlockState state, World worldIn, BlockPos pos) {
        int count = calculateHeight(worldIn, pos);
        return isMaxAge(state) && count >= MAX_HEIGHT;
    }

    public void updateTick(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, @Nonnull Random rand) {
//        if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (worldIn.getLightFromNeighbors(pos.up()) >= 9) {
            int age = this.getAge(state);

            if (age <= this.getMaxAge()) {
                float f = getGrowthChance(this, worldIn, pos);
                if (rand.nextInt((int) (4.0F / f) + 1) == 0) {
                    this.grow(worldIn, pos, state);
                }
            }
        }
    }

    public void grow(World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state) {
        int age = getAge(state);
        if (age < getMaxAge()) {
            worldIn.setBlockState(pos, this.withAge(age + 1), 2);
        }

        if (worldIn.isAirBlock(pos.up())) {
            int count = calculateHeight(worldIn, pos);
            if (count < MAX_HEIGHT && age == getMaxAge()) {
                worldIn.setBlockState(pos.up(), this.getDefaultState());
            }
        }
    }

    protected static float getGrowthChance(Block blockIn, World worldIn, BlockPos pos) {
        float f = 1.0F;
        if (worldIn.getBlockState(pos.east()).getBlock() == ModBlocks.hempBlock) {
            f -= 0.24f;
        }
        if (worldIn.getBlockState(pos.west()).getBlock() == ModBlocks.hempBlock) {
            f -= 0.24f;
        }
        if (worldIn.getBlockState(pos.north()).getBlock() == ModBlocks.hempBlock) {
            f -= 0.24f;
        }
        if (worldIn.getBlockState(pos.south()).getBlock() == ModBlocks.hempBlock) {
            f -= 0.24f;
        }
        return f;
    }

    @Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        IBlockState under = worldIn.getBlockState(pos.down());
        Block underBlock = under.getBlock();
        return underBlock == ModBlocks.hempBlock ||
                (worldIn.getLight(pos) >= 8 || worldIn.canSeeSky(pos)) &&
                        underBlock.canSustainPlant(under, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this) &&
                        underBlock.isFertile(worldIn, pos.down());
    }

    @Nonnull
    @Override
    protected Item getSeed() {
        return Items.WHEAT_SEEDS;
    }

    @Nonnull
    @Override
    protected Item getCrop() {
        return Items.WHEAT;
    }

    @Override
    public void getDrops(@Nonnull NonNullList<ItemStack> drops, @Nonnull IBlockAccess world, @Nonnull BlockPos pos, @Nonnull IBlockState state, int fortune) {
        int age = getAge(state);
        if (age >= getMaxAge()) {
            int lootRounds = 3 + fortune;
            for (int i = 0; i < lootRounds; ++i) {
                if (RANDOM.nextInt(4 * getMaxAge()) <= age) {
                    drops.add(new ItemStack(this.getSeed(), 1, 0));
                }
            }

            // bonus crop if at max age
            drops.add(new ItemStack(this.getCrop(), 1));
        }
    }

    @Override
    public void dropBlockAsItemWithChance(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, float chance, int fortune) {
        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, 0);
    }

    /**
     * Whether this IGrowable can grow
     */
    @Override
    public boolean canGrow(@Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull IBlockState state, boolean isClient) {
        return !this.isFullGrown(state, worldIn, pos);
    }

    @Nonnull
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, HEMP_AGE);
    }
}
