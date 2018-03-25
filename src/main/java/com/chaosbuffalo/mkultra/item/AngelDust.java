package com.chaosbuffalo.mkultra.item;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerDataProvider;
import com.chaosbuffalo.mkultra.item.interfaces.IClassProvider;
import com.chaosbuffalo.mkultra.network.ModGuiHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;


public class AngelDust extends Item implements IClassProvider {

    public AngelDust(String unlocalizedName) {
        super();
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.MATERIALS);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (worldIn.isRemote && hand == EnumHand.MAIN_HAND) {
            IPlayerData data = PlayerDataProvider.get(playerIn);
            if (data != null) {
                playerIn.openGui(MKUltra.INSTANCE, ModGuiHandler.CHANGE_CLASS_SCREEN, worldIn,
                        (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
            }
        }
        return super.onItemRightClick(worldIn, playerIn, hand);
    }

    @Override
    public String getClassSelectionText() {
        return "Select your next class";
    }
}
