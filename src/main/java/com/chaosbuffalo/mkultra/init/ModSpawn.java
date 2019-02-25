package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MKUMobData;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.core.mob_abilities.*;
import com.chaosbuffalo.mkultra.utils.JsonLoader;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.mob_ai.*;
import com.chaosbuffalo.mkultra.spawn.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;


@Mod.EventBusSubscriber
public class ModSpawn {
    public static final int MAX_LEVEL = 10;

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerItemOptions(RegistryEvent.Register<ItemOption> event) {
        Log.info("Registering Item Options");
        JsonLoader.loadModsForType("spawn/item_options", ModSpawn::loadItemOption, event);
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
        JsonLoader.loadModsForType("spawn/attributes", ModSpawn::loadAttribute, event);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerMobAbilities(RegistryEvent.Register<MobAbility> event) {
        Log.info("Registering Mob Abilities");
        event.getRegistry().register(new MobNaturesRemedy());
        event.getRegistry().register(new ShadowDash());
        event.getRegistry().register(new MobFireball());
        event.getRegistry().register(new MobFireArrow());
        event.getRegistry().register(new MobFlameBlade());
        event.getRegistry().register(new MobHeal());
        event.getRegistry().register(new GraspingRoots());
        event.getRegistry().register(new ManaBurn());
        event.getRegistry().register(new TripleFireball());
        event.getRegistry().register(new MobFlameWave());
        event.getRegistry().register(new FullHeal());
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerMobDefinitions(RegistryEvent.Register<MobDefinition> event) {
        Log.info("Registering Mob Definitions");
        JsonLoader.loadModsForType("spawn/mob_definitions", ModSpawn::loadMobDefinition, event);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerMobFactions(RegistryEvent.Register<MobFaction> event) {
        Log.info("Registering Mob Factions");
        JsonLoader.loadModsForType("spawn/mob_factions", ModSpawn::loadMobFactions, event);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerSpawnLists(RegistryEvent.Register<SpawnList> event) {
        Log.info("Registering Spawn Lists");
        JsonLoader.loadModsForType("spawn/spawn_lists", ModSpawn::loadSpawnList, event);
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
            return new EntityAIBuffTeammates(entity, mobData,
                    (11 - mobData.getMobLevel()) * GameConstants.TICKS_PER_SECOND);
        };
        AIGenerator beneficialSpells = new AIGenerator(MKUltra.MODID, "beneficial_spells", addBeneficialSpells);
        event.getRegistry().register(beneficialSpells);
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> addNoStrafeBuffs = (entity, choice) -> {
            IMobData mobData = MKUMobData.get(entity);
            if (mobData == null){
                return null;
            }
            EntityAIBuffTeammates ai = new EntityAIBuffTeammates(entity, mobData,
                    (6 - mobData.getMobLevel() / 2) * GameConstants.TICKS_PER_SECOND);
            ai.setStrafe(false);
            return ai;
        };
        AIGenerator noStrafeBuffs = new AIGenerator(MKUltra.MODID, "no_strafe_buffs", addNoStrafeBuffs);
        event.getRegistry().register(noStrafeBuffs);
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> addAggroTarget =
                (entity, choice) -> new EntityAINearestAttackableTargetMK((EntityCreature) entity,true);
        AIGenerator aggroTarget = new AIGenerator(MKUltra.MODID, "aggro_target", addAggroTarget);
        event.getRegistry().register(aggroTarget);
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> addOffensiveSpells = (entity, choice) -> {
            IMobData mobData = MKUMobData.get(entity);
            if (mobData == null){
                return null;
            }
            return new EntityAISpellAttack(entity, mobData,
                    (6 - mobData.getMobLevel() / 2) * GameConstants.TICKS_PER_SECOND,
                    .25f, .75f);
        };
        AIGenerator offensiveSpells = new AIGenerator(MKUltra.MODID, "offensive_spells", addOffensiveSpells);
        event.getRegistry().register(offensiveSpells);
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> addNoStrafeSpells = (entity, choice) -> {
            IMobData mobData = MKUMobData.get(entity);
            if (mobData == null){
                return null;
            }
            EntityAISpellAttack ai = new EntityAISpellAttack(entity, mobData,
                    (6 - mobData.getMobLevel() / 2) * GameConstants.TICKS_PER_SECOND,
                    .25f, .75f);
            ai.setStrafe(false);
            return ai;
        };
        AIGenerator noStrafespells = new AIGenerator(MKUltra.MODID, "no_strafe_spells", addNoStrafeSpells);
        event.getRegistry().register(noStrafespells);
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
        JsonLoader.loadModsForType("spawn/ai_modifiers", ModSpawn::loadAIModifier, event);
        AIModifier remove_all_tasks = new AIModifier(
                new ResourceLocation(MKUltra.MODID, "remove_all_tasks"),
                AIModifiers.REMOVE_ALL_TASKS);
        event.getRegistry().register(remove_all_tasks);
        AIModifier remove_all_target_tasks = new AIModifier(
                new ResourceLocation(MKUltra.MODID, "remove_all_target_tasks"),
                AIModifiers.REMOVE_ALL_TARGET_TASKS);
        event.getRegistry().register(remove_all_target_tasks);

    }


    public static void loadAttribute(ResourceLocation name, JsonObject obj,
                                     RegistryEvent.Register<AttributeRange> event) {
        String[] keys = {"setter", "min_value", "max_value"};
        if (!checkKeysExist(keys, obj)) {
            return;
        }
        AttributeRange range = new AttributeRange(name, MKURegistry.getAttributeSetter(
                new ResourceLocation(obj.get("setter").getAsString())),
                obj.get("min_value").getAsDouble(), obj.get("max_value").getAsDouble());
        event.getRegistry().register(range);
    }

    public static boolean checkKeysExist(String[] keys, JsonObject obj) {
        for (String key : keys) {
            if (!obj.has(key)) {
                Log.info("Skipping load for %s because %s not present", obj.toString(), key);
                return false;
            }
        }
        return true;
    }

    public static BiConsumer<EntityLivingBase, ItemChoice> getItemAssigner(String type) {
        switch (type) {
            case "MAINHAND":
                return ItemAssigners.MAINHAND;
            case "OFFHAND":
                return ItemAssigners.OFFHAND;
            case "FEET":
                return ItemAssigners.FEET;
            case "LEGS":
                return ItemAssigners.LEGS;
            case "CHEST":
                return ItemAssigners.CHEST;
            case "HEAD":
                return ItemAssigners.HEAD;
            default:
                return null;
        }
    }

    public static BiConsumer<EntityLivingBase, AIModifier> getAIModifier(String type) {
        switch (type) {
            case "ADD_TASKS":
                return AIModifiers.ADD_TASKS;
            case "REMOVE_AI":
                return AIModifiers.REMOVE_AI;
            case "REMOVE_ALL_TARGET_TASKS":
                return AIModifiers.REMOVE_ALL_TARGET_TASKS;
            case "REMOVE_ALL_TASKS":
                return AIModifiers.REMOVE_ALL_TASKS;
            default:
                return null;
        }
    }

    public static BehaviorChoice.TaskType getTaskType(String type) {
        switch (type) {
            case "TASK":
                return BehaviorChoice.TaskType.TASK;
            case "TARGET_TASK":
                return BehaviorChoice.TaskType.TARGET_TASK;
            default:
                return null;
        }
    }

    public static void loadItemOption(ResourceLocation name, JsonObject obj,
                                      RegistryEvent.Register<ItemOption> event) {
        String[] keys = {"slot", "choices"};
        if (!checkKeysExist(keys, obj)) {
            return;
        }
        BiConsumer<EntityLivingBase, ItemChoice> assigner = getItemAssigner(obj.get("slot").getAsString());
        if (assigner == null) {
            Log.info("Item assigner %s not a valid option. Skipping load for: %s", obj.get("slot").getAsString(),
                    name);
            return;
        }
        String[] choiceKeys = {"weight", "item", "min_level"};
        ArrayList<ItemChoice> choices = new ArrayList<>();
        for (JsonElement subEle : obj.getAsJsonArray("choices")) {
            JsonObject jsonObject = subEle.getAsJsonObject();
            if (!checkKeysExist(choiceKeys, jsonObject)) {
                continue;
            }
            float dropChance = .05f;
            if (jsonObject.has("drop_chance")) {
                dropChance = jsonObject.get("drop_chance").getAsFloat();
            }
            String itemName = jsonObject.get("item").getAsString();
            Item item;
            if (itemName.equals("EMPTY")) {
                item = null;
            } else {
                item = Item.REGISTRY.getObject(new ResourceLocation(itemName));
            }
            if (item == null && !itemName.equals("EMPTY")) {
                Log.info("Failed to load item for %s", jsonObject.get("item").getAsString());
                continue;
            } else {
                ItemStack itemStack;
                if (item == null) {
                    itemStack = ItemStack.EMPTY;
                } else {
                    itemStack = new ItemStack(item, 1);
                }
                ItemChoice choice = new ItemChoice(itemStack,
                        jsonObject.get("weight").getAsDouble(),
                        jsonObject.get("min_level").getAsInt(),
                        dropChance);
                choices.add(choice);
            }
        }
        ItemOption option = new ItemOption(name, assigner, choices.toArray(new ItemChoice[0]));
        event.getRegistry().register(option);
    }

    public static MobFaction.MobGroups getMobGroupFromString(String mobGroupIn) {
        switch (mobGroupIn) {
            case "RANGE_GRUNT":
                return MobFaction.MobGroups.RANGE_GRUNT;
            case "MELEE_GRUNT":
                return MobFaction.MobGroups.MELEE_GRUNT;
            case "SUPPORT_GRUNT":
                return MobFaction.MobGroups.SUPPORT_GRUNT;
            case "MELEE_CAPTAIN":
                return MobFaction.MobGroups.MELEE_CAPTAIN;
            case "RANGE_CAPTAIN":
                return MobFaction.MobGroups.RANGE_CAPTAIN;
            case "SUPPORT_CAPTAIN":
                return MobFaction.MobGroups.SUPPORT_CAPTAIN;
            case "BOSS":
                return MobFaction.MobGroups.BOSS;
            default:
                return null;
        }
    }

    public static void loadMobFactions(ResourceLocation name, JsonObject obj,
                                       RegistryEvent.Register<MobFaction> event) {
        String[] groups = {"RANGE_GRUNT", "MELEE_GRUNT", "SUPPORT_GRUNT", "RANGE_CAPTAIN",
                "MELEE_CAPTAIN", "SUPPORT_CAPTAIN", "BOSS"};
        MobFaction faction = new MobFaction(name);
        for (String key : groups) {
            if (obj.has(key)) {
                JsonArray options = obj.get(key).getAsJsonArray();
                for (JsonElement ele : options) {
                    JsonObject jsonObject = ele.getAsJsonObject();
                    String[] keys = {"spawn_list", "weight"};
                    if (!checkKeysExist(keys, jsonObject)) {
                        continue;
                    }
                    SpawnList spawnList = MKURegistry.getSpawnList(new ResourceLocation(jsonObject.get("spawn_list")
                            .getAsString()));
                    if (spawnList == null) {
                        Log.info("Error loading Spawn List: %s for Faction: %s", jsonObject.get("spawn_list")
                                .getAsString(), name);
                        continue;
                    }
                    MobFaction.MobGroups group = getMobGroupFromString(key);
                    if (group == null) {
                        Log.info("Incorrect group name for faction %s", key);
                        continue;
                    }
                    faction.addSpawnList(group, spawnList, jsonObject.get("weight").getAsDouble());
                }
            }
        }
        event.getRegistry().register(faction);
    }

    public static void loadSpawnList(ResourceLocation name, JsonObject obj,
                                     RegistryEvent.Register<SpawnList> event) {
        String[] keys = {"options"};
        if (!checkKeysExist(keys, obj)) {
            return;
        }
        JsonArray options = obj.get("options").getAsJsonArray();
        ArrayList<MobChoice> choices = new ArrayList<>();
        for (JsonElement ele : options) {
            JsonObject jsonObject = ele.getAsJsonObject();
            String[] optionkeys = {"definition", "weight"};
            if (!checkKeysExist(optionkeys, jsonObject)) {
                continue;
            }
            MobDefinition definition = MKURegistry.getMobDefinition(
                    new ResourceLocation(jsonObject.get("definition").getAsString()));
            if (definition == null) {
                Log.info("Could not load mob definition for %s", jsonObject.get("definition").getAsString());
                continue;
            }
            MobChoice mobChoice = new MobChoice(definition, jsonObject.get("weight").getAsDouble());
            choices.add(mobChoice);
        }
        SpawnList spawnList = new SpawnList(name).withOptions(choices.toArray(new MobChoice[0]));
        event.getRegistry().register(spawnList);

    }

    public static void loadMobDefinition(ResourceLocation name, JsonObject obj,
                                         RegistryEvent.Register<MobDefinition> event) {
        String[] keys = {"type"};
        if (!checkKeysExist(keys, obj)) {
            return;
        }
        try {
            Class mobClass = Class.forName(obj.get("type").getAsString());
            if (EntityLivingBase.class.isAssignableFrom(mobClass)) {
                MobDefinition definition = new MobDefinition(name, mobClass);
                if (obj.has("attributes")) {
                    JsonArray attributeList = obj.get("attributes").getAsJsonArray();
                    ArrayList<AttributeRange> ranges = new ArrayList<>();
                    for (JsonElement ele : attributeList) {
                        String attributeName = ele.getAsString();
                        AttributeRange range = MKURegistry.getAttributeRange(new ResourceLocation(attributeName));
                        if (range != null) {
                            ranges.add(range);
                        } else {
                            Log.info("Error finding attribute range for: %s", attributeName);
                        }
                    }
                    definition.withAttributeRanges(ranges.toArray(new AttributeRange[0]));
                }
                if (obj.has("item_options")) {
                    JsonArray item_options = obj.get("item_options").getAsJsonArray();
                    ArrayList<ItemOption> options = new ArrayList<>();
                    for (JsonElement ele : item_options) {
                        String option_name = ele.getAsString();
                        ItemOption option = MKURegistry.getItemOption(new ResourceLocation(option_name));
                        if (option != null) {
                            options.add(option);
                        } else {
                            Log.info("Error finding ItemOption for: %s", option_name);
                        }
                    }
                    definition.withItemOptions(options.toArray(new ItemOption[0]));
                }
                if (obj.has("abilities")) {
                    JsonArray json_options = obj.get("abilities").getAsJsonArray();
                    ArrayList<MobAbility> options = new ArrayList<>();
                    for (JsonElement ele : json_options) {
                        String option_name = ele.getAsString();
                        MobAbility option = MKURegistry.getMobAbility(new ResourceLocation(option_name));
                        if (option != null) {
                            options.add(option);
                        } else {
                            Log.info("Error finding MobAbility for: %s", option_name);
                        }
                    }
                    definition.withAbilities(options.toArray(new MobAbility[0]));
                }
                if (obj.has("ai_modifiers")) {
                    JsonArray json_options = obj.get("ai_modifiers").getAsJsonArray();
                    ArrayList<AIModifier> options = new ArrayList<>();
                    for (JsonElement ele : json_options) {
                        String option_name = ele.getAsString();
                        AIModifier option = MKURegistry.getAIModifier(new ResourceLocation(option_name));
                        if (option != null) {
                            options.add(option);
                        } else {
                            Log.info("Error finding AI Modifier for: %s", option_name);
                        }
                    }
                    definition.withAIModifiers(options.toArray(new AIModifier[0]));
                }
                if (obj.has("name")){
                    String mobName = obj.get("name").getAsString();
                    definition.withMobName(mobName);
                }
                if (obj.has("custom_modifiers")){
                    JsonArray json_modifiers = obj.get("custom_modifiers").getAsJsonArray();
                    ArrayList<CustomModifier> modifiers = new ArrayList<>();
                    for (JsonElement ele : json_modifiers){
                        JsonObject jsonObject = ele.getAsJsonObject();
                        String[] customKeys = {"setter"};
                        if (!checkKeysExist(customKeys, jsonObject)) {
                            continue;
                        }
                        CustomSetter setter = MKURegistry.getCustomSetter(
                                new ResourceLocation(jsonObject.get("setter").getAsString()));
                        if (setter == null){
                            Log.info("Could not find custom setter with name %s, skipping load.",
                                    jsonObject.get("setter").getAsString());
                            continue;
                        }
                        CustomModifier mod = setter.loadFromJson(jsonObject);
                        if (mod == null){
                            continue;
                        }
                        modifiers.add(mod);
                    }
                    definition.withCustomModifiers(modifiers.toArray(new CustomModifier[0]));
                }
                event.getRegistry().register(definition);

            } else {
                Log.info("%s Class not an EntityLivingBase skipping mob definition %s",
                        mobClass.toString(), obj.toString());
            }
        } catch (ClassNotFoundException e) {
            Log.info("Type not found for entity: %s, skipping mob definition: %s",
                    obj.get("type").getAsString(),
                    obj.toString());
        }
    }

    public static void loadAIModifier(ResourceLocation name, JsonObject obj,
                                      RegistryEvent.Register<AIModifier> event) {
        String[] keys = {"type", "choices"};
        if (!checkKeysExist(keys, obj)) {
            return;
        }
        String type = obj.get("type").getAsString();
        BiConsumer<EntityLivingBase, AIModifier> modifier = getAIModifier(type);
        if (modifier == null) {
            Log.info("AI Modifier type %s not a valid option. Skipping load for: %s", obj.get("slot").getAsString(),
                    name);
            return;
        }
        switch (type) {
            case "ADD_TASKS": {
                String[] choiceKeys = {"task_type", "generator", "min_level", "priority"};
                ArrayList<BehaviorChoice> choices = new ArrayList<>();
                for (JsonElement subEle : obj.getAsJsonArray("choices")) {
                    JsonObject jsonObject = subEle.getAsJsonObject();
                    if (!checkKeysExist(choiceKeys, jsonObject)) {
                        continue;
                    }
                    BehaviorChoice.TaskType taskType = getTaskType(jsonObject.get("task_type").getAsString());
                    if (taskType == null) {
                        Log.info("Failed to load behavior choice %s, task type: %s not valid.", jsonObject.toString(),
                                jsonObject.get("task_type").getAsString());
                        return;
                    }
                    BehaviorChoice choice = new BehaviorChoice(
                            MKURegistry.getAIGenerator(new ResourceLocation(jsonObject.get("generator").getAsString())),
                            jsonObject.get("min_level").getAsInt(),
                            jsonObject.get("priority").getAsInt(),
                            taskType);
                    choices.add(choice);
                }
                Log.info("registering add task %s", name);
                AIModifier ai_modifier = new AIModifier(name, modifier, choices.toArray(new BehaviorChoice[0]));
                event.getRegistry().register(ai_modifier);
                break;
            }
            case "REMOVE_ALL_TARGET_TASKS":
            case "REMOVE_ALL_TASKS": {
                AIModifier ai_modifier = new AIModifier(name, modifier);
                event.getRegistry().register(ai_modifier);
                break;
            }
            case "REMOVE_AI": {
                String[] choiceKeys = {"task_class", "min_level", "task_type"};
                ArrayList<BehaviorChoice> choices = new ArrayList<>();
                for (JsonElement subEle : obj.getAsJsonArray("choices")) {
                    JsonObject jsonObject = subEle.getAsJsonObject();
                    if (!checkKeysExist(choiceKeys, jsonObject)) {
                        continue;
                    }
                    BehaviorChoice.TaskType taskType = getTaskType(jsonObject.get("task_type").getAsString());
                    if (taskType == null) {
                        Log.info("Failed to load behavior choice %s, task type: %s not valid.", jsonObject.toString(),
                                jsonObject.get("task_type").getAsString());
                        return;
                    }
                    try {
                        Log.info("Trying to get %s", jsonObject.get("task_class"));
                        Class taskClass = Class.forName(jsonObject.get("task_class").getAsString());
                        if (EntityAIBase.class.isAssignableFrom(taskClass)) {
                            BehaviorChoice choice = new BehaviorChoice(
                                    taskClass,
                                    jsonObject.get("min_level").getAsInt(),
                                    taskType);
                            choices.add(choice);
                        }

                    } catch (ClassNotFoundException e) {
                        Log.info("Task not found for class: %s, skipping behavior %s",
                                jsonObject.get("task_class").getAsString(),
                                jsonObject.toString());
                    }
                }
                Log.info("registering remove task %s", name);
                AIModifier remove_task = new AIModifier(name, AIModifiers.REMOVE_AI,
                        choices.toArray(new BehaviorChoice[0]));
                event.getRegistry().register(remove_task);
            }

        }
    }
}
