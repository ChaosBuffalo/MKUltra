package com.chaosbuffalo.mkultra.utils;

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
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.util.TriConsumer;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;

public class JsonLoader {
    private static Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();

    public static <E> void loadModsForType(String subFolder,
                                           TriConsumer<ResourceLocation, JsonObject, E> registerFunc,
                                           E event) {
        ModContainer old = Loader.instance().activeModContainer();
        Loader.instance().setActiveModContainer(null);
        Loader.instance().getActiveModList().forEach(mod -> loadJsonFiles(mod, subFolder, registerFunc, event));
        Loader.instance().setActiveModContainer(old);
    }

    public static <E> boolean loadJsonFiles(ModContainer mod, String subFolder,
                                            TriConsumer<ResourceLocation, JsonObject, E> registerFunc,
                                            E event) {
        Loader.instance().setActiveModContainer(mod);
        return CraftingHelper.findFiles(mod, "assets/" + mod.getModId() + subFolder,
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
