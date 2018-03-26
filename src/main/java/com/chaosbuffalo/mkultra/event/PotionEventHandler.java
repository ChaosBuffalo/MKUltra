package com.chaosbuffalo.mkultra.event;


import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerDataProvider;
import com.chaosbuffalo.mkultra.effects.spells.*;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.party.PartyManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class PotionEventHandler {

    public static boolean isPlayerPhysicalDamage(DamageSource source) {
        return (!source.isFireDamage() && !source.isExplosion() && !source.isMagicDamage() &&
                source.getDamageType().equals("player"));
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();
        EntityLivingBase livingTarget = event.getEntityLiving();
        if (source == DamageSource.FALL) { // TODO: maybe just use LivingFallEvent?
            EntityLivingBase entity = event.getEntityLiving();
            if (entity.isPotionActive(FeatherFallPotion.INSTANCE)) {
                event.setAmount(0.0f);
                if (entity instanceof EntityPlayer) {
                    entity.sendMessage(new TextComponentString("My legs are OK"));
                }
            } else if (entity.isPotionActive(WhirlpoolPotion.INSTANCE)){
                PotionEffect potion = entity.getActivePotionEffect(WhirlpoolPotion.INSTANCE);
                entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(source.getImmediateSource(),
                        source.getTrueSource()), 8.0f * potion.getAmplifier());
                entity.removePotionEffect(WhirlpoolPotion.INSTANCE);
            }
        }

        // Player is the source
        if (source.getTrueSource() != null && source.getTrueSource() instanceof EntityPlayerMP) {
            EntityPlayerMP playerSource = (EntityPlayerMP) source.getTrueSource();
            IPlayerData sourceData = PlayerDataProvider.get(playerSource);
            if (sourceData == null) {
                return;
            }
            if (source.isMagicDamage()) {
                float newDamage = sourceData.scaleMagicDamage(event.getAmount());
                Log.debug("Scaling magic damage from %f to %f", event.getAmount(), newDamage);
                event.setAmount(newDamage);
            }
            PotionEffect potion;
            if (playerSource.isPotionActive(VampiricReverePotion.INSTANCE) && isPlayerPhysicalDamage(source)
                    && sourceData.getMana() > 0) {
                potion = playerSource.getActivePotionEffect(VampiricReverePotion.INSTANCE);
                sourceData.setMana(sourceData.getMana() - 1);
                playerSource.heal(event.getAmount() * .15f * potion.getAmplifier());
            }

            if (playerSource.isPotionActive(NocturnalCommunionPotion.INSTANCE)) {
                potion = playerSource.getActivePotionEffect(NocturnalCommunionPotion.INSTANCE);
                playerSource.heal(event.getAmount() * .1f * potion.getAmplifier());
            }
        }

        // Player is the victim
        if (livingTarget instanceof EntityPlayerMP) {
            IPlayerData targetData = PlayerDataProvider.get((EntityPlayerMP)livingTarget);
            if (targetData == null) {
                return;
            }

            ArrayList<EntityPlayer> teammates = PartyManager.getPlayersOnTeam((EntityPlayer)livingTarget);
            for (EntityPlayer teammate : teammates){
                if (teammate == null){
                    continue;
                }
                if (teammate.isPotionActive(WaveBreakPotion.INSTANCE)){
                    float amount = event.getAmount();
                    event.setAmount(amount * .2f);
                    teammate.attackEntityFrom(source, amount *.8f);
                }
            }

            float newDamage = targetData.applyMagicArmor(event.getAmount());
            Log.debug("Magic armor reducing damage from %f to %f", event.getAmount(), newDamage);
            event.setAmount(newDamage);
        }

        // Anyone is the victim

        // MoonTrance thorns. Source is attacker, event target is player
        if (event.getSource().getTrueSource() != null && event.getSource().getTrueSource() instanceof EntityLivingBase) {
            EntityLivingBase livingSource = (EntityLivingBase) event.getSource().getTrueSource();

            PotionEffect effect = livingTarget.getActivePotionEffect(MoonTrancePotion.INSTANCE);
            if (effect != null) {
                Log.debug("Attacking %s due to %s", livingSource.getName(), effect.getPotion().getRegistryName());
                livingSource.attackEntityFrom(DamageSource.causeIndirectMagicDamage(livingTarget, livingTarget),
                        Math.min(event.getAmount(), 4.0f * effect.getAmplifier()));

            }
        }
    }

    @SubscribeEvent
    public static void onAttackEntityEvent(AttackEntityEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        Entity target = event.getTarget();
        PotionEffect potion = player.getActivePotionEffect(UndertowPotion.INSTANCE);
        if (potion != null) {
            UndertowPotion.INSTANCE.onAttackEntity(player, target, potion);
        }

        potion = player.getActivePotionEffect(FlameBladePotion.INSTANCE);
        if (potion != null) {
            FlameBladePotion.INSTANCE.onAttackEntity(player, target, potion);
        }
    }
}
