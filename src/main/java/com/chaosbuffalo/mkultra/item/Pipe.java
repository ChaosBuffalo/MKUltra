package com.chaosbuffalo.mkultra.item;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.init.ModItems;
import com.chaosbuffalo.mkultra.network.ModGuiHandler;
import net.minecraft.block.BlockShulkerBox;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import static com.chaosbuffalo.mkultra.item.ItemHelper.findSmokeable;

/**
 * Created by Jacob on 4/10/2016.
 */
public class Pipe extends Item {

    static final int COOLDOWN = 10 * GameConstants.TICKS_PER_SECOND;

    public Pipe(String unlocalizedName) {
        super();
        this.setUnlocalizedName(unlocalizedName);
        this.setCreativeTab(MKUltra.MKULTRA_TAB);
        this.setMaxDamage(150);
    }

    @Override
    @Nullable
    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
        if( stack.getItem() == ModItems.pipe) {
            return new PipeProvider();
        }
        return null;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn,
                                                    EntityPlayer playerIn, EnumHand hand) {
        if (!worldIn.isRemote) {
            if (playerIn.isSneaking()){
                playerIn.openGui(MKUltra.INSTANCE, ModGuiHandler.PIPE_CONTAINER_SCREEN, worldIn,
                        (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
            } else {
                IPlayerData data = MKUPlayerData.get(playerIn);
                if (data == null || !data.hasChosenClass()) {
                    return super.onItemRightClick(worldIn, playerIn, hand);
                }

                int currentMana = data.getMana();
                int augMax = data.getTotalMana() + 10;
                ItemStack pipe = playerIn.getHeldItem(hand);
                IItemHandler inventory = pipe.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
                if (inventory != null){
                    ItemStack smokeable = inventory.getStackInSlot(0);

                    if (!smokeable.isEmpty() && currentMana < augMax){
                        data.setMana(Math.min(augMax, currentMana + 10));
                        ItemHelper.shrinkStack(playerIn, smokeable, 1);
                        ItemStack pipe_item = playerIn.getHeldItem(hand);
                        ItemHelper.damageStack(playerIn, pipe_item, 1);

                        playerIn.getCooldownTracker().setCooldown(this, COOLDOWN);
                    }
                }

            }
        }
        return super.onItemRightClick(worldIn, playerIn, hand);
    }
}