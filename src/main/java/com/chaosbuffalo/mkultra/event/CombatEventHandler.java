package com.chaosbuffalo.mkultra.event;


import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.effects.spells.*;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.party.PartyManager;
import com.chaosbuffalo.mkultra.utils.ItemUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class CombatEventHandler {

    private static float MAX_CRIT_MESSAGE_DISTANCE = 50.0f;

    public static boolean isPlayerPhysicalDamage(DamageSource source) {
        return (!source.isFireDamage() && !source.isExplosion() && !source.isMagicDamage() &&
                source.getDamageType().equals("player"));
    }

    public static float getCombinedCritChance(IPlayerData data, EntityPlayerMP player){
        return data.getMeleeCritChance() + ItemUtils.getCritChanceForItem(player.getHeldItemMainhand());
    }

    public static boolean isMKUltraAbilityDamage(DamageSource source){
        return source.getDamageType().equals(MKDamageSource.ABILITY_DMG_TYPE);
    }

    public static void doVampiricRevere(EntityPlayerMP playerSource, IPlayerData sourceData, LivingHurtEvent event){
        if (playerSource.isPotionActive(VampiricReverePotion.INSTANCE) && sourceData.getMana() > 0) {
            PotionEffect potion = playerSource.getActivePotionEffect(VampiricReverePotion.INSTANCE);
            sourceData.setMana(sourceData.getMana() - 1);
            playerSource.heal(event.getAmount() * .25f * potion.getAmplifier());
        }
    }

    public static void doLightningCritical(EntityPlayerMP playerSource, IPlayerData sourceData, LivingHurtEvent event,
                                           EntityLivingBase livingTarget, TextFormatting msgColor){
        if (playerSource.getRNG().nextFloat() >= 1.0f - sourceData.getSpellCritChance()){
            Style messageStyle = new Style();
            messageStyle.setColor(msgColor);
            float newDamage = event.getAmount() * sourceData.getSpellCritDamage();
            event.setAmount(newDamage);
            playerSource.sendMessage(new TextComponentString(
                    String.format("Your lightning just crit %s for %s",
                            livingTarget.getDisplayName().getUnformattedText(),
                            Float.toString(newDamage)))
                    .setStyle(messageStyle)
            );
            ArrayList<EntityPlayer> partymates = PartyManager.getPlayersOnTeam(playerSource);
            if (partymates.size() > 0){
                String team_message = String.format("%s's lightning just crit %s for %s",
                        playerSource.getDisplayName().getUnformattedText(),
                        livingTarget.getDisplayName().getUnformattedText(),
                        Float.toString(newDamage));
                for (EntityPlayer mate : partymates){
                    if (playerSource.getDistance(mate) <= MAX_CRIT_MESSAGE_DISTANCE){
                        mate.sendMessage(new TextComponentString(team_message).setStyle(messageStyle));
                    }
                }
            }
            Vec3d lookVec = livingTarget.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.EXPLOSION_NORMAL.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 20, 4,
                            livingTarget.posX, livingTarget.posY + 1.0f,
                            livingTarget.posZ, .5f, .5f, .5f, 1.5,
                            lookVec),
                    livingTarget.dimension, livingTarget.posX,
                    livingTarget.posY, livingTarget.posZ, 50.0f);
        }
    }

    public static void doAbilityMagicCritical(EntityPlayerMP playerSource, IPlayerData sourceData, LivingHurtEvent event,
                                               EntityLivingBase livingTarget, TextFormatting msgColor, BaseAbility ability){
        if (playerSource.getRNG().nextFloat() >= 1.0f - sourceData.getSpellCritChance()){
            Style messageStyle = new Style();
            messageStyle.setColor(msgColor);
            float newDamage = event.getAmount() * sourceData.getSpellCritDamage();
            event.setAmount(newDamage);
            playerSource.sendMessage(new TextComponentString(
                    String.format("Your %s spell just crit %s for %s",
                            ability.getAbilityName(),
                            livingTarget.getDisplayName().getUnformattedText(),
                            Float.toString(newDamage)))
                    .setStyle(messageStyle)
            );
            ArrayList<EntityPlayer> partymates = PartyManager.getPlayersOnTeam(playerSource);
            if (partymates.size() > 0){
                String team_message = String.format("%s's %s spell just crit %s for %s",
                        playerSource.getDisplayName().getUnformattedText(),
                        ability.getAbilityName(),
                        livingTarget.getDisplayName().getUnformattedText(),
                        Float.toString(newDamage));
                for (EntityPlayer mate : partymates){
                    if (playerSource.getDistance(mate) <= MAX_CRIT_MESSAGE_DISTANCE){
                        mate.sendMessage(new TextComponentString(team_message).setStyle(messageStyle));
                    }
                }
            }
            Vec3d lookVec = livingTarget.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.CRIT_MAGIC.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 30, 6,
                            livingTarget.posX, livingTarget.posY + 1.0f,
                            livingTarget.posZ, .5f, .5f, .5f, 1.5,
                            lookVec),
                    livingTarget.dimension, livingTarget.posX,
                    livingTarget.posY, livingTarget.posZ, 50.0f);
        }
    }

    public static void doIndirectMagicCritical(EntityPlayerMP playerSource, IPlayerData sourceData, LivingHurtEvent event,
                                           EntityLivingBase livingTarget, TextFormatting msgColor){
        if (playerSource.getRNG().nextFloat() >= 1.0f - sourceData.getSpellCritChance()){
            Style messageStyle = new Style();
            messageStyle.setColor(msgColor);
            float newDamage = event.getAmount() * sourceData.getSpellCritDamage();
            event.setAmount(newDamage);
            playerSource.sendMessage(new TextComponentString(
                    String.format("Your magic spell just crit %s for %s",
                            livingTarget.getDisplayName().getUnformattedText(),
                            Float.toString(newDamage)))
                    .setStyle(messageStyle)
            );
            ArrayList<EntityPlayer> partymates = PartyManager.getPlayersOnTeam(playerSource);
            if (partymates.size() > 0){
                String team_message = String.format("%s's magic spell just crit %s for %s",
                        playerSource.getDisplayName().getUnformattedText(),
                        livingTarget.getDisplayName().getUnformattedText(),
                        Float.toString(newDamage));
                for (EntityPlayer mate : partymates){
                    if (playerSource.getDistance(mate) <= MAX_CRIT_MESSAGE_DISTANCE){
                        mate.sendMessage(new TextComponentString(team_message).setStyle(messageStyle));
                    }
                }
            }
            Vec3d lookVec = livingTarget.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.CRIT_MAGIC.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 30, 6,
                            livingTarget.posX, livingTarget.posY + 1.0f,
                            livingTarget.posZ, .5f, .5f, .5f, 1.5,
                            lookVec),
                    livingTarget.dimension, livingTarget.posX,
                    livingTarget.posY, livingTarget.posZ, 50.0f);
        }
    }

    public static void doMeleeCritical(EntityPlayerMP playerSource, IPlayerData sourceData, LivingHurtEvent event,
                                       EntityLivingBase livingTarget, TextFormatting msgColor){
        Log.info("Crit chance for %s: %f", playerSource.getHeldItemMainhand().getDisplayName().toString(), getCombinedCritChance(sourceData, playerSource));
        if (playerSource.getRNG().nextFloat() >= 1.0f - getCombinedCritChance(sourceData, playerSource)){
            Style messageStyle = new Style();
            messageStyle.setColor(msgColor);
            ItemStack mainHand = playerSource.getHeldItemMainhand();
            float newDamage = event.getAmount() * ItemUtils.getCritDamageForItem(
                    mainHand);
            event.setAmount(newDamage);
            playerSource.sendMessage(new TextComponentString(
                    String.format("You just crit %s with: %s for %s",
                            livingTarget.getDisplayName().getUnformattedText(), mainHand.getDisplayName(),
                            Float.toString(newDamage)))
                    .setStyle(messageStyle)
            );
            ArrayList<EntityPlayer> partymates = PartyManager.getPlayersOnTeam(playerSource);
            if (partymates.size() > 0){
                String team_message = String.format("%s just crit %s with: %s for %s",
                        playerSource.getDisplayName().getUnformattedText(),
                        livingTarget.getDisplayName().getUnformattedText(),
                        mainHand.getDisplayName(),
                        Float.toString(newDamage));
                for (EntityPlayer mate : partymates){
                    if (playerSource.getDistance(mate) <= MAX_CRIT_MESSAGE_DISTANCE){
                        mate.sendMessage(new TextComponentString(team_message).setStyle(messageStyle));
                    }
                }
            }
            Vec3d lookVec = livingTarget.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.CRIT.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 30, 6,
                            livingTarget.posX, livingTarget.posY + 1.0f,
                            livingTarget.posZ, .5f, .5f, .5f, 1.5,
                            lookVec),
                    livingTarget.dimension, livingTarget.posX,
                    livingTarget.posY, livingTarget.posZ, 50.0f);
        }
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
                float newDamage = PlayerFormulas.scaleMagicDamage(sourceData, event.getAmount());
                Log.debug("Scaling magic damage from %f to %f", event.getAmount(), newDamage);
                event.setAmount(newDamage);
            }
            if (isMKUltraAbilityDamage(source)){
                Log.info("This is ability damage");
                MKDamageSource mkSource = (MKDamageSource) source;
                // Handle 'melee damage' abilities
                if (mkSource.ability_id.equals(InstantIndirectDamagePotion.INDIRECT_DMG_ABILITY_ID)){
                    Log.info("Running indirect dmg ability logic.");
                    doMeleeCritical(playerSource, sourceData, event, livingTarget, TextFormatting.GOLD);
                    doVampiricRevere(playerSource, sourceData, event);
                } else if (mkSource.ability_id.equals(LightningDamagePotion.INDIRECT_LIGHTNING_DMG_ABILITY_ID)){
                    doLightningCritical(playerSource, sourceData, event, livingTarget, TextFormatting.YELLOW);
                } else if (mkSource.ability_id.equals(InstantIndirectMagicDamagePotion.INDIRECT_MAGIC_DMG_ABILITY_ID)){
                    Log.info("This is indirect magic damage");
                    doIndirectMagicCritical(playerSource, sourceData, event, livingTarget, TextFormatting.BLUE);
                } else {
                    Log.info("This is ability handling for spells. %s", mkSource.ability_id.toString());
                    doAbilityMagicCritical(playerSource, sourceData, event, livingTarget, TextFormatting.AQUA,
                            ClassData.getAbility(mkSource.ability_id));
                }
            }

            if (isPlayerPhysicalDamage(source)){
                Log.info("Doing melee crit logic.");
                doMeleeCritical(playerSource, sourceData, event, livingTarget, TextFormatting.DARK_RED);
                doVampiricRevere(playerSource, sourceData, event);
            }



            if (playerSource.isPotionActive(NocturnalCommunionPotion.INSTANCE)) {
                PotionEffect potion;
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
                float newDamage = PlayerFormulas.applyMagicArmor(targetData, event.getAmount());
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
        potion = player.getActivePotionEffect(WildToxinPotion.INSTANCE);
        if (potion != null){
            WildToxinPotion.INSTANCE.onAttackEntity(player, target, potion);
        }
    }

}
