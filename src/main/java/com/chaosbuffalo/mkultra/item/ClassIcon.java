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
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Created by Jacob on 3/15/2016.
 */
public class ClassIcon extends Item implements IClassProvider {

    private final String iconText;
    private final ResourceLocation iconLoc;
    private final String xpTableText;
    private final ResourceLocation xpTableBackground;
    private final int xpTableTextColor;

    public ClassIcon(String unlocalizedName, String iconText, int maxDamageIn, ResourceLocation iconLoc,
                     String xpTableText, ResourceLocation xpTableBackground, int xpTableTextColor) {
        super();
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.MATERIALS);
        this.iconText = iconText;
        this.iconLoc = iconLoc;
        this.xpTableText = xpTableText;
        this.xpTableBackground = xpTableBackground;
        this.xpTableTextColor = xpTableTextColor;
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
    public String getXpTableText() {
        return xpTableText;
    }

    @Override
    public ResourceLocation getIconForProvider() {
        return iconLoc;
    }

    @Override
    public String getClassSelectionText() {
        return iconText;
    }

    @Override
    public ResourceLocation getXpTableBackground() {
        return xpTableBackground;
    }

    @Override
    public int getXpTableTextColor() {
        return xpTableTextColor;
    }

}