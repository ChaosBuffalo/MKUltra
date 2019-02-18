package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MKUMobData;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.chaosbuffalo.mkultra.core.mob_abilities.MobFireball;
import com.chaosbuffalo.mkultra.core.mob_abilities.ShadowDash;
import com.chaosbuffalo.mkultra.core.mob_abilities.TestHealDot;
import com.chaosbuffalo.mkultra.mob_ai.EntityAIBuffSelf;
import com.chaosbuffalo.mkultra.mob_ai.EntityAINearestAttackableTargetMK;
import com.chaosbuffalo.mkultra.mob_ai.EntityAIRangedSpellAttack;
import com.chaosbuffalo.mkultra.mob_ai.EntityAIReturnToSpawn;
import com.chaosbuffalo.mkultra.spawn.*;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
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
        ItemOption iron_weapons = new ItemOption(
                new ResourceLocation(MKUltra.MODID, "iron_weapons"),
                ItemAssigners.MAINHAND,
                new ItemChoice(new ItemStack(Items.IRON_SWORD, 1), 5, 0),
                new ItemChoice(new ItemStack(Items.IRON_AXE, 1), 5, 0));
        event.getRegistry().register(iron_weapons);
        ItemOption grunt_helm = new ItemOption(
                new ResourceLocation(MKUltra.MODID, "grunt_helm"),
                ItemAssigners.HEAD,
                new ItemChoice(new ItemStack(Items.LEATHER_HELMET, 1), 10, 0),
                new ItemChoice(new ItemStack(Items.IRON_HELMET, 1), 1, 0));
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
                new ItemChoice(new ItemStack(Items.LEATHER_HELMET, 1), 1, 0),
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
    public static void registerAttributeRanges(RegistryEvent.Register<AttributeRange> event) {
        AttributeRange grunt_health = new AttributeRange(
                new ResourceLocation(MKUltra.MODID, "grunt_health"),
                BaseSpawnAttributes.MAX_HEALTH, 20.0, 50.0);
        event.getRegistry().register(grunt_health);
        AttributeRange captain_health = new AttributeRange(
                new ResourceLocation(MKUltra.MODID, "captain_health"),
                BaseSpawnAttributes.MAX_HEALTH, 40.0, 120.0);
        event.getRegistry().register(captain_health);
        AttributeRange boss_health = new AttributeRange(
                new ResourceLocation(MKUltra.MODID, "boss_health"),
                BaseSpawnAttributes.MAX_HEALTH, 200.00, 500.0);
        event.getRegistry().register(boss_health);
        AttributeRange set_follow = new AttributeRange(
                new ResourceLocation(MKUltra.MODID, "follow_range"),
                BaseSpawnAttributes.FOLLOW_RANGE, 20.0, 20.0);
        event.getRegistry().register(set_follow);
        AttributeRange melee_aggro = new AttributeRange(
                new ResourceLocation(MKUltra.MODID, "melee_aggro"),
                MKSpawnAttributes.SET_AGGRO_RADIUS, 8.0, 8.0);
        event.getRegistry().register(melee_aggro);
        AttributeRange range_aggro = new AttributeRange(
                new ResourceLocation(MKUltra.MODID, "ranged_aggro"),
                MKSpawnAttributes.SET_AGGRO_RADIUS, 15.0, 15.0);
        event.getRegistry().register(range_aggro);
//        AttributeRange size_range = new AttributeRange(
//                new ResourceLocation(MKUltra.MODID, "test_size"),
//                BaseSpawnAttributes.SCALE_SIZE, 2.0, 2.0);
//        event.getRegistry().register(size_range);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerMobAbilities(RegistryEvent.Register<MobAbility> event) {
        MobAbility test_buff = new TestHealDot();
        event.getRegistry().register(test_buff);
        MobAbility shadow_dash = new ShadowDash();
        event.getRegistry().register(shadow_dash);
        MobAbility fireball = new MobFireball();
        event.getRegistry().register(fireball);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerMobDefinitions(RegistryEvent.Register<MobDefinition> event) {

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
                                new ResourceLocation(MKUltra.MODID, "mob_ability.test_heal_dot")))
                .withAIModifiers(
                        REMOVE_SKELETON_AI,
                        ADD_STANDARD_AI
                )
                .withMobName("Skeletal Grunt");
        event.getRegistry().register(skeletal_grunt);
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
                                new ResourceLocation(MKUltra.MODID, "mob_ability.shadow_dash")))
                .withAIModifiers(
                        REMOVE_SKELETON_AI,
                        ADD_STANDARD_AI
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
                                new ResourceLocation(MKUltra.MODID, "mob_ability.test_heal_dot")),
                        MKURegistry.getMobAbility(
                                new ResourceLocation(MKUltra.MODID, "mob_ability.fireball")
                        ))
                .withAIModifiers(
                        REMOVE_SKELETON_AI,
                        ADD_STANDARD_AI
                )
                .withMobName("Skeletal Mage");
        event.getRegistry().register(skeletal_mage);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerMobFactions(RegistryEvent.Register<MobFaction> event) {
        MobFaction skeleton_faction = new MobFaction(MKUltra.MODID, "skeletons");
        skeleton_faction.addSpawnList(MobFaction.MobGroups.MELEE_GRUNT, MKURegistry.getSpawnList(
                new ResourceLocation(MKUltra.MODID, "skeletal_grunts")), 1);
        skeleton_faction.addSpawnList(MobFaction.MobGroups.MELEE_CAPTAIN, MKURegistry.getSpawnList(
                new ResourceLocation(MKUltra.MODID, "skeletal_skulkers")), 1);
        skeleton_faction.addSpawnList(MobFaction.MobGroups.RANGE_GRUNT, MKURegistry.getSpawnList(
                new ResourceLocation(MKUltra.MODID, "skeletal_mages")), 1);
        event.getRegistry().register(skeleton_faction);

    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerSpawnLists(RegistryEvent.Register<SpawnList> event) {
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
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerAIModifiers(RegistryEvent.Register<AIModifier> event) {
        AIModifier remove_all_tasks = new AIModifier(
                new ResourceLocation(MKUltra.MODID, "remove_all_tasks"),
                AIModifiers.REMOVE_ALL_TASKS);
        event.getRegistry().register(remove_all_tasks);
        AIModifier remove_all_target_tasks = new AIModifier(
                new ResourceLocation(MKUltra.MODID, "remove_all_target_tasks"),
                AIModifiers.REMOVE_ALL_TARGET_TASKS);
        event.getRegistry().register(remove_all_target_tasks);
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> getWatchClosestLongRange =
                (entity, choice) -> new EntityAIWatchClosest(entity, EntityPlayer.class, 20.0F);
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> addSelfBuff = (entity, choice) -> {
            IMobData mobData = MKUMobData.get(entity);
            return new EntityAIBuffSelf(entity, mobData, .75f);
        };
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> addAggroTarget =
                (entity, choice) -> new EntityAINearestAttackableTargetMK((EntityCreature) entity,true);

        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> addOffensiveSpells = (entity, choice) -> {
            IMobData mobData = MKUMobData.get(entity);
            return new EntityAIRangedSpellAttack(entity, 3 * GameConstants.TICKS_PER_SECOND, mobData);
        };
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> addLeashRange = (entity, choice) -> {
            IMobData mobData = MKUMobData.get(entity);
            return new EntityAIReturnToSpawn((EntityCreature)entity, mobData, 1.0);
        };
        ADD_STANDARD_AI = new AIModifier(
                new ResourceLocation(MKUltra.MODID, "add_standard_ai"),
                AIModifiers.ADD_TASKS,
                new BehaviorChoice(addSelfBuff, 0, 3, BehaviorChoice.TaskType.TASK),
                new BehaviorChoice(addAggroTarget, 0, 2, BehaviorChoice.TaskType.TARGET_TASK),
                new BehaviorChoice(addOffensiveSpells, 0, 3, BehaviorChoice.TaskType.TASK),
                new BehaviorChoice(getWatchClosestLongRange, 0, 6, BehaviorChoice.TaskType.TASK),
                new BehaviorChoice(addLeashRange, 0, 2, BehaviorChoice.TaskType.TASK)
        );
        event.getRegistry().register(ADD_STANDARD_AI);

        REMOVE_SKELETON_AI = new AIModifier(
                new ResourceLocation(MKUltra.MODID, "remove_wander"),
                AIModifiers.REMOVE_AI,
                new BehaviorChoice(EntityAIWanderAvoidWater.class, 0, BehaviorChoice.TaskType.TASK),
                new BehaviorChoice(EntityAINearestAttackableTarget.class, 0, BehaviorChoice.TaskType.TARGET_TASK),
                new BehaviorChoice(EntityAIWatchClosest.class, 0, BehaviorChoice.TaskType.TASK));
        event.getRegistry().register(REMOVE_SKELETON_AI);

    }

}
