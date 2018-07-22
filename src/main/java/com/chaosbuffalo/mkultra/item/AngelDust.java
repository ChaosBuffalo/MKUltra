package com.chaosbuffalo.mkultra.item;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
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


public class AngelDust extends Item implements IClassProvider {

    public AngelDust(String unlocalizedName) {
        super();
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(MKUltra.MKULTRA_TAB);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (worldIn.isRemote && hand == EnumHand.MAIN_HAND) {
            IPlayerData data = MKUPlayerData.get(playerIn);
            if (data != null) {
                playerIn.openGui(MKUltra.INSTANCE, ModGuiHandler.CHANGE_CLASS_SCREEN, worldIn,
                        (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
            }
        }
        return super.onItemRightClick(worldIn, playerIn, hand);
    }

    @Override
    public ResourceLocation getIconForProvider() {
        return new ResourceLocation(MKUltra.MODID, "textures/items/sun_icon.png");
    }

    @Override
    public String getClassSelectionText() {
        return "Select your next class";
    }

    @Override
    public String getXpTableText() {
        return "You shouldn't see this.";
    }

    @Override
    public ResourceLocation getXpTableBackground() {
        return new ResourceLocation(MKUltra.MODID, "textures/gui/xp_table_background_moon.png");
    }

    @Override
    public int getXpTableTextColor() {
        return 38600;
    }
}
