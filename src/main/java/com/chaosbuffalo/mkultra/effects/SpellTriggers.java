package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.packets.CritMessagePacket;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.utils.EntityUtils;
import com.chaosbuffalo.mkultra.utils.ItemUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.registry.IThrowableEntity;

import java.util.List;
import java.util.Map;

public class SpellTriggers {


    private static boolean isMKUltraAbilityDamage(DamageSource source) {
        return source instanceof MKDamageSource;
    }

    private static boolean isPlayerPhysicalDamage(DamageSource source) {
        return (!source.isFireDamage() && !source.isExplosion() && !source.isMagicDamage() &&
                source.getDamageType().equals("player"));
    }

    private static boolean isMislabeledThrowable(DamageSource source){
        return source.getImmediateSource() instanceof IThrowableEntity;
    }

    private static boolean isProjectileDamage(DamageSource source){
        return source.isProjectile();
    }


    public static class FALL {
        private static List<FallTrigger> fallTriggers = Lists.newArrayList();


        @FunctionalInterface
        public interface FallTrigger {
            void apply(LivingHurtEvent event, DamageSource source, EntityLivingBase entity);
        }

        public static void register(FallTrigger trigger) {
            fallTriggers.add(trigger);
        }

        public static void onLivingFall(LivingHurtEvent event, DamageSource source, EntityLivingBase entity) {
            fallTriggers.forEach(f -> f.apply(event, source, entity));
        }
    }

    public static class ENTITY_HURT_PLAYER {
        @FunctionalInterface
        public interface EntityHurtPlayerTrigger {
            void apply(LivingHurtEvent event, DamageSource source, EntityPlayer livingTarget, IPlayerData targetData);
        }

        private static List<EntityHurtPlayerTrigger> entityHurtPlayerPreTriggers = Lists.newArrayList();
        private static List<EntityHurtPlayerTrigger> entityHurtPlayerPostTriggers = Lists.newArrayList();

        public static void registerPreScale(EntityHurtPlayerTrigger trigger) {
            entityHurtPlayerPreTriggers.add(trigger);
        }

        public static void registerPostScale(EntityHurtPlayerTrigger trigger) {
            entityHurtPlayerPostTriggers.add(trigger);
        }

        public static void onEntityHurtPlayer(LivingHurtEvent event, DamageSource source, EntityPlayer livingTarget, IPlayerData targetData) {
            entityHurtPlayerPreTriggers.forEach(f -> f.apply(event, source, livingTarget, targetData));

            if (source.isMagicDamage()) {
                float newDamage = PlayerFormulas.applyMagicArmor(targetData, event.getAmount());
                Log.debug("Magic armor reducing damage from %f to %f", event.getAmount(), newDamage);
                event.setAmount(newDamage);
            }

            entityHurtPlayerPostTriggers.forEach(f -> f.apply(event, source, livingTarget, targetData));
        }
    }

    public static class PLAYER_HURT_ENTITY {

        @FunctionalInterface
        public interface PlayerHurtEntityTrigger {
            void apply(LivingHurtEvent event, DamageSource source, EntityLivingBase livingTarget, EntityPlayerMP playerSource, IPlayerData sourceData);
        }

        private static List<PlayerHurtEntityTrigger> playerHurtEntityMeleeTriggers = Lists.newArrayList();
        private static List<PlayerHurtEntityTrigger> playerHurtEntityMagicTriggers = Lists.newArrayList();
        private static List<PlayerHurtEntityTrigger> playerHurtEntityPostTriggers = Lists.newArrayList();

        public static void registerMelee(PlayerHurtEntityTrigger trigger) {
            playerHurtEntityMeleeTriggers.add(trigger);
        }

        public static void registerMagic(PlayerHurtEntityTrigger trigger) {
            playerHurtEntityMagicTriggers.add(trigger);
        }

        public static void registerPostHandler(PlayerHurtEntityTrigger trigger) {
            playerHurtEntityPostTriggers.add(trigger);
        }

        public static void onPlayerHurtEntity(LivingHurtEvent event, DamageSource source, EntityLivingBase livingTarget, EntityPlayerMP playerSource, IPlayerData sourceData) {
            if (source.isMagicDamage()) {
                float newDamage = PlayerFormulas.scaleMagicDamage(sourceData, event.getAmount());
                event.setAmount(newDamage);
            }

            if (isMKUltraAbilityDamage(source)) {
                MKDamageSource mkSource = (MKDamageSource) source;
                // Handle 'melee damage' abilities
                if (mkSource.isMeleeAbility()) {
                    handleMelee(event, source, livingTarget, playerSource, sourceData, false);
                } else {
                    // Handle the generic magic damage potions
                    handleMagic(event, livingTarget, playerSource, sourceData, mkSource);
                }
            }

            // If this is a weapon swing
            if (isPlayerPhysicalDamage(source)) {
                if (isMislabeledThrowable(source)){
                    handleProjectile(event, source, livingTarget, playerSource, sourceData);
                } else {
                    handleMelee(event, source, livingTarget, playerSource, sourceData, true);
                }

            }

            if (isProjectileDamage(source)){
                handleProjectile(event, source, livingTarget, playerSource, sourceData);
            }

            playerHurtEntityPostTriggers.forEach(f -> f.apply(event, source, livingTarget, playerSource, sourceData));
        }

        private static boolean checkCrit(EntityPlayerMP player, float chance) {
            return player.getRNG().nextFloat() >= 1.0f - chance;
        }

        private static void handleMagic(LivingHurtEvent event, EntityLivingBase livingTarget, EntityPlayerMP playerSource,
                                        IPlayerData sourceData, MKDamageSource mkSource) {

            if (checkCrit(playerSource, sourceData.getSpellCritChance())) {
                float newDamage = event.getAmount() * sourceData.getSpellCritDamage();
                event.setAmount(newDamage);

                CritMessagePacket packet;
                if (mkSource.isIndirectMagic()) {
                    packet = new CritMessagePacket(livingTarget.getEntityId(), playerSource.getUniqueID(),
                            newDamage, CritMessagePacket.CritType.INDIRECT_MAGIC_CRIT);
                } else {
                    PlayerAbility ability = MKURegistry.getAbility(mkSource.getAbilityId());
                    packet = new CritMessagePacket(livingTarget.getEntityId(), playerSource.getUniqueID(),
                            newDamage, ability.getAbilityId());
                }

                sendCritPacket(livingTarget, playerSource, packet);
            }

            playerHurtEntityMagicTriggers.forEach(f -> f.apply(event, mkSource, livingTarget, playerSource, sourceData));
        }

        private static void handleProjectile(LivingHurtEvent event, DamageSource source, EntityLivingBase livingTarget,
                                             EntityPlayerMP playerSource, IPlayerData sourceData)
        {
            if (source.getImmediateSource() != null &&
                checkCrit(playerSource, PlayerFormulas.getRangedCritChanceForEntity(sourceData,
                        playerSource, source.getImmediateSource()))){
                float damageMultiplier = EntityUtils.ENTITY_CRIT.getDamage(source.getImmediateSource());
                if (livingTarget.isGlowing()) {
                    damageMultiplier += 1.0f;
                }
                float newDamage = event.getAmount() * damageMultiplier;
                event.setAmount(newDamage);
                sendCritPacket(livingTarget, playerSource,
                        new CritMessagePacket(livingTarget.getEntityId(), playerSource.getUniqueID(), newDamage,
                                source.getImmediateSource().getEntityId()));
            }
        }

        private static void handleMelee(LivingHurtEvent event, DamageSource source, EntityLivingBase livingTarget,
                                        EntityPlayerMP playerSource, IPlayerData sourceData, boolean isDirect) {
            ItemStack mainHand = playerSource.getHeldItemMainhand();
            if (checkCrit(playerSource, PlayerFormulas.getMeleeCritChanceForItem(sourceData, playerSource, mainHand))) {
                float newDamage = event.getAmount() * ItemUtils.getCritDamageForItem(mainHand);
                event.setAmount(newDamage);
                CritMessagePacket.CritType type = isDirect ?
                        CritMessagePacket.CritType.MELEE_CRIT :
                        CritMessagePacket.CritType.INDIRECT_CRIT;

                sendCritPacket(livingTarget, playerSource,
                        new CritMessagePacket(livingTarget.getEntityId(), playerSource.getUniqueID(), newDamage, type));
            }

            playerHurtEntityMeleeTriggers.forEach(f -> f.apply(event, source, livingTarget, playerSource, sourceData));
        }
    }

    private static void sendCritPacket(EntityLivingBase livingTarget, EntityPlayerMP playerSource,
                                       CritMessagePacket packet) {
        MKUltra.packetHandler.sendToAllAround(packet, playerSource, 25.0f);

        Vec3d lookVec = livingTarget.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.CRIT_MAGIC.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 30, 6,
                        livingTarget.posX, livingTarget.posY + 1.0f,
                        livingTarget.posZ, .5f, .5f, .5f, 1.5,
                        lookVec),
                livingTarget, 25.0f);
    }


    public static class ATTACK_ENTITY {

        @FunctionalInterface
        public interface AttackEntityTrigger {
            void apply(EntityLivingBase player, Entity target, PotionEffect effect);
        }

        private static Map<SpellPotionBase, AttackEntityTrigger> attackEntityTriggers = Maps.newLinkedHashMap();

        public static void register(SpellPotionBase potion, AttackEntityTrigger trigger) {
            attackEntityTriggers.put(potion, trigger);
        }

        public static void onAttackEntity(EntityLivingBase attacker, Entity target) {
            attackEntityTriggers.forEach((spellPotionBase, attackEntityTrigger) -> {
                PotionEffect effect = attacker.getActivePotionEffect(spellPotionBase);
                if (effect != null) {
                    attackEntityTrigger.apply(attacker, target, effect);
                }
            });
        }
    }
}
