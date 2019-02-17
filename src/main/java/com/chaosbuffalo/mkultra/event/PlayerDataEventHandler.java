package com.chaosbuffalo.mkultra.event;

import com.chaosbuffalo.mkultra.MKConfig;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class PlayerDataEventHandler {

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void onEntityJoinWorldEventServer(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayerMP) {
            PlayerData data = (PlayerData) MKUPlayerData.get((EntityPlayer) event.getEntity());
            if (data != null) {
                data.onJoinWorld();
            }
//            for (PotionEffect effect : ((EntityPlayer) event.getEntity()).getActivePotionEffects()) {
//                System.out.println(effect.getPotion().getRegistryName().toString());
//                if (effect.getPotion() instanceof SongPotionBase) {
//                    SongPotionBase songPotion = (SongPotionBase) effect.getPotion();
//                    if (songPotion.isHostSong) {
//                        ((EntityPlayer)event.getEntity()).removePotionEffect(songPotion);
//                    }
//                }
//            }
        } else if (event.getEntity() instanceof EntityLivingBase){
            MobData mobD = (MobData) MKUMobData.get((EntityLivingBase) event.getEntity());
            if (mobD != null){
                if (mobD.isMKSpawned()) {
                    event.getEntity().setDead();
                }
            }
        }
    }


    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onEntityJoinWorldEventClient(EntityJoinWorldEvent event) {

        if (event.getEntity() instanceof EntityPlayerSP) {
            PlayerData data = (PlayerData) MKUPlayerData.get((EntityPlayer) event.getEntity());
            if (data != null) {
                data.onJoinWorld();
            }
//            for (PotionEffect effect : ((EntityPlayer) event.getEntity()).getActivePotionEffects()) {
//                System.out.println(effect.getPotion().getRegistryName().toString());
//                if (effect.getPotion() instanceof SongPotionBase) {
//                    SongPotionBase songPotion = (SongPotionBase) effect.getPotion();
//                    if (songPotion.isHostSong) {
//                        ((EntityPlayer)event.getEntity()).removePotionEffect(songPotion);
//                    }
//                }
//            }
        // Run this on the server if we are single player.
        } else if (event.getEntity() instanceof EntityLivingBase && !event.getWorld().isRemote){
            MobData mobD = (MobData) MKUMobData.get((EntityLivingBase) event.getEntity());
            if (mobD != null){
                if (mobD.isMKSpawned()) {
                    event.getEntity().setDead();
                }
            }
        }
    }

    @SubscribeEvent
    public static void onLivingUpdateTick(LivingEvent.LivingUpdateEvent e) {
        if (e.getEntityLiving() instanceof EntityPlayer) {
            PlayerData playerData = (PlayerData) MKUPlayerData.get((EntityPlayer) e.getEntityLiving());
            if (playerData != null) {
                playerData.onTick();
            }
        } else {
            IMobData mobData = MKUMobData.get(e.getEntityLiving());
            if (mobData != null){
                mobData.onTick();
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone e) {
        // I would hope this can't happen
        if (e.getEntityPlayer() == null)
            return;

        PlayerData newData = (PlayerData) MKUPlayerData.get(e.getEntityPlayer());
        if (newData == null)
            return;

        // Die on the original so we can clone properly and not need an immediate onTick packet
        if (e.isWasDeath() && !MKConfig.PEPSI_BLUE_MODE) {
            IPlayerData oldData = MKUPlayerData.get(e.getOriginal());
            if (oldData == null)
                return;
            ((PlayerData) oldData).doDeath();
        }

        newData.clone(e.getOriginal());
        newData.onRespawn();
    }

    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer){
            event.addCapability(new ResourceLocation(MKUltra.MODID, "player_data"),
                    new PlayerDataProvider((EntityPlayer) event.getObject()));
        } else if (event.getObject() instanceof EntityLivingBase){
            event.addCapability(new ResourceLocation(MKUltra.MODID, "mob_data"),
                    new MobDataProvider((EntityLivingBase) event.getObject()));
        }



    }
}
