package com.chaosbuffalo.mkultra.item;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IClassProvider;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.chaosbuffalo.mkultra.network.ModGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.List;


public class DiamondDust extends Item implements IClassProvider {

    public DiamondDust(String unlocalizedName) {
        super();
        this.setTranslationKey(unlocalizedName);
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
        return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
    }

    @Override
    public ResourceLocation getIdentity() {
        return getRegistryName();
    }

    @Override
    public String getClassSelectionText() {
        return "Select your next class";
    }

    @Nonnull
    @Override
    public List<ResourceLocation> getClasses() {
        return MKURegistry.getAllClasses();
    }
}
