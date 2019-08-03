package com.chaosbuffalo.mkultra.utils;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.log.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.util.TriConsumer;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonLoader {
    private static Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public static <E extends IForgeRegistry> void loadModsForType(String subFolder, String configLocation,
                                           String assetsLocation,
                                           TriConsumer<ResourceLocation, JsonObject, E> registerFunc,
                                           E register) {
        ModContainer old = Loader.instance().activeModContainer();
        Loader.instance().getActiveModList().forEach(mod -> {
            loadJsonFiles(mod, subFolder, assetsLocation, registerFunc, register);
            loadJsonConfigs(mod, subFolder, configLocation, registerFunc, register);
        });
        Loader.instance().setActiveModContainer(old);

    }

    public static <E extends IForgeRegistry> void loadJsonConfigs(ModContainer mod, String subFolder, String baseDir,
                                                                  TriConsumer<ResourceLocation, JsonObject, E> registerFunc,
                                                                  E registry){

        String path = MKUltra.config_loc + File.separator + baseDir + File.separator + mod.getModId() + File.separator + subFolder;
        if (Files.isDirectory(Paths.get(path))) {
            try {
                Files.walk(Paths.get(path))
                        .filter((f) -> FilenameUtils.getExtension(f.toString()).equals("json"))
                        .forEach((f) -> {
                            String name = FilenameUtils.removeExtension(f.getFileName().toString()).replaceAll("\\\\", "/");
                            ResourceLocation key = new ResourceLocation(mod.getModId(), name);
                            try (BufferedReader reader = Files.newBufferedReader(f)) {
                                JsonObject json = JsonUtils.fromJson(GSON, reader, JsonObject.class);
                                registerFunc.accept(key, json, registry);
                            } catch (JsonParseException e) {
                                Log.error("Parsing error loading json {}", key, e);
                            } catch (IOException e) {
                                Log.error("Couldn't read json {} from {}", key, f, e);
                            }
                        });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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

    public static <E> boolean loadJsonFiles(ModContainer mod, String subFolder, String baseDir,
                                            TriConsumer<ResourceLocation, JsonObject, E> registerFunc,
                                            E event) {
        Loader.instance().setActiveModContainer(mod);
        String path = baseDir + File.separator + mod.getModId() + File.separator + subFolder;
        Log.info("Looking in %s for spawn assets.", path);
        return CraftingHelper.findFiles(mod, path,
                root -> true,
                (root, file) -> {
                    String relative = root.relativize(file).toString();
                    if (!"json".equals(FilenameUtils.getExtension(file.toString())) || relative.startsWith("_")) {
                        return true;
                    } else {
                        String name = FilenameUtils.removeExtension(relative).replaceAll("\\\\", "/");
                        ResourceLocation key = new ResourceLocation(mod.getModId(), name);
                        Log.info("Trying to load %s", key.toString());
                        try (BufferedReader reader = Files.newBufferedReader(file)) {
                            JsonObject json = JsonUtils.fromJson(GSON, reader, JsonObject.class);
                            registerFunc.accept(key, json, event);
                        } catch (JsonParseException e) {
                            Log.error("Parsing error loading json {}", key, e);
                            return false;
                        } catch (IOException e) {
                            Log.error("Couldn't read json {} from {}", key, file, e);
                            return false;
                        }
                        return true;
                    }
                }, true, true);
    }
}
