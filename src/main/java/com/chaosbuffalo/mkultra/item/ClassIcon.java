package com.chaosbuffalo.mkultra.item;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IClassProvider;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.network.ModGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Created by Jacob on 3/15/2016.
 */
public class ClassIcon extends Item implements IClassProvider {

    private final String iconText;
    private final ResourceLocation classListId;

    public ClassIcon(String unlocalizedName, String iconText, int maxDamageIn, ResourceLocation classListId) {
        super();
        this.setTranslationKey(unlocalizedName);
        this.setCreativeTab(MKUltra.MKULTRA_TAB);
        this.iconText = iconText;
        this.classListId = classListId;
        this.setMaxDamage(maxDamageIn);
        this.setMaxStackSize(1);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        if (worldIn.isRemote && hand == EnumHand.MAIN_HAND) {
            IPlayerData data = MKUPlayerData.get(playerIn);
            if (data != null && !data.hasChosenClass()) {
                playerIn.openGui(MKUltra.INSTANCE, ModGuiHandler.LEARN_CLASS_SCREEN, worldIn,
                        (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
            }
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, playerIn.getHeldItem(hand));
    }

    @Override
    public ResourceLocation getIdentity() {
        return classListId;
    }

    @Override
    public String getClassSelectionText() {
        return iconText;
    }

}
