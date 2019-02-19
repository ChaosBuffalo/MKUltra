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
    public static AIModifier ADD_STANDARD_AI;
    public static AIModifier REMOVE_SKELETON_AI;

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerItemOptions(RegistryEvent.Register<ItemOption> event) {
        Log.info("Registering Item Options");
        LoadingHelper.loadModsForType("/spawn/item_options", LoadingHelper::loadItemOption, event);
        ItemOption bow = new ItemOption(
                new ResourceLocation(MKUltra.MODID, "bow"),
                ItemAssigners.MAINHAND,
                new ItemChoice(new ItemStack(Items.BOW, 1), 5, 0));
        event.getRegistry().register(bow);
        ItemOption grunt_helm = new ItemOption(
                new ResourceLocation(MKUltra.MODID, "grunt_helm"),
                ItemAssigners.HEAD,
                new ItemChoice(new ItemStack(Items.IRON_HELMET, 1), 5, 0));
        event.getRegistry().register(grunt_helm);
        ItemOption grunt_chest = new ItemOption(
                new ResourceLocation(MKUltra.MODID, "grunt_chest"),
                ItemAssigners.CHEST,
                new ItemChoice(new ItemStack(Items.LEATHER_CHESTPLATE, 1), 5, 0),
                new ItemChoice(ItemStack.EMPTY, 10, 0));
        event.getRegistry().register(grunt_chest);
        ItemOption captain_helm = new ItemOption(
                new ResourceLocation(MKUltra.MODID, "captain_helm"),
                ItemAssigners.HEAD,
                new ItemChoice(new ItemStack(Items.IRON_HELMET, 1), 7, 0));
        event.getRegistry().register(captain_helm);
        ItemOption captain_chest = new ItemOption(
                new ResourceLocation(MKUltra.MODID, "captain_chest"),
                ItemAssigners.CHEST,
                new ItemChoice(new ItemStack(Items.LEATHER_CHESTPLATE, 1), 5, 0));
        event.getRegistry().register(captain_chest);

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
        AttributeRange boss_health = new AttributeRange(
                new ResourceLocation(MKUltra.MODID, "boss_health"),
                MKURegistry.getAttributeSetter(new ResourceLocation(MKUltra.MODID, "max_health")),
                200.00, 500.0);
        event.getRegistry().register(boss_health);
        AttributeRange set_follow = new AttributeRange(
                new ResourceLocation(MKUltra.MODID, "follow_range"),
                MKURegistry.getAttributeSetter(new ResourceLocation(MKUltra.MODID, "follow_range")),
                20.0, 20.0);
        event.getRegistry().register(set_follow);
        AttributeRange melee_aggro = new AttributeRange(
                new ResourceLocation(MKUltra.MODID, "melee_aggro"),
                MKURegistry.getAttributeSetter(new ResourceLocation(MKUltra.MODID, "aggro_range")),
                10.0, 10.0);
        event.getRegistry().register(melee_aggro);
        AttributeRange range_aggro = new AttributeRange(
                new ResourceLocation(MKUltra.MODID, "ranged_aggro"),
                MKURegistry.getAttributeSetter(new ResourceLocation(MKUltra.MODID, "aggro_range")),
                16.0, 16.0);
        event.getRegistry().register(range_aggro);
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
        MobDefinition skeletal_grunt =  new MobDefinition(
                new ResourceLocation(MKUltra.MODID, "skeletal_grunt"),
                EntitySkeleton.class, 10)
                .withAttributeRanges(
                        MKURegistry.getAttributeRange(
                                new ResourceLocation(MKUltra.MODID, "grunt_health")),
                        MKURegistry.getAttributeRange(
                                new ResourceLocation(MKUltra.MODID, "melee_aggro"))
                )
                .withItemOptions(
                        MKURegistry.getItemOption(
                                new ResourceLocation(MKUltra.MODID, "iron_weapons")),
                        MKURegistry.getItemOption(
                                new ResourceLocation(MKUltra.MODID, "grunt_helm")),
                        MKURegistry.getItemOption(
                                new ResourceLocation(MKUltra.MODID, "grunt_chest")))
                .withAbilities(
                        MKURegistry.getMobAbility(
                                new ResourceLocation(MKUltra.MODID, "mob_ability.natures_remedy")))
                .withAIModifiers(
                        MKURegistry.getAIModifier(new ResourceLocation(MKUltra.MODID, "remove_skeleton_ai")),
                        MKURegistry.getAIModifier(new ResourceLocation(MKUltra.MODID, "add_standard_ai"))
                )
                .withMobName("Skeletal Grunt");
        event.getRegistry().register(skeletal_grunt);
        MobDefinition skeletal_archer =  new MobDefinition(
                new ResourceLocation(MKUltra.MODID, "skeletal_archer"),
                EntitySkeleton.class, 10)
                .withAttributeRanges(
                        MKURegistry.getAttributeRange(
                                new ResourceLocation(MKUltra.MODID, "grunt_health")),
                        MKURegistry.getAttributeRange(
                                new ResourceLocation(MKUltra.MODID, "ranged_aggro"))
                )
                .withItemOptions(
                        MKURegistry.getItemOption(
                                new ResourceLocation(MKUltra.MODID, "bow")),
                        MKURegistry.getItemOption(
                                new ResourceLocation(MKUltra.MODID, "grunt_helm")),
                        MKURegistry.getItemOption(
                                new ResourceLocation(MKUltra.MODID, "grunt_chest")))
                .withAbilities(
                        MKURegistry.getMobAbility(
                                new ResourceLocation(MKUltra.MODID, "mob_ability.fire_arrow")))
                .withAIModifiers(
                        MKURegistry.getAIModifier(new ResourceLocation(MKUltra.MODID, "remove_skeleton_ai")),
                        MKURegistry.getAIModifier(new ResourceLocation(MKUltra.MODID, "add_standard_ai"))
                )
                .withMobName("Skeletal Archer");
        event.getRegistry().register(skeletal_archer);
        MobDefinition skeletal_skulker =  new MobDefinition(
                new ResourceLocation(MKUltra.MODID, "skeletal_skulker"),
                EntitySkeleton.class, 10)
                .withAttributeRanges(
                        MKURegistry.getAttributeRange(
                                new ResourceLocation(MKUltra.MODID, "captain_health")),
                        MKURegistry.getAttributeRange(
                                new ResourceLocation(MKUltra.MODID, "melee_aggro"))
                )
                .withItemOptions(
                        MKURegistry.getItemOption(
                                new ResourceLocation(MKUltra.MODID, "iron_weapons")),
                        MKURegistry.getItemOption(
                                new ResourceLocation(MKUltra.MODID, "captain_helm")),
                        MKURegistry.getItemOption(
                                new ResourceLocation(MKUltra.MODID, "captain_chest")))
                .withAbilities(
                        MKURegistry.getMobAbility(
                                new ResourceLocation(MKUltra.MODID, "mob_ability.shadow_dash")),
                        MKURegistry.getMobAbility(
                                new ResourceLocation(MKUltra.MODID, "mob_ability.natures_remedy")))
                .withAIModifiers(
                        MKURegistry.getAIModifier(new ResourceLocation(MKUltra.MODID, "remove_skeleton_ai")),
                        MKURegistry.getAIModifier(new ResourceLocation(MKUltra.MODID, "add_standard_ai"))
                )
                .withMobName("Skeletal Skulker");
        event.getRegistry().register(skeletal_skulker);
        MobDefinition skeletal_mage =  new MobDefinition(
                new ResourceLocation(MKUltra.MODID, "skeletal_mage"),
                EntitySkeleton.class, 10)
                .withAttributeRanges(
                        MKURegistry.getAttributeRange(
                                new ResourceLocation(MKUltra.MODID, "grunt_health")),
                        MKURegistry.getAttributeRange(
                                new ResourceLocation(MKUltra.MODID, "ranged_aggro"))
                )
                .withItemOptions(
                        MKURegistry.getItemOption(
                                new ResourceLocation(MKUltra.MODID, "grunt_helm")),
                        MKURegistry.getItemOption(
                                new ResourceLocation(MKUltra.MODID, "grunt_chest")))
                .withAbilities(
                        MKURegistry.getMobAbility(
                                new ResourceLocation(MKUltra.MODID, "mob_ability.natures_remedy")),
                        MKURegistry.getMobAbility(
                                new ResourceLocation(MKUltra.MODID, "mob_ability.fireball")
                        ))
                .withAIModifiers(
                        MKURegistry.getAIModifier(new ResourceLocation(MKUltra.MODID, "remove_skeleton_ai")),
                        MKURegistry.getAIModifier(new ResourceLocation(MKUltra.MODID, "add_standard_ai"))
                )
                .withMobName("Skeletal Mage");
        event.getRegistry().register(skeletal_mage);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerMobFactions(RegistryEvent.Register<MobFaction> event) {
        Log.info("Registering Mob Factions");
        MobFaction skeleton_faction = new MobFaction(MKUltra.MODID, "skeletons");
        skeleton_faction.addSpawnList(MobFaction.MobGroups.MELEE_GRUNT, MKURegistry.getSpawnList(
                new ResourceLocation(MKUltra.MODID, "skeletal_grunts")), 1);
        skeleton_faction.addSpawnList(MobFaction.MobGroups.MELEE_CAPTAIN, MKURegistry.getSpawnList(
                new ResourceLocation(MKUltra.MODID, "skeletal_skulkers")), 1);
        skeleton_faction.addSpawnList(MobFaction.MobGroups.RANGE_GRUNT, MKURegistry.getSpawnList(
                new ResourceLocation(MKUltra.MODID, "skeletal_mages")), 1);
        skeleton_faction.addSpawnList(MobFaction.MobGroups.RANGE_GRUNT, MKURegistry.getSpawnList(
                new ResourceLocation(MKUltra.MODID, "skeletal_archers")), 1);
        event.getRegistry().register(skeleton_faction);

    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerSpawnLists(RegistryEvent.Register<SpawnList> event) {
        Log.info("Registering Spawn Lists");
        SpawnList skeletal_grunts = new SpawnList(new ResourceLocation(MKUltra.MODID, "skeletal_grunts"));
        skeletal_grunts.addOption(MKURegistry.getMobDefinition(
                new ResourceLocation(MKUltra.MODID, "skeletal_grunt")));
        event.getRegistry().register(skeletal_grunts);
        SpawnList skeletal_skulkers = new SpawnList(new ResourceLocation(MKUltra.MODID, "skeletal_skulkers"));
        skeletal_skulkers.addOption(MKURegistry.getMobDefinition(
                new ResourceLocation(MKUltra.MODID, "skeletal_skulker")));
        event.getRegistry().register(skeletal_skulkers);
        SpawnList skeletal_mages = new SpawnList(new ResourceLocation(MKUltra.MODID, "skeletal_mages"));
        skeletal_mages.addOption(MKURegistry.getMobDefinition(
                new ResourceLocation(MKUltra.MODID, "skeletal_mage")));
        event.getRegistry().register(skeletal_mages);
        SpawnList skeletal_archers = new SpawnList(new ResourceLocation(MKUltra.MODID, "skeletal_archers"));
        skeletal_archers.addOption(MKURegistry.getMobDefinition(
                new ResourceLocation(MKUltra.MODID, "skeletal_archer")));
        event.getRegistry().register(skeletal_archers);
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
