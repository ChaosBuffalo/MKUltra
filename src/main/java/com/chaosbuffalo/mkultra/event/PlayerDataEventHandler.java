package com.chaosbuffalo.mkultra.event;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerDataProvider;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class PlayerDataEventHandler {

    @SubscribeEvent
    @SideOnly(Side.SERVER)
    public static void onEntityJoinWorldEventServer(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayerMP) {
            IPlayerData data = PlayerDataProvider.get((EntityPlayer) event.getEntity());
            if (data != null) {
                data.onJoinWorld();
            }
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onEntityJoinWorldEventClient(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof EntityPlayerSP) {
            IPlayerData data = PlayerDataProvider.get((EntityPlayer) event.getEntity());
            if (data != null) {
                data.onJoinWorld();
            }
        }
    }

    @SubscribeEvent
    public static void onLivingUpdateTick(LivingEvent.LivingUpdateEvent e) {
        if (e.getEntityLiving() instanceof EntityPlayer) {
            IPlayerData playerData = PlayerDataProvider.get((EntityPlayer) e.getEntityLiving());
            if (playerData != null) {
                playerData.onTick();
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone e) {
        // I would hope this can't happen
        if (e.getEntityPlayer() == null)
            return;

        IPlayerData newData = PlayerDataProvider.get(e.getEntityPlayer());
        if (newData == null)
            return;

        // Die on the original so we can clone properly and not need an immediate update packet
        if (e.isWasDeath()) {
            IPlayerData oldData = PlayerDataProvider.get(e.getOriginal());
            if (oldData == null)
                return;
            oldData.doDeath();
        }

        newData.clone(e.getOriginal());
        newData.onRespawn();

    }

    @SubscribeEvent
    public static void onAttachCapability(AttachCapabilitiesEvent<Entity> event) {
        if (!(event.getObject() instanceof EntityPlayer))
            return;

        event.addCapability(new ResourceLocation(MKUltra.MODID, "player_data"), new PlayerDataProvider((EntityPlayer) event.getObject()));
    }

    @SubscribeEvent
    static void onLivingHurt(LivingHurtEvent event) {

        DamageSource source = event.getSource();
        if (!source.isMagicDamage())
            return;


        // Try to apply magic damage bonus
        if (source.getImmediateSource() != null && source.getImmediateSource() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) source.getImmediateSource();

            IPlayerData data = PlayerDataProvider.get(player);
            if (data == null) {
                return;
            }

            float newDamage = data.scaleMagicDamage(event.getAmount());
            event.setAmount(newDamage);
        }


        // Try to apply magic armor
        if (event.getEntityLiving() instanceof EntityPlayerMP) {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntityLiving();

            IPlayerData data = PlayerDataProvider.get(player);
            if (data == null) {
                return;
            }

            float newDamage = data.applyMagicArmor(event.getAmount());
            event.setAmount(newDamage);

            // TODO: set unblockable to skip applyArmorCalculations?
            // TODO: set absolute to skip applyPotionDamageCalculations?
        }

    }
}