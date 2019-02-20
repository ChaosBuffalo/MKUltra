package com.chaosbuffalo.mkultra.json_utils;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.spawn.*;
import com.google.common.collect.Maps;
import com.google.gson.*;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.util.TriConsumer;


import javax.annotation.Nullable;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class LoadingHelper {
    private static Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private static Map<ResourceLocation, IConditionFactory> conditions = Maps.newHashMap();


    public static boolean findFiles(ModContainer mod, String base, Function<Path, Boolean> preprocessor,
                                    BiFunction<Path, Path, Boolean> processor, boolean defaultUnfoundRoot,
                                    boolean visitAllFiles) {
        File source = mod.getSource();
        if ("minecraft".equals(mod.getModId())) {
            return true;
        } else {
            FileSystem fs = null;
            boolean success = true;

            try {
                Path root = null;
                boolean var11;
                if (source.isFile()) {
                    try {
                        fs = FileSystems.newFileSystem(source.toPath(), (ClassLoader)null);
                        root = fs.getPath("/" + base);
                    } catch (IOException var17) {
                        FMLLog.log.error("Error loading FileSystem from jar: ", var17);
                        var11 = false;
                        return var11;
                    }
                } else if (source.isDirectory()) {
                    root = source.toPath().resolve(base);
                }

                Boolean cont;
                label240: {
                    if (root != null && Files.exists(root, new LinkOption[0])) {
                        if (preprocessor == null) {
                            break label240;
                        }

                        cont = (Boolean)preprocessor.apply(root);
                        if (cont != null && cont) {
                            break label240;
                        }

                        var11 = false;
                        return var11;
                    }

                    boolean var10 = defaultUnfoundRoot;
                    return var10;
                }

                if (processor == null) {
                    return success;
                } else {
                    cont = null;

                    boolean var12;
                    Iterator itr;
                    try {
                        itr = Files.walk(root).iterator();
                    } catch (IOException var18) {
                        FMLLog.log.error("Error iterating filesystem for: {}", mod.getModId(), var18);
                        var12 = false;
                        return var12;
                    }

                    while(itr != null && itr.hasNext()) {
                        cont = processor.apply(root, (Path) itr.next());
                        if (visitAllFiles) {
                            success &= cont != null && cont;
                        } else if (cont == null || !cont) {
                            var12 = false;
                            return var12;
                        }
                    }

                    return success;
                }
            } finally {
                IOUtils.closeQuietly(fs);
            }
        }
    }

    //  BiFunction<ResourceLocation, JsonObject, Boolean> registerFunc = (loc, json)-> {
    //
    //        };

    public static void loadAttribute(ResourceLocation name, JsonObject obj,
                                     RegistryEvent.Register<AttributeRange> event){
        String[] keys = {"setter", "min_value", "max_value"};
        if (!checkKeysExist(keys, obj)){
            return;
        }
        AttributeRange range = new AttributeRange(name, MKURegistry.getAttributeSetter(
                new ResourceLocation(obj.get("setter").getAsString())),
                obj.get("min_value").getAsDouble(), obj.get("max_value").getAsDouble());
        event.getRegistry().register(range);
    }

    public static boolean checkKeysExist(String[] keys, JsonObject obj){
        for (String key : keys){
            if (!obj.has(key)){
                Log.info("Skipping load for %s because %s not present", obj.toString(), key);
                return false;
            }
        }
        return true;
    }

    public static BiConsumer<EntityLivingBase, ItemChoice> getItemAssigner(String type){
        switch (type){
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

    public static BiConsumer<EntityLivingBase, AIModifier> getAIModifier(String type){
        switch (type){
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

    public static BehaviorChoice.TaskType getTaskType(String type){
        switch (type){
            case "TASK":
                return BehaviorChoice.TaskType.TASK;
            case "TARGET_TASK":
                return BehaviorChoice.TaskType.TARGET_TASK;
            default:
                return null;
        }
    }

    public static void loadItemOption(ResourceLocation name, JsonObject obj,
                                      RegistryEvent.Register<ItemOption> event){
        String[] keys = {"slot", "choices"};
        if (!checkKeysExist(keys, obj)){
            return;
        }
        BiConsumer<EntityLivingBase, ItemChoice> assigner = getItemAssigner(obj.get("slot").getAsString());
        if (assigner == null){
            Log.info("Item assigner %s not a valid option. Skipping load for: %s", obj.get("slot").getAsString(),
                    name);
            return;
        }
        String[] choiceKeys = {"weight", "item", "min_level"};
        ArrayList<ItemChoice> choices = new ArrayList<>();
        for (JsonElement subEle : obj.getAsJsonArray("choices")){
            JsonObject jsonObject = subEle.getAsJsonObject();
            if (!checkKeysExist(choiceKeys, jsonObject)){
                continue;
            }
            float dropChance = .05f;
            if (jsonObject.has("drop_chance")){
                dropChance = jsonObject.get("drop_chance").getAsFloat();
            }
            String itemName = jsonObject.get("item").getAsString();
            Item item;
            if (itemName.equals("EMPTY")){
                item = null;
            } else {
                item = Item.REGISTRY.getObject(new ResourceLocation(itemName));
            }
            if (item == null && !itemName.equals("EMPTY")){
                Log.info("Failed to load item for %s", jsonObject.get("item").getAsString());
                continue;
            } else {
                ItemStack itemStack;
                if (item == null){
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


    public static MobFaction.MobGroups getMobGroupFromString(String mobGroupIn){
        switch (mobGroupIn){
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
                                      RegistryEvent.Register<MobFaction> event){
        String[] groups = {"RANGE_GRUNT", "MELEE_GRUNT", "SUPPORT_GRUNT", "RANGE_CAPTAIN",
                "MELEE_CAPTAIN", "SUPPORT_CAPTAIN", "BOSS"};
        MobFaction faction = new MobFaction(name);
        for (String key : groups){
            if (obj.has(key)){
                JsonArray options = obj.get(key).getAsJsonArray();
                for (JsonElement ele : options){
                    JsonObject jsonObject = ele.getAsJsonObject();
                    String[] keys = {"spawn_list", "weight"};
                    if (!checkKeysExist(keys, jsonObject)){
                        continue;
                    }
                    SpawnList spawnList = MKURegistry.getSpawnList(new ResourceLocation(jsonObject.get("spawn_list")
                            .getAsString()));
                    if (spawnList == null){
                        Log.info("Error loading Spawn List: %s for Faction: %s", jsonObject.get("spawn_list")
                                .getAsString(), name);
                        continue;
                    }
                    MobFaction.MobGroups group = getMobGroupFromString(key);
                    if (group == null){
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
                                     RegistryEvent.Register<SpawnList> event){
        String[] keys = {"options"};
        if (!checkKeysExist(keys, obj)){
            return;
        }
        JsonArray options = obj.get("options").getAsJsonArray();
        ArrayList<MobChoice> choices = new ArrayList<>();
        for (JsonElement ele : options){
            JsonObject jsonObject = ele.getAsJsonObject();
            String[] optionkeys = {"definition", "weight"};
            if (!checkKeysExist(optionkeys, jsonObject)){
                continue;
            }
            MobDefinition definition = MKURegistry.getMobDefinition(
                    new ResourceLocation(jsonObject.get("definition").getAsString()));
            if (definition == null){
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
                                         RegistryEvent.Register<MobDefinition> event){
        String[] keys = {"type"};
        if (!checkKeysExist(keys, obj)){
            return;
        }
        try {
            Class mobClass = Class.forName(obj.get("type").getAsString());
            if (EntityLivingBase.class.isAssignableFrom(mobClass)){
                MobDefinition definition = new MobDefinition(name, mobClass);
                if (obj.has("attributes")){
                    JsonArray attributeList = obj.get("attributes").getAsJsonArray();
                    ArrayList<AttributeRange> ranges = new ArrayList<>();
                    for (JsonElement ele : attributeList){
                        String attributeName = ele.getAsString();
                        AttributeRange range = MKURegistry.getAttributeRange(new ResourceLocation(attributeName));
                        if (range != null){
                            ranges.add(range);
                        } else {
                            Log.info("Error finding attribute range for: %s", attributeName);
                        }
                    }
                    definition.withAttributeRanges(ranges.toArray(new AttributeRange[0]));
                }
                if (obj.has("item_options")){
                    JsonArray item_options = obj.get("item_options").getAsJsonArray();
                    ArrayList<ItemOption> options = new ArrayList<>();
                    for (JsonElement ele : item_options){
                        String option_name = ele.getAsString();
                        ItemOption option = MKURegistry.getItemOption(new ResourceLocation(option_name));
                        if (option != null){
                            options.add(option);
                        } else {
                            Log.info("Error finding ItemOption for: %s", option_name);
                        }
                    }
                    definition.withItemOptions(options.toArray(new ItemOption[0]));
                }
                if (obj.has("abilities")){
                    JsonArray json_options = obj.get("abilities").getAsJsonArray();
                    ArrayList<MobAbility> options = new ArrayList<>();
                    for (JsonElement ele : json_options){
                        String option_name = ele.getAsString();
                        MobAbility option = MKURegistry.getMobAbility(new ResourceLocation(option_name));
                        if (option != null){
                            options.add(option);
                        } else {
                            Log.info("Error finding MobAbility for: %s", option_name);
                        }
                    }
                    definition.withAbilities(options.toArray(new MobAbility[0]));
                }
                if (obj.has("ai_modifiers")){
                    JsonArray json_options = obj.get("ai_modifiers").getAsJsonArray();
                    ArrayList<AIModifier> options = new ArrayList<>();
                    for (JsonElement ele : json_options){
                        String option_name = ele.getAsString();
                        AIModifier option = MKURegistry.getAIModifier(new ResourceLocation(option_name));
                        if (option != null){
                            options.add(option);
                        } else {
                            Log.info("Error finding AI Modifier for: %s", option_name);
                        }
                    }
                    definition.withAIModifiers(options.toArray(new AIModifier[0]));
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
                                      RegistryEvent.Register<AIModifier> event){
        String[] keys = {"type", "choices"};
        if (!checkKeysExist(keys, obj)){
            return;
        }
        String type = obj.get("type").getAsString();
        BiConsumer<EntityLivingBase, AIModifier> modifier = getAIModifier(type);
        if (modifier == null){
            Log.info("AI Modifier type %s not a valid option. Skipping load for: %s", obj.get("slot").getAsString(),
                    name);
            return;
        }
        switch (type){
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
            case "REMOVE_ALL_TASKS":{
                AIModifier ai_modifier = new AIModifier(name, modifier);
                event.getRegistry().register(ai_modifier);
                break;
            }
            case "REMOVE_AI":{
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

    public static <E> void loadModsForType(String subFolder,
                                           TriConsumer<ResourceLocation, JsonObject, E> registerFunc,
                                           E event){
        Loader.instance().setActiveModContainer(null);
        Loader.instance().getActiveModList().forEach(mod -> loadJsonFiles(mod, subFolder, registerFunc, event));
        Loader.instance().setActiveModContainer(null);
    }

    public static <E> boolean loadJsonFiles(ModContainer mod, String subFolder,
                                            TriConsumer<ResourceLocation, JsonObject, E> registerFunc,
                                            E event){
        Loader.instance().setActiveModContainer(mod);
        JsonContext ctx = new JsonContext(mod.getModId());
        return findFiles(mod, "assets/" + mod.getModId() + subFolder, root -> true, (root, file) -> {
            String relative = root.relativize(file).toString();
            if ("json".equals(FilenameUtils.getExtension(file.toString())) && !relative.startsWith("_")) {
                String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
                ResourceLocation key = new ResourceLocation(ctx.getModId(), name);
                Log.info("Trying to load %s", key.toString());
                try {
                    BufferedReader reader = Files.newBufferedReader(file);
                    Throwable var8 = null;

                    Boolean var10;
                    try {
                        JsonObject json = JsonUtils.fromJson(GSON, reader, JsonObject.class);
                        if (!json.has("conditions") ||
                                CraftingHelper.processConditions(JsonUtils.getJsonArray(json,
                                        "conditions"), ctx)) {
                            Log.info("Got json: %s", json.toString());
                            registerFunc.accept(key, json, event);
                            return true;
                        }

                        var10 = true;
                    } catch (Throwable var22) {
                        var8 = var22;
                        throw var22;
                    } finally {
                        if (reader != null) {
                            if (var8 != null) {
                                try {
                                    reader.close();
                                } catch (Throwable var21) {
                                    var8.addSuppressed(var21);
                                }
                            } else {
                                reader.close();
                            }
                        }

                    }
                    return var10;
                } catch (JsonParseException var24) {
                    Log.info("Parsing error loading json from %s: %s, %s", subFolder, key.toString(), var24.toString());
                    return false;
                } catch (IOException var25) {
                    Log.info("Couldn't read json from %s: %s from %s", subFolder, key.toString(),var25.toString());
                    return false;
                }
            } else {
                return true;
            }
        }, true, true);
    }
}
