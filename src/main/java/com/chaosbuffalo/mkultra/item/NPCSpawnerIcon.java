package com.chaosbuffalo.mkultra.item;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.ModBlocks;
import com.chaosbuffalo.mkultra.tiles.TileEntityNPCSpawner;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NPCSpawnerIcon extends Item{
    private ResourceLocation mobName;

    public NPCSpawnerIcon(String unlocalizedName, ResourceLocation mobName) {
        super();
        this.setTranslationKey(unlocalizedName);
        this.setCreativeTab(MKUltra.MKULTRA_TAB);
        this.setMaxStackSize(1);
        this.mobName = mobName;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos blockPos, EnumHand hand, EnumFacing facing, float p_onItemUse_6_, float p_onItemUse_7_, float p_onItemUse_8_) {
        ItemStack stack = player.getHeldItem(hand);
        NBTTagCompound nbt;
        if (stack.hasTagCompound())
        {
            nbt = stack.getTagCompound();
        } else {
            nbt = new NBTTagCompound();
            nbt.setString("mobName", mobName.toString());
            nbt.setString("itemToDrop", this.getRegistryName().toString());
        }
        world.setBlockState(blockPos.up(), ModBlocks.npcSpawnerBlock.getDefaultState());
        TileEntity tileEntity = world.getTileEntity(blockPos.up());
        if (tileEntity instanceof TileEntityNPCSpawner){
            TileEntityNPCSpawner npcSpawner = (TileEntityNPCSpawner) tileEntity;
            npcSpawner.readFromNBTItem(nbt);
        }
        player.setHeldItem(hand, ItemStack.EMPTY);
        return EnumActionResult.SUCCESS;
    }


}
