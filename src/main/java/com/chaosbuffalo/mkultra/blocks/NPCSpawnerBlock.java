package com.chaosbuffalo.mkultra.blocks;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.tiles.TileEntityNPCSpawner;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class NPCSpawnerBlock extends Block implements ITileEntityProvider {

    public static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);

    public NPCSpawnerBlock(String unlocalizedName, Material material, float hardness, float resistance) {
        super(material);
        this.setTranslationKey(unlocalizedName);
        this.setCreativeTab(MKUltra.MKULTRA_TAB);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setRegistryName(MKUltra.MODID, unlocalizedName);
    }

    public NPCSpawnerBlock(String unlocalizedName, float hardness, float resistance) {
        this(unlocalizedName, Material.IRON, hardness, resistance);
    }

    public NPCSpawnerBlock(String unlocalizedName) {
        this(unlocalizedName, 5.0f, 10.0f);
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BLOCK_AABB;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }


    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess access, BlockPos pos, IBlockState blockState, int p_getDrops_5_) {
        TileEntity tileEntity = access.getTileEntity(pos);

        if (tileEntity != null && tileEntity instanceof TileEntityNPCSpawner) {
            TileEntityNPCSpawner npcSpawner = (TileEntityNPCSpawner) tileEntity;
            ResourceLocation itemToDrop = npcSpawner.getItemToDrop();
            if (itemToDrop != null) {
                Item item = Item.REGISTRY.getObject(itemToDrop);
                if (item != null) {
                    ItemStack stack = new ItemStack(item);
                    stack.setTagCompound(npcSpawner.serializeForItem());
                    drops.add(stack);
                }
            }
            npcSpawner.cleanupMob();
        }
    }

    @Override
    public boolean removedByPlayer(IBlockState p_removedByPlayer_1_, World p_removedByPlayer_2_, BlockPos p_removedByPlayer_3_, EntityPlayer p_removedByPlayer_4_, boolean willHarvest) {
        if (willHarvest) return true;
        return super.removedByPlayer(p_removedByPlayer_1_, p_removedByPlayer_2_, p_removedByPlayer_3_, p_removedByPlayer_4_, willHarvest);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer p_harvestBlock_2_, BlockPos blockPos, IBlockState p_harvestBlock_4_, @Nullable TileEntity p_harvestBlock_5_, ItemStack p_harvestBlock_6_) {
        super.harvestBlock(world, p_harvestBlock_2_, blockPos, p_harvestBlock_4_, p_harvestBlock_5_, p_harvestBlock_6_);
        world.setBlockToAir(blockPos);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState p_breakBlock_3_) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity != null && tileEntity instanceof TileEntityNPCSpawner) {
            TileEntityNPCSpawner npcSpawner = (TileEntityNPCSpawner) tileEntity;
            npcSpawner.cleanupMob();
        }
        super.breakBlock(world, pos, p_breakBlock_3_);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityNPCSpawner();
    }
}
