package com.chaosbuffalo.mkultra.data_generators;

import com.chaosbuffalo.mkfaction.data.MKFactionDataProvider;
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

public class MKUFactionProvider extends MKFactionDataProvider {

    public MKUFactionProvider(DataGenerator generator) {
        super(MKUltra.MODID, generator);
    }

    public void act(@Nonnull DirectoryCache cache) {
        writeFaction(MKUFactions.GREEN_KNIGHTS_FACTION, cache);
        writeFaction(MKUFactions.HYBOREAN_DEAD, cache);
        writeFaction(MKUFactions.IMPERIAL_DEAD, cache);
        writeFaction(MKUFactions.SEE_OF_SOLANG, cache);
    }
}
