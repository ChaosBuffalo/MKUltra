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
import com.chaosbuffalo.mkultra.utils.RandomCollection;
import com.chaosbuffalo.targeting_api.Targeting;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import java.io.File;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;


@Mod.EventBusSubscriber
public class ModSpawn {
    public static final int MAX_LEVEL = 10;
    public static final ResourceLocation NPC_FACTION_NAME = new ResourceLocation(MKUltra.MODID, "npcs");
    public static final MobFaction NPC_FACTION = new MobFaction(NPC_FACTION_NAME, true);

    public static final RandomCollection<Integer> levelChances = new RandomCollection<>();
    static {
        levelChances.add(8.0, 1);
        levelChances.add(6.0, 2);
        levelChances.add(10.0, 3);
        levelChances.add(9.0, 4);
        levelChances.add(7.0, 5);
        levelChances.add(5.0, 6);
        levelChances.add(4.0, 7);
        levelChances.add(3.0, 8);
        levelChances.add(2.0, 9);
        levelChances.add(1.0, 10);
    }

    public static void addMobDefinitionsToDefaultSpawnIndex(){
        for (MobDefinition def : MKURegistry.REGISTRY_MOB_DEF) {
            if (def.getCanDefaultSpawn()) {
                DefaultSpawnIndex.addSpawn(def.getRegistryName(), def, def.getDefaultSpawnWeight());
                Log.info("%s  added to Default Spawn Index with weight %f", def.getRegistryName().toString(),
                        def.getDefaultSpawnWeight());
            }
        }
    }

    public static void postInitJsonRegisistation(){
        ModContainer old = Loader.instance().activeModContainer();
        JsonLoader.loadModsForType("mk_spawn" + File.separator + "attributes",
                "mk_overrides", "assets",
                ModSpawn::loadAttribute, MKURegistry.REGISTRY_MOB_ATTRS);
        JsonLoader.loadModsForType("mk_spawn" + File.separator + "item_options",
                "mk_overrides", "assets",
                ModSpawn::loadItemOption, MKURegistry.REGISTRY_MOB_ITEMS);
        JsonLoader.loadModsForType("mk_spawn" + File.separator + "ai_modifiers",
                "mk_overrides", "assets",
                ModSpawn::loadAIModifier, MKURegistry.REGISTRY_MOB_AI_MODS);
        JsonLoader.loadModsForType("mk_spawn" + File.separator + "mob_definitions",
                "mk_overrides", "assets",
                ModSpawn::loadMobDefinition, MKURegistry.REGISTRY_MOB_DEF);
        JsonLoader.loadModsForType("mk_spawn" + File.separator + "spawn_lists",
                "mk_overrides", "assets",
                ModSpawn::loadSpawnList, MKURegistry.REGISTRY_SPAWN_LISTS);
        JsonLoader.loadModsForType("mk_spawn" + File.separator + "mob_factions",
                "mk_overrides", "assets",
                ModSpawn::loadMobFactions, MKURegistry.REGISTRY_MOB_FACTIONS);
        addMobDefinitionsToDefaultSpawnIndex();
        Loader.instance().setActiveModContainer(old);
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
    public static void registerMobAbilities(RegistryEvent.Register<MobAbility> event) {
        Log.info("Registering Mob Abilities");
        event.getRegistry().register(new NaturesRemedy());
        event.getRegistry().register(new ShadowDash());
        event.getRegistry().register(new Fireball());
        event.getRegistry().register(new FireArrow());
        event.getRegistry().register(new FlameBlade());
        event.getRegistry().register(new Heal());
        event.getRegistry().register(new GraspingRoots());
        event.getRegistry().register(new ManaBurn());
        event.getRegistry().register(new TripleFireball());
        event.getRegistry().register(new FlameWave());
        event.getRegistry().register(new FullHeal());
        event.getRegistry().register(new Drown());
        event.getRegistry().register(new PoisonArrow());
        event.getRegistry().register(new PowerWordSummon());
        event.getRegistry().register(new WarpDash());
        event.getRegistry().register(new Whirlpool());
        event.getRegistry().register(new Repulse());
        event.getRegistry().register(new FireImmunity());
        event.getRegistry().register(new GraspingTouch());
        event.getRegistry().register(new HungerLeech());
        event.getRegistry().register(new ManaLeech());
        event.getRegistry().register(new MobFuriousBrooding());
        event.getRegistry().register(new MobHaste());
        event.getRegistry().register(new MobSprint());
        event.getRegistry().register(new MobYank());
        event.getRegistry().register(new WildToxinTouch());
        event.getRegistry().register(new WitherTouch());
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
                (entity, choice) -> new EntityAINearestAttackableTargetMK((EntityCreature) entity,true,
                        Targeting.TargetType.PLAYERS);
        AIGenerator aggroTarget = new AIGenerator(MKUltra.MODID, "aggro_target", addAggroTarget);
        event.getRegistry().register(aggroTarget);
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> addAggroTargetEnemies =
                (entity, choice) -> new EntityAINearestAttackableTargetMK((EntityCreature) entity,true,
                        Targeting.TargetType.ENEMY);
        AIGenerator aggroTargetEnemy = new AIGenerator(MKUltra.MODID, "aggro_target_enemies",
                addAggroTargetEnemies);
        event.getRegistry().register(aggroTargetEnemy);
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
            if (!(entity instanceof EntityCreature)){
                Log.info("Failed to add leash range ai, " +
                        "because it is not an EntityCreature");
                return null;
            }
            return new EntityAIReturnToSpawn((EntityCreature)entity, mobData, 1.0);
        };
        AIGenerator leashRange = new AIGenerator(
                MKUltra.MODID, "leash_range", addLeashRange);
        event.getRegistry().register(leashRange);
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> addHurtTarget = (entity, choice) ->
                new EntityAIHurtByTargetMK((EntityCreature)entity, true);
        AIGenerator hurtTarget = new AIGenerator(
                MKUltra.MODID, "hurt_target", addHurtTarget);
        event.getRegistry().register(hurtTarget);
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> addAttackTarget = (entity, choice) ->
                new EntityAIAttackMeleeMK((EntityCreature)entity, 1.0, false);
        AIGenerator attackTarget = new AIGenerator(
                MKUltra.MODID, "attack_target", addAttackTarget);
        event.getRegistry().register(attackTarget);
        BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> addZombieAttackTarget = (entity, choice) -> {
            if (entity instanceof EntityZombie) {
                return new EntityAIZombieAttackMK((EntityZombie) entity, 1.0, false);
            } else {
                return null;
            }
        };
        AIGenerator zombieAttackTarget = new AIGenerator(
                MKUltra.MODID, "zombie_attack_target", addZombieAttackTarget);
        event.getRegistry().register(zombieAttackTarget);
    }


    @SubscribeEvent
    public static void registerMobFactions(RegistryEvent.Register<MobFaction> event){
        event.getRegistry().register(NPC_FACTION);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerAIModifiers(RegistryEvent.Register<AIModifier> event) {
        Log.info("Registering AI Modifiers");
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
                                     IForgeRegistry<AttributeRange> registry) {
        String[] keys = {"setter", "min_value", "max_value"};
        if (!JsonLoader.checkKeysExist(obj, keys)) {
            return;
        }
        AttributeRange range = new AttributeRange(name, MKURegistry.getAttributeSetter(
                new ResourceLocation(obj.get("setter").getAsString())),
                obj.get("min_value").getAsDouble(), obj.get("max_value").getAsDouble());
        if (registry instanceof IForgeRegistryModifiable){
            IForgeRegistryModifiable modRegistry = (IForgeRegistryModifiable) registry;
            modRegistry.remove(name);
        }
        registry.register(range);
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
                                      IForgeRegistry<ItemOption> registry) {
        String[] keys = {"slot", "choices"};
        if (!JsonLoader.checkKeysExist(obj, keys)) {
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
            if (!JsonLoader.checkKeysExist(jsonObject, choiceKeys)) {
                continue;
            }
            float dropChance = .00f;
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
        if (registry instanceof IForgeRegistryModifiable){
            IForgeRegistryModifiable modRegistry = (IForgeRegistryModifiable) registry;
            modRegistry.remove(name);
        }
        registry.register(option);
    }


    public static void loadMobFactions(ResourceLocation name, JsonObject obj,
                                       IForgeRegistry<MobFaction> registry) {
        String[] keys = {"groups"};
        if (!JsonLoader.checkKeysExist(obj, keys)) {
            return;
        }
        MobFaction faction = new MobFaction(name);
        JsonObject groups = obj.getAsJsonObject("groups");
        for (java.util.Map.Entry<String, JsonElement> key : groups.entrySet()) {
            String groupName = key.getKey();
            JsonArray options = key.getValue().getAsJsonArray();
            for (JsonElement ele : options) {
                JsonObject jsonObject = ele.getAsJsonObject();
                String[] eleKeys = {"spawn_list", "weight"};
                if (!JsonLoader.checkKeysExist(jsonObject, eleKeys)) {
                    continue;
                }
                SpawnList spawnList = MKURegistry.getSpawnList(new ResourceLocation(jsonObject.get("spawn_list")
                        .getAsString()));
                if (spawnList == null) {
                    Log.info("Error loading Spawn List: %s for Faction: %s", jsonObject.get("spawn_list")
                            .getAsString(), name);
                    continue;
                }
                faction.addSpawnList(groupName, spawnList, jsonObject.get("weight").getAsDouble());
            }
        }
        if (obj.has("is_player_friendly")) {
            boolean isPlayerFriendly = obj.get("is_player_friendly").getAsBoolean();
            faction.setPlayerFriendly(isPlayerFriendly);
        }
        Log.info("Registered Faction %s", faction.getRegistryName().toString());
        if (registry instanceof IForgeRegistryModifiable){
            IForgeRegistryModifiable modRegistry = (IForgeRegistryModifiable) registry;
            modRegistry.remove(name);
        }
        registry.register(faction);
    }

    public static void loadSpawnList(ResourceLocation name, JsonObject obj,
                                     IForgeRegistry<SpawnList> registry) {
        String[] keys = {"options"};
        if (!JsonLoader.checkKeysExist(obj, keys)) {
            return;
        }
        JsonArray options = obj.get("options").getAsJsonArray();
        ArrayList<MobChoice> choices = new ArrayList<>();
        for (JsonElement ele : options) {
            JsonObject jsonObject = ele.getAsJsonObject();
            String[] optionkeys = {"definition", "weight"};
            if (!JsonLoader.checkKeysExist(jsonObject, optionkeys)) {
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
        if (registry instanceof IForgeRegistryModifiable){
            IForgeRegistryModifiable modRegistry = (IForgeRegistryModifiable) registry;
            modRegistry.remove(name);
        }
        registry.register(spawnList);

    }

    public static void loadMobDefinition(ResourceLocation name, JsonObject obj,
                                         IForgeRegistry<MobDefinition> registry) {
        Log.info("Loading Mob Definition: %s", name.toString());
        String[] keys = {"type"};
        if (!JsonLoader.checkKeysExist(obj, keys)) {
            return;
        }
        ResourceLocation loc = new ResourceLocation(obj.get("type").getAsString());
        Class<? extends Entity> mobClass = EntityList.getClass(loc);
        if  (mobClass == null){
            Log.info("Mob not found for: %s", loc.toString());
            return;
        }
        if (EntityLivingBase.class.isAssignableFrom(mobClass)) {
            MobDefinition definition = new MobDefinition(name,
                    (Class<? extends EntityLivingBase>) mobClass);
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
            if (obj.has("name")) {
                String mobName = obj.get("name").getAsString();
                definition.withMobName(mobName);
            }
            if (obj.has("additional_loot")) {
                String lootTable = obj.get("additional_loot").getAsString();
                definition.setAdditionalLootTable(new ResourceLocation(lootTable));
            }
            if (obj.has("xp")){
                int bonus = obj.get("xp").getAsInt();
                definition.setBonusExperience(bonus);
            }
            if (obj.has("custom_modifiers")) {
                JsonArray json_modifiers = obj.get("custom_modifiers").getAsJsonArray();
                ArrayList<CustomModifier> modifiers = new ArrayList<>();
                for (JsonElement ele : json_modifiers) {
                    JsonObject jsonObject = ele.getAsJsonObject();
                    String[] customKeys = {"setter"};
                    if (!JsonLoader.checkKeysExist(jsonObject, customKeys)) {
                        continue;
                    }
                    CustomSetter setter = MKURegistry.getCustomSetter(
                            new ResourceLocation(jsonObject.get("setter").getAsString()));
                    if (setter == null) {
                        Log.info("Could not find custom setter with name %s, skipping load.",
                                jsonObject.get("setter").getAsString());
                        continue;
                    }
                    CustomModifier mod = setter.loadFromJson(jsonObject);
                    if (mod == null) {
                        continue;
                    }
                    modifiers.add(mod);
                }
                definition.withCustomModifiers(modifiers.toArray(new CustomModifier[0]));
            }
            if (obj.has("default_spawn_weight")){
                double weight = obj.get("default_spawn_weight").getAsDouble();
                definition.setDefaultSpawnWeight(weight);
            }
            if (obj.has("can_default_spawn")) {
                boolean canSpawn = obj.get("can_default_spawn").getAsBoolean();
                definition.setCanDefaultSpawn(canSpawn);
            }
            if (registry instanceof IForgeRegistryModifiable){
                IForgeRegistryModifiable modRegistry = (IForgeRegistryModifiable) registry;
                modRegistry.remove(name);
            }
            registry.register(definition);
        } else {
            Log.info("%s  not an EntityLivingBase skipping mob definition %s",
                loc.toString(), obj.toString());
        }
    }

    public static void loadAIModifier(ResourceLocation name, JsonObject obj,
                                      IForgeRegistry<AIModifier> registry) {
        String[] keys = {"type", "choices"};
        if (!JsonLoader.checkKeysExist(obj, keys)) {
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
                    if (!JsonLoader.checkKeysExist(jsonObject, choiceKeys)) {
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
                if (registry instanceof IForgeRegistryModifiable){
                    IForgeRegistryModifiable modRegistry = (IForgeRegistryModifiable) registry;
                    modRegistry.remove(name);
                }
                registry.register(ai_modifier);
                break;
            }
            case "REMOVE_ALL_TARGET_TASKS":
            case "REMOVE_ALL_TASKS": {
                AIModifier ai_modifier = new AIModifier(name, modifier);
                if (registry instanceof IForgeRegistryModifiable){
                    IForgeRegistryModifiable modRegistry = (IForgeRegistryModifiable) registry;
                    modRegistry.remove(name);
                }
                registry.register(ai_modifier);
                break;
            }
            case "REMOVE_AI": {
                String[] choiceKeys = {"task_class", "min_level", "task_type"};
                ArrayList<BehaviorChoice> choices = new ArrayList<>();
                for (JsonElement subEle : obj.getAsJsonArray("choices")) {
                    JsonObject jsonObject = subEle.getAsJsonObject();
                    if (!JsonLoader.checkKeysExist(jsonObject, choiceKeys)) {
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
                if (registry instanceof IForgeRegistryModifiable){
                    IForgeRegistryModifiable modRegistry = (IForgeRegistryModifiable) registry;
                    modRegistry.remove(name);
                }
                registry.register(remove_task);
            }

        }
    }
}
