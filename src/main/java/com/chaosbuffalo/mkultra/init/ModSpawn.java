package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MKUMobData;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.chaosbuffalo.mkultra.core.mob_abilities.MobFireArrow;
import com.chaosbuffalo.mkultra.core.mob_abilities.MobFireball;
import com.chaosbuffalo.mkultra.core.mob_abilities.ShadowDash;
import com.chaosbuffalo.mkultra.core.mob_abilities.MobNaturesRemedy;
import com.chaosbuffalo.mkultra.json_utils.LoadingHelper;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.mob_ai.*;
import com.chaosbuffalo.mkultra.spawn.*;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.function.BiFunction;


@Mod.EventBusSubscriber
public class ModSpawn {
    public static final int MAX_LEVEL = 10;

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerItemOptions(RegistryEvent.Register<ItemOption> event) {
        Log.info("Registering Item Options");
        LoadingHelper.loadModsForType("/spawn/item_options", LoadingHelper::loadItemOption, event);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerAttributeSetters(RegistryEvent.Register<AttributeSetter> event) {
        Log.info("Registering Attribute Setters");
        AttributeSetter max_health = new AttributeSetter(MKUltra.MODID,
                "max_health", BaseSpawnAttributes.MAX_HEALTH);
        event.getRegistry().register(max_health);
        AttributeSetter follow_range = new AttributeSetter(MKUltra.MODID,
                "follow_range", BaseSpawnAttributes.FOLLOW_RANGE);
        event.getRegistry().register(follow_range);
        AttributeSetter attack_speed = new AttributeSetter(MKUltra.MODID,
                "attack_speed", BaseSpawnAttributes.ATTACK_SPEED);
        event.getRegistry().register(attack_speed);
        AttributeSetter armor = new AttributeSetter(MKUltra.MODID,
                "armor", BaseSpawnAttributes.ARMOR);
        event.getRegistry().register(armor);
        AttributeSetter armor_toughness = new AttributeSetter(MKUltra.MODID,
                "armor_toughness", BaseSpawnAttributes.ARMOR_TOUGHNESS);
        event.getRegistry().register(armor_toughness);
        AttributeSetter attack_damage = new AttributeSetter(MKUltra.MODID,
                "attack_damage", BaseSpawnAttributes.ATTACK_DAMAGE);
        event.getRegistry().register(attack_damage);
        AttributeSetter knockback_resistance = new AttributeSetter(MKUltra.MODID,
                "knockback_resistance", BaseSpawnAttributes.KNOCKBACK_RESISTANCE);
        event.getRegistry().register(knockback_resistance);
        AttributeSetter movement_speed = new AttributeSetter(MKUltra.MODID,
                "movement_speed", BaseSpawnAttributes.MOVEMENT_SPEED);
        event.getRegistry().register(movement_speed);
        AttributeSetter aggro_range = new AttributeSetter(MKUltra.MODID,
                "aggro_range", MKSpawnAttributes.SET_AGGRO_RADIUS);
        event.getRegistry().register(aggro_range);
    }


    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerAttributeRanges(RegistryEvent.Register<AttributeRange> event) {
        Log.info("Registering Attribute Ranges");
        LoadingHelper.loadModsForType("/spawn/attributes", LoadingHelper::loadAttribute, event);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerMobAbilities(RegistryEvent.Register<MobAbility> event) {
        Log.info("Registering Mob Abilities");
        MobAbility test_buff = new MobNaturesRemedy();
        event.getRegistry().register(test_buff);
        MobAbility shadow_dash = new ShadowDash();
        event.getRegistry().register(shadow_dash);
        MobAbility fireball = new MobFireball();
        event.getRegistry().register(fireball);
        MobAbility fire_arrow = new MobFireArrow();
        event.getRegistry().register(fire_arrow);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerMobDefinitions(RegistryEvent.Register<MobDefinition> event) {
        Log.info("Registering Mob Definitions");
        LoadingHelper.loadModsForType("/spawn/mob_definitions", LoadingHelper::loadMobDefinition, event);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerMobFactions(RegistryEvent.Register<MobFaction> event) {
        Log.info("Registering Mob Factions");
        LoadingHelper.loadModsForType("/spawn/mob_factions", LoadingHelper::loadMobFactions, event);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerSpawnLists(RegistryEvent.Register<SpawnList> event) {
        Log.info("Registering Spawn Lists");
        LoadingHelper.loadModsForType("/spawn/spawn_lists", LoadingHelper::loadSpawnList, event);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerAIGenerators(RegistryEvent.Register<AIGenerator> event){
        Log.info("Registering AI Generators");
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> getWatchClosestLongRange =
                (entity, choice) -> new EntityAIWatchClosest(entity, EntityPlayer.class, 20.0F);
        AIGenerator watchClosest = new AIGenerator(MKUltra.MODID, "long_range_watch_closest",
                getWatchClosestLongRange);
        event.getRegistry().register(watchClosest);
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> addBeneficialSpells = (entity, choice) -> {
            IMobData mobData = MKUMobData.get(entity);
            return new EntityAIBuffTeammates(entity, mobData, .75f,
                    3 * GameConstants.TICKS_PER_SECOND);
        };
        AIGenerator beneficialSpells = new AIGenerator(MKUltra.MODID, "beneficial_spells", addBeneficialSpells);
        event.getRegistry().register(beneficialSpells);
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> addAggroTarget =
                (entity, choice) -> new EntityAINearestAttackableTargetMK((EntityCreature) entity,true);
        AIGenerator aggroTarget = new AIGenerator(MKUltra.MODID, "aggro_target", addAggroTarget);
        event.getRegistry().register(aggroTarget);
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> addOffensiveSpells = (entity, choice) -> {
            IMobData mobData = MKUMobData.get(entity);
            return new EntityAIRangedSpellAttack(entity, mobData, 3 * GameConstants.TICKS_PER_SECOND,
                    .25f, .75f);
        };
        AIGenerator offensiveSpells = new AIGenerator(MKUltra.MODID, "offensive_spells", addOffensiveSpells);
        event.getRegistry().register(offensiveSpells);
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> addLeashRange = (entity, choice) -> {
            IMobData mobData = MKUMobData.get(entity);
            return new EntityAIReturnToSpawn((EntityCreature)entity, mobData, 1.0);
        };
        AIGenerator leashRange = new AIGenerator(MKUltra.MODID, "leash_range", addLeashRange);
        event.getRegistry().register(leashRange);
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> addHurtTarget = (entity, choice) ->
                new EntityAIHurtByTargetMK((EntityCreature)entity, true);
        AIGenerator hurtTarget = new AIGenerator(MKUltra.MODID, "hurt_target", addHurtTarget);
        event.getRegistry().register(hurtTarget);
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> addAttackTarget = (entity, choice) ->
                new EntityAIAttackMeleeMK((EntityCreature)entity, 1.0, false);
        AIGenerator attackTarget = new AIGenerator(MKUltra.MODID, "attack_target", addAttackTarget);
        event.getRegistry().register(attackTarget);
    }


    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerAIModifiers(RegistryEvent.Register<AIModifier> event) {
        Log.info("Registering AI Modifiers");
        LoadingHelper.loadModsForType("/spawn/ai_modifiers", LoadingHelper::loadAIModifier, event);
        AIModifier remove_all_tasks = new AIModifier(
                new ResourceLocation(MKUltra.MODID, "remove_all_tasks"),
                AIModifiers.REMOVE_ALL_TASKS);
        event.getRegistry().register(remove_all_tasks);
        AIModifier remove_all_target_tasks = new AIModifier(
                new ResourceLocation(MKUltra.MODID, "remove_all_target_tasks"),
                AIModifiers.REMOVE_ALL_TARGET_TASKS);
        event.getRegistry().register(remove_all_target_tasks);

    }

}
