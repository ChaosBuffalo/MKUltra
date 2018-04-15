package com.chaosbuffalo.mkultra.item;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import static com.chaosbuffalo.mkultra.item.ItemHelper.findSmokeable;

/**
 * Created by Jacob on 4/10/2016.
 */
public class Pipe extends Item {

    static final int COOLDOWN = 10 * GameConstants.TICKS_PER_SECOND;

    public Pipe(String unlocalizedName) {
        super();
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(CreativeTabs.MATERIALS);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn,
                                                    EntityPlayer playerIn, EnumHand hand) {
        if (!worldIn.isRemote) {
            IPlayerData data = MKUPlayerData.get(playerIn);
            if (data == null || !data.hasChosenClass()) {
                return super.onItemRightClick(worldIn, playerIn, hand);
            }

            int currentMana = data.getMana();
            int augMax = data.getTotalMana() + 10;
            ItemStack smokeable = findSmokeable(playerIn);
            if (!smokeable.isEmpty() && currentMana < augMax){
                data.setMana(Math.min(augMax, currentMana + 10));
                ItemHelper.shrinkStack(playerIn, smokeable, 1);

                playerIn.getCooldownTracker().setCooldown(this, COOLDOWN);
            }
        }
        return super.onItemRightClick(worldIn, playerIn, hand);
    }
}