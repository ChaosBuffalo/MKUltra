//package com.chaosbuffalo.mkultra.item;
//
//import com.chaosbuffalo.mkultra.GameConstants;
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.fx.ParticleEffects;
//import com.chaosbuffalo.mkultra.init.ModItems;
//import com.chaosbuffalo.mkultra.network.ModGuiHandler;
//import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
//import com.chaosbuffalo.mkultra.utils.SmokeUtils;
//import net.minecraft.client.util.ITooltipFlag;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//import net.minecraft.nbt.NBTTagCompound;
//import net.minecraft.potion.PotionEffect;
//import net.minecraft.util.ActionResult;
//import net.minecraft.util.EnumHand;
//import net.minecraft.util.EnumParticleTypes;
//import net.minecraft.world.World;
//import net.minecraftforge.common.capabilities.ICapabilityProvider;
//import net.minecraftforge.fml.relauncher.Side;
//import net.minecraftforge.fml.relauncher.SideOnly;
//import net.minecraftforge.items.CapabilityItemHandler;
//import net.minecraftforge.items.IItemHandler;
//
//import javax.annotation.Nullable;
//import java.util.List;
//
//
///**
// * Created by Jacob on 4/10/2016.
// */
//public class Pipe extends Item {
//
//    static final int COOLDOWN = 5 * GameConstants.TICKS_PER_SECOND;
//
//    public Pipe(String unlocalizedName) {
//        super();
//        this.setTranslationKey(unlocalizedName);
//        this.setCreativeTab(MKUltra.MKULTRA_TAB);
//        this.setMaxDamage(150);
//        this.setMaxStackSize(1);
//    }
//
//    @Override
//    @Nullable
//    public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable NBTTagCompound nbt) {
//        if (stack.getItem() == ModItems.pipe) {
//            return new PipeProvider();
//        }
//        return null;
//    }
//
//    @Override
//    @SideOnly(Side.CLIENT)
//    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
//        tooltip.add("Right click while sneaking to load the Pipe.");
//        // We can't do this because Item Capabilities dont auto sync with the client. Would need to implement some kind of
//        // packet.
////        IItemHandler inventory = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
////        if (inventory != null){
////            ItemStack smokeable = inventory.getStackInSlot(0);
////            if (!smokeable.isEmpty()){
////                tooltip.add(String.format("Contains: %s", smokeable.getDisplayName()));
////            } else {
////                tooltip.add("The pipe is empty.");
////            }
////        }
//        super.addInformation(stack, worldIn, tooltip, flagIn);
//    }
//
//    @Override
//    public ActionResult<ItemStack> onItemRightClick(World worldIn,
//                                                    EntityPlayer playerIn, EnumHand hand) {
//        if (!worldIn.isRemote) {
//            if (playerIn.isSneaking()) {
//                playerIn.openGui(MKUltra.INSTANCE, ModGuiHandler.PIPE_CONTAINER_SCREEN, worldIn,
//                        (int) playerIn.posX, (int) playerIn.posY, (int) playerIn.posZ);
//            } else {
//                ItemStack pipe = playerIn.getHeldItem(hand);
//                IItemHandler inventory = pipe.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
//                if (inventory != null) {
//                    ItemStack smokeable = inventory.getStackInSlot(0);
//                    if (!smokeable.isEmpty()) {
//                        PotionEffect effect_to_add = SmokeUtils.getPotionEffectForSmokeable(smokeable, playerIn);
//                        if (effect_to_add != null) {
//                            playerIn.addPotionEffect(effect_to_add);
//                        }
//                        ItemStack pipe_item = playerIn.getHeldItem(hand);
//                        ItemHelper.damageStack(playerIn, pipe_item, 1);
//                        inventory.extractItem(0, 1, false);
//                        playerIn.getCooldownTracker().setCooldown(this, COOLDOWN);
//                        MKUltra.packetHandler.sendToAllAround(
//                                new ParticleEffectSpawnPacket(
//                                        EnumParticleTypes.SMOKE_LARGE.getParticleID(),
//                                        ParticleEffects.SPHERE_MOTION, 30, 5,
//                                        playerIn.posX, playerIn.posY + 1.0,
//                                        playerIn.posZ, 1.0, 1.0, 1.0, .01f,
//                                        playerIn.getLookVec()),
//                                playerIn, 50.0f);
//                    }
//                }
//
//            }
//        }
//        return super.onItemRightClick(worldIn, playerIn, hand);
//    }
//}