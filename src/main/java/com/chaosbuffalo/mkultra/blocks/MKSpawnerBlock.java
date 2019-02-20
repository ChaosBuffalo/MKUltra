package com.chaosbuffalo.mkultra.blocks;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.chaosbuffalo.mkultra.network.ModGuiHandler;
import com.chaosbuffalo.mkultra.tiles.TileEntityMKSpawner;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MKSpawnerBlock extends Block implements ITileEntityProvider {

    public static final AxisAlignedBB BLOCK_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.0625D, 1.0D);

    public MKSpawnerBlock(String unlocalizedName, Material material, float hardness, float resistance) {
        super(material);
        this.setTranslationKey(unlocalizedName);
        this.setCreativeTab(MKUltra.MKULTRA_TAB);
        this.setHardness(hardness);
        this.setResistance(resistance);
        this.setRegistryName(MKUltra.MODID, unlocalizedName);
    }

    public MKSpawnerBlock(String unlocalizedName, float hardness, float resistance) {
        this(unlocalizedName, Material.ANVIL, hardness, resistance);
    }

    public MKSpawnerBlock(String unlocalizedName) {
        this(unlocalizedName, 10.0f, 10.0f);
    }

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state)
    {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return BLOCK_AABB;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state,
                                    EntityPlayer player, EnumHand hand,
                                    EnumFacing side,
                                    float hitX, float hitY, float hitZ) {

        if (player.isCreative()) {
            TileEntity entity = world.getTileEntity(pos);
            if (entity instanceof TileEntityMKSpawner) {
                TileEntityMKSpawner mkSpawner = (TileEntityMKSpawner)entity;
                mkSpawner.sync();
                player.openGui(MKUltra.INSTANCE, ModGuiHandler.MK_SPAWNER_SCREEN, player.world,
                        pos.getX(), pos.getY(), pos.getZ());
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileEntityMKSpawner();
    }
}
