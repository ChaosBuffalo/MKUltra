package com.chaosbuffalo.mkultra.item;


import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.network.packets.client.ResurrectPlayerPacket;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

/**
 * Created by Jacob on 4/7/2018.
 */
public class PhoenixDust extends Item {

    public static float RANGE = 10.0f;

    public PhoenixDust(String unlocalizedName) {
        super();
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(MKUltra.MKULTRA_TAB);
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, @Nonnull EnumHand hand) {
        if (worldIn.isRemote) {
            MKUltra.packetHandler.sendToServer(new ResurrectPlayerPacket());
        }

        return super.onItemRightClick(worldIn, playerIn, hand);
    }
}
