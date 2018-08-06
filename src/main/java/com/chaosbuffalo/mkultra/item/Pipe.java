package com.chaosbuffalo.mkultra.item;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.init.ModItems;
import com.chaosbuffalo.mkultra.network.ModGuiHandler;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
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
import net.minecraft.util.EnumParticleTypes;
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
                        ItemStack pipe_item = playerIn.getHeldItem(hand);
                        ItemHelper.damageStack(playerIn, pipe_item, 1);
                        inventory.extractItem(0, 1, false);
                        playerIn.getCooldownTracker().setCooldown(this, COOLDOWN);
                        MKUltra.packetHandler.sendToAllAround(
                                new ParticleEffectSpawnPacket(
                                        EnumParticleTypes.SMOKE_LARGE.getParticleID(),
                                        ParticleEffects.SPHERE_MOTION, 30, 5,
                                        playerIn.posX, playerIn.posY + 1.0,
                                        playerIn.posZ, 1.0, 1.0, 1.0, .01f,
                                        playerIn.getLookVec()),
                                playerIn, 50.0f);
                    }
                }

            }
        }
        return super.onItemRightClick(worldIn, playerIn, hand);
    }
}