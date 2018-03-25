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

/**
 * Created by Jacob on 3/15/2016.
 */
public class ClassIcon extends Item implements IClassProvider {

    public String iconText;

    public ClassIcon(String unlocalizedName, String iconText, int maxDamageIn) {
        super();
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.MATERIALS);
        this.iconText = iconText;
        this.setMaxDamage(maxDamageIn);
        this.setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (worldIn.isRemote && hand == EnumHand.MAIN_HAND) {
            IPlayerData data = PlayerDataProvider.get(playerIn);
            if (data != null && !data.hasChosenClass()) {
                playerIn.openGui(MKUltra.INSTANCE, ModGuiHandler.LEARN_CLASS_SCREEN, worldIn,
                        (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
            }
        }
        return super.onItemRightClick(worldIn, playerIn, hand);
    }

    @Override
    public String getClassSelectionText() {
        return iconText;
    }
}