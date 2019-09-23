package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.core.events.ServerSideLeftClickEmpty;
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
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.registry.IThrowableEntity;

import java.util.List;
import java.util.Map;

public class SpellTriggers {


    public static boolean isMKUltraAbilityDamage(DamageSource source) {
        return source instanceof MKDamageSource;
    }

    public static boolean isPlayerPhysicalDamage(DamageSource source) {
        return (!source.isFireDamage() && !source.isExplosion() && !source.isMagicDamage() &&
                source.getDamageType().equals("player"));
    }

    public static boolean isMeleeDamage(DamageSource source) {
        return isPlayerPhysicalDamage(source) ||
                (source instanceof MKDamageSource && ((MKDamageSource) source).isMeleeAbility());
    }

    public static boolean isMislabeledThrowable(DamageSource source) {
        return source.getImmediateSource() instanceof IThrowableEntity;
    }

    public static boolean isProjectileDamage(DamageSource source) {
        return source.isProjectile();
    }


    public static class FALL {
        private static final String TAG = FALL.class.getName();
        private static List<FallTrigger> fallTriggers = Lists.newArrayList();

        @FunctionalInterface
        public interface FallTrigger {
            void apply(LivingHurtEvent event, DamageSource source, EntityLivingBase entity);
        }

        public static void register(FallTrigger trigger) {
            fallTriggers.add(trigger);
        }

        public static void onLivingFall(LivingHurtEvent event, DamageSource source, EntityLivingBase entity) {
            if (!startTrigger(entity, TAG))
                return;
            fallTriggers.forEach(f -> f.apply(event, source, entity));
            endTrigger(entity, TAG);
        }
    }

    public static class PLAYER_KILL_ENTITY {
        private static final String TAG = PLAYER_KILL_ENTITY.class.getName();
        private static Map<SpellPotionBase, PlayerKillEntityTrigger> killTriggers = Maps.newLinkedHashMap();

        @FunctionalInterface
        public interface PlayerKillEntityTrigger {
            void apply(LivingDeathEvent event, DamageSource source, EntityPlayer player);
        }

        public static void register(SpellPotionBase potion, PlayerKillEntityTrigger trigger) {
            killTriggers.put(potion, trigger);
        }

        public static void onEntityDeath(LivingDeathEvent event, DamageSource source, EntityPlayer entity) {
            if (!startTrigger(entity, TAG))
                return;
            killTriggers.forEach((spellPotionBase, trigger) -> {
                PotionEffect effect = entity.getActivePotionEffect(spellPotionBase);
                if (effect != null) {
                    trigger.apply(event, source, entity);
                }
            });
            endTrigger(entity, TAG);
        }
    }

    public static class PLAYER_EQUIPMENT_CHANGE {
        private static final String TAG = PLAYER_EQUIPMENT_CHANGE.class.getName();
        private static Map<SpellPotionBase, PlayerEquipmentChangeTrigger> triggers = Maps.newLinkedHashMap();

        @FunctionalInterface
        public interface PlayerEquipmentChangeTrigger {
            void apply(LivingEquipmentChangeEvent event, IPlayerData data, EntityPlayer player);
        }

        public static void register(SpellPotionBase potion, PlayerEquipmentChangeTrigger trigger) {
            triggers.put(potion, trigger);
        }

        public static void onEquipmentChange(LivingEquipmentChangeEvent event, IPlayerData data, EntityPlayer player) {
            if (!startTrigger(player, TAG))
                return;
            triggers.forEach((spellPotionBase, trigger) -> {
                PotionEffect effect = player.getActivePotionEffect(spellPotionBase);
                if (effect != null) {
                    trigger.apply(event, data, player);
                }
            });
            endTrigger(player, TAG);
        }
    }

    public static class ENTITY_HURT_PLAYER {
        @FunctionalInterface
        public interface EntityHurtPlayerTrigger {
            void apply(LivingHurtEvent event, DamageSource source, EntityPlayer livingTarget, IPlayerData targetData);
        }

        private static final String TAG = ENTITY_HURT_PLAYER.class.getName();
        private static List<EntityHurtPlayerTrigger> entityHurtPlayerPreTriggers = Lists.newArrayList();
        private static List<EntityHurtPlayerTrigger> entityHurtPlayerPostTriggers = Lists.newArrayList();

        public static void registerPreScale(EntityHurtPlayerTrigger trigger) {
            entityHurtPlayerPreTriggers.add(trigger);
        }

        public static void registerPostScale(EntityHurtPlayerTrigger trigger) {
            entityHurtPlayerPostTriggers.add(trigger);
        }

        public static void onEntityHurtPlayer(LivingHurtEvent event, DamageSource source, EntityPlayer livingTarget, IPlayerData targetData) {
            if (!startTrigger(livingTarget, TAG))
                return;
            entityHurtPlayerPreTriggers.forEach(f -> f.apply(event, source, livingTarget, targetData));

            if (source.isMagicDamage()) {
                float newDamage = PlayerFormulas.applyMagicArmor(targetData, event.getAmount());
                Log.debug("Magic armor reducing damage from %f to %f", event.getAmount(), newDamage);
                event.setAmount(newDamage);
            }

            entityHurtPlayerPostTriggers.forEach(f -> f.apply(event, source, livingTarget, targetData));
            endTrigger(livingTarget, TAG);
        }
    }

    public static class PLAYER_HURT_ENTITY {

        @FunctionalInterface
        public interface PlayerHurtEntityTrigger {
            void apply(LivingHurtEvent event, DamageSource source, EntityLivingBase livingTarget, EntityPlayerMP playerSource, IPlayerData sourceData);
        }

        private static final String MELEE_TAG = "PLAYER_HURT_ENTITY.melee";
        private static final String MAGIC_TAG = "PLAYER_HURT_ENTITY.magic";
        private static final String POST_TAG = "PLAYER_HURT_ENTITY.post";
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
                float scaleFactor = 1.0f;
                if (isMKUltraAbilityDamage(source)){
                    MKDamageSource mkSource = (MKDamageSource) source;
                    scaleFactor = mkSource.getModifierScaling();
                }
                float newDamage = PlayerFormulas.scaleMagicDamage(sourceData, event.getAmount(), scaleFactor);
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
                if (isMislabeledThrowable(source)) {
                    handleProjectile(event, source, livingTarget, playerSource, sourceData);
                } else {
                    handleMelee(event, source, livingTarget, playerSource, sourceData, true);
                }

            }

            if (isProjectileDamage(source)) {
                handleProjectile(event, source, livingTarget, playerSource, sourceData);
            }

            if (!startTrigger(playerSource, POST_TAG))
                return;
            playerHurtEntityPostTriggers.forEach(f -> f.apply(event, source, livingTarget, playerSource, sourceData));
            endTrigger(playerSource, POST_TAG);
        }

        private static boolean checkCrit(EntityPlayerMP player, float chance) {
            return player.getRNG().nextFloat() >= 1.0f - chance;
        }

        private static void handleMagic(LivingHurtEvent event, EntityLivingBase livingTarget, EntityPlayerMP playerSource,
                                        IPlayerData sourceData, MKDamageSource mkSource) {

            float spellCritchance = sourceData.getSpellCritChance();
            if (mkSource.isHolyDamage()) {
                spellCritchance *= 2.0f;
            }
            if (checkCrit(playerSource, spellCritchance)) {
                float newDamage = event.getAmount() * sourceData.getSpellCritDamage();
                event.setAmount(newDamage);

                CritMessagePacket packet;
                if (mkSource.isIndirectMagic()) {
                    packet = new CritMessagePacket(livingTarget.getEntityId(), playerSource.getUniqueID(),
                            newDamage, CritMessagePacket.CritType.INDIRECT_MAGIC_CRIT);
                } else if (mkSource.isHolyDamage()) {
                    packet = new CritMessagePacket(livingTarget.getEntityId(), playerSource.getUniqueID(),
                            newDamage, CritMessagePacket.CritType.HOLY_DAMAGE_CRIT);
                } else {
                    PlayerAbility ability = MKURegistry.getAbility(mkSource.getAbilityId());
                    packet = new CritMessagePacket(livingTarget.getEntityId(), playerSource.getUniqueID(),
                            newDamage, ability.getAbilityId());
                }

                sendCritPacket(livingTarget, playerSource, packet);
            }

            if (!startTrigger(playerSource, MAGIC_TAG))
                return;
            playerHurtEntityMagicTriggers.forEach(f -> f.apply(event, mkSource, livingTarget, playerSource, sourceData));
            endTrigger(playerSource, MAGIC_TAG);
        }

        private static void handleProjectile(LivingHurtEvent event, DamageSource source, EntityLivingBase livingTarget,
                                             EntityPlayerMP playerSource, IPlayerData sourceData) {
            if (source.getImmediateSource() != null &&
                    checkCrit(playerSource, PlayerFormulas.getRangedCritChanceForEntity(sourceData,
                            playerSource, source.getImmediateSource()))) {
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
            float critChance = PlayerFormulas.getMeleeCritChanceForItem(sourceData, playerSource, mainHand);
            if (!isDirect) {
                IAttributeInstance atkDmg = playerSource.getAttributeMap().getAttributeInstance(
                        SharedMonsterAttributes.ATTACK_DAMAGE);
                double amount = atkDmg.getAttributeValue();
                if (isMKUltraAbilityDamage(source)){
                    MKDamageSource mkSource = (MKDamageSource) source;
                    amount *= mkSource.getModifierScaling();
                }
                event.setAmount((float) (event.getAmount() +
                        amount * playerSource.world.rand.nextDouble()));
            }
            if (checkCrit(playerSource, critChance)) {
                float critMultiplier = ItemUtils.getCritDamageForItem(mainHand);
                critMultiplier += sourceData.getMeleeCritDamage();
                float newDamage = event.getAmount() * critMultiplier;
                event.setAmount(newDamage);
                CritMessagePacket.CritType type = isDirect ?
                        CritMessagePacket.CritType.MELEE_CRIT :
                        CritMessagePacket.CritType.INDIRECT_CRIT;

                sendCritPacket(livingTarget, playerSource,
                        new CritMessagePacket(livingTarget.getEntityId(), playerSource.getUniqueID(), newDamage, type));
            }

            if (startTrigger(playerSource, MELEE_TAG))
                return;
            playerHurtEntityMeleeTriggers.forEach(f -> f.apply(event, source, livingTarget, playerSource, sourceData));
            endTrigger(playerSource, MELEE_TAG);
        }
    }

    private static void sendCritPacket(EntityLivingBase livingTarget, EntityPlayerMP playerSource,
                                       CritMessagePacket packet) {
        MKUltra.packetHandler.sendToAllAround(packet, playerSource, 25.0f);

        Vec3d lookVec = livingTarget.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.CRIT_MAGIC.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 12, 4,
                        livingTarget.posX, livingTarget.posY + 1.0f,
                        livingTarget.posZ, .5f, .5f, .5f, 0.2,
                        lookVec),
                livingTarget, 25.0f);
    }

    public static class EMPTY_LEFT_CLICK {

        @FunctionalInterface
        public interface EmptyLeftClickTrigger {
            void apply(ServerSideLeftClickEmpty event, EntityPlayer player, PotionEffect effect);
        }

        private static final String TAG = EMPTY_LEFT_CLICK.class.getName();
        private static Map<SpellPotionBase, EmptyLeftClickTrigger> emptyLeftClickTriggers = Maps.newLinkedHashMap();

        public static void register(SpellPotionBase potion, EmptyLeftClickTrigger trigger) {
            emptyLeftClickTriggers.put(potion, trigger);
        }

        public static void onEmptyLeftClick(EntityPlayer player, ServerSideLeftClickEmpty event) {
            if (!startTrigger(player, TAG))
                return;
            emptyLeftClickTriggers.forEach((spellPotionBase, trigger) -> {
                PotionEffect effect = player.getActivePotionEffect(spellPotionBase);
                if (effect != null) {
                    trigger.apply(event, player, effect);
                }
            });
            endTrigger(player, TAG);
        }
    }

    private static boolean startTrigger(Entity source, String tag) {
        if (source instanceof EntityPlayer) {
//            Log.info("startTrigger - %s", tag);
            PlayerData data = (PlayerData) MKUPlayerData.get((EntityPlayer) source);
            if (data == null || data.isInSpellTriggerCallback(tag)) {
//                Log.info("startTrigger - BLOCKING %s", tag);
                return false;
            }
            data.setInSpellTriggerCallback(tag, true);
        }
        return true;
    }

    private static void endTrigger(Entity source, String tag) {
        if (source instanceof EntityPlayer) {
//            Log.info("endTrigger - %s", tag);
            PlayerData data = (PlayerData) MKUPlayerData.get((EntityPlayer) source);
            if (data == null)
                return;
            data.setInSpellTriggerCallback(tag, false);
        }
    }

    public static class ATTACK_ENTITY {

        @FunctionalInterface
        public interface AttackEntityTrigger {
            void apply(EntityLivingBase player, Entity target, PotionEffect effect);
        }

        private static final String TAG = ATTACK_ENTITY.class.getName();
        private static Map<SpellPotionBase, AttackEntityTrigger> attackEntityTriggers = Maps.newLinkedHashMap();

        public static void register(SpellPotionBase potion, AttackEntityTrigger trigger) {
            attackEntityTriggers.put(potion, trigger);
        }

        public static void onAttackEntity(EntityLivingBase attacker, Entity target) {
            if (!startTrigger(attacker, TAG))
                return;
            attackEntityTriggers.forEach((spellPotionBase, attackEntityTrigger) -> {
                PotionEffect effect = attacker.getActivePotionEffect(spellPotionBase);
                if (effect != null) {
                    attackEntityTrigger.apply(attacker, target, effect);
                }
            });
            endTrigger(attacker, TAG);
        }
    }

    public static class PLAYER_ATTACK_ENTITY {
        @FunctionalInterface
        public interface PlayerAttackEntityTrigger {
            void apply(EntityLivingBase player, Entity target, PotionEffect effect);
        }

        private static final String TAG = PLAYER_ATTACK_ENTITY.class.getName();
        private static Map<SpellPotionBase, PlayerAttackEntityTrigger> attackEntityTriggers = Maps.newLinkedHashMap();

        public static void register(SpellPotionBase potion, PlayerAttackEntityTrigger trigger) {
            attackEntityTriggers.put(potion, trigger);
        }

        public static void onAttackEntity(EntityLivingBase attacker, Entity target) {
            if (!startTrigger(attacker, TAG))
                return;
            attackEntityTriggers.forEach((spellPotionBase, attackEntityTrigger) -> {
                PotionEffect effect = attacker.getActivePotionEffect(spellPotionBase);
                if (effect != null) {
                    attackEntityTrigger.apply(attacker, target, effect);
                }
            });
            endTrigger(attacker, TAG);
        }
    }

    public static class PLAYER_DEATH {
        @FunctionalInterface
        public interface PlayerKillEntityTrigger {
            void apply(LivingDeathEvent event, DamageSource source, EntityPlayer player);
        }

        private static final String TAG = PLAYER_DEATH.class.getName();
        private static Map<SpellPotionBase, PlayerKillEntityTrigger> killTriggers = Maps.newLinkedHashMap();

        public static void register(SpellPotionBase potion, PlayerKillEntityTrigger trigger) {
            killTriggers.put(potion, trigger);
        }

        public static void onEntityDeath(LivingDeathEvent event, DamageSource source, EntityPlayer entity) {
            if (!startTrigger(entity, TAG))
                return;
            killTriggers.forEach((spellPotionBase, trigger) -> {
                if (entity.isPotionActive(spellPotionBase)) {
                    trigger.apply(event, source, entity);
                }
            });
            endTrigger(entity, TAG);
        }
    }
}
