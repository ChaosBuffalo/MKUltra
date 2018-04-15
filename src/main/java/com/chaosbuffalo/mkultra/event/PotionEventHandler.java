package com.chaosbuffalo.mkultra.event;


import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
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
            } else if (entity.isPotionActive(WhirlpoolPotion.INSTANCE)) {
                PotionEffect potion = entity.getActivePotionEffect(WhirlpoolPotion.INSTANCE);
                entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(source.getImmediateSource(),
                        source.getTrueSource()), 8.0f * potion.getAmplifier());
            }
        }

        // Player is the source
        if (source.getTrueSource() != null && source.getTrueSource() instanceof EntityPlayerMP) {
            EntityPlayerMP playerSource = (EntityPlayerMP) source.getTrueSource();
            IPlayerData sourceData = MKUPlayerData.get(playerSource);
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
                playerSource.heal(event.getAmount() * .25f * potion.getAmplifier());
            }

            if (playerSource.isPotionActive(NocturnalCommunionPotion.INSTANCE)) {
                potion = playerSource.getActivePotionEffect(NocturnalCommunionPotion.INSTANCE);
                playerSource.heal(event.getAmount() * .20f * potion.getAmplifier());
            }
        }

        // Player is the victim
        if (livingTarget instanceof EntityPlayerMP) {
            IPlayerData targetData = MKUPlayerData.get((EntityPlayerMP) livingTarget);
            if (targetData == null) {
                return;
            }

            ArrayList<EntityPlayer> teammates = PartyManager.getPlayersOnTeam((EntityPlayer) livingTarget);
            ArrayList<EntityPlayer> damageAbsorbers = new ArrayList<>();
            for (EntityPlayer teammate : teammates) {
                if (teammate == null) {
                    continue;
                }
                if (teammate.isPotionActive(WaveBreakPotion.INSTANCE)) {
                    IPlayerData teammatedata = MKUPlayerData.get(teammate);
                    if (teammatedata == null) {
                        continue;
                    }
                    if (teammatedata.getMana() > 0) {
                        damageAbsorbers.add(teammate);
                        teammatedata.setMana(teammatedata.getMana() - 1);
                    } else {
                        teammate.removePotionEffect(WaveBreakPotion.INSTANCE);
                    }

                }
            }
            int absorbCount = damageAbsorbers.size();

            if (absorbCount > 0) {
                float newDamage = event.getAmount() / (absorbCount + 1);
                for (EntityPlayer absorber : damageAbsorbers) {
                    absorber.attackEntityFrom(source, newDamage);
                }
                event.setAmount(newDamage);
            }
            if (source.isMagicDamage()) {
                float newDamage = targetData.applyMagicArmor(event.getAmount());
                Log.debug("Magic armor reducing damage from %f to %f", event.getAmount(), newDamage);
                event.setAmount(newDamage);
            }

        }

        // Anyone is the victim

        // MoonTrance thorns. Source is attacker, event target is player
        if (source.getTrueSource() != null && source.getTrueSource() instanceof EntityLivingBase) {
            EntityLivingBase livingSource = (EntityLivingBase) source.getTrueSource();

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
