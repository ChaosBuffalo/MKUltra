package com.chaosbuffalo.mkultra.item;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.utils.EnvironmentUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;


/**
 * Created by Jacob on 7/1/2018.
 */
public class FireExtinguisherFlask extends Item {

    // can't be public because this is an ObjectHolder

    public static int rangeX = 16;
    public static int rangeY = 8;
    public static int rangeZ = 16;

    public FireExtinguisherFlask() {
        super();
        this.setCreativeTab(MKUltra.MKULTRA_TAB);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn,
                                                    EntityPlayer playerIn, EnumHand hand) {
        ItemStack stack = playerIn.getHeldItem(hand);
        if (!worldIn.isRemote) {
            BlockPos player_pos = playerIn.getPosition();
            EnvironmentUtils.putOutFires(playerIn, player_pos, new Vec3i(rangeX, rangeY, rangeZ));
            ItemHelper.shrinkStack(playerIn, stack, 1);
        }
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }
}