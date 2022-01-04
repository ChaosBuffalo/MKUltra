package com.chaosbuffalo.mkultra.data_generators;

import com.chaosbuffalo.mkfaction.faction.MKFaction;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.MKUFactions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.file.Path;

public class MKUFactionProvider implements IDataProvider {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    private final DataGenerator generator;

    public MKUFactionProvider(DataGenerator generator) {
        this.generator = generator;
    }

    public void act(@Nonnull DirectoryCache cache) {
        writeFaction(MKUFactions.GREEN_KNIGHTS_FACTION, cache);
        writeFaction(MKUFactions.HYBOREAN_DEAD, cache);
        writeFaction(MKUFactions.IMPERIAL_DEAD, cache);

    }


    public void writeFaction(MKFaction faction, @Nonnull DirectoryCache cache) {
        Path outputFolder = this.generator.getOutputFolder();
        ResourceLocation key = faction.getRegistryName();
        Path path = outputFolder.resolve("data/" + key.getNamespace() + "/factions/" + key.getPath() + ".json");

        try {
            JsonElement element = faction.serialize(JsonOps.INSTANCE);
            IDataProvider.save(GSON, cache, element, path);
        } catch (IOException var7) {
            MKUltra.LOGGER.error("Couldn't write faction {}", path, var7);
        }
    }

    public String getName() {
        return "MKU Factions";
    }
}
