package com.chaosbuffalo.mkultra.spawn;

import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;

public class DefaultSpawnIndex {

    private static final HashMap<ResourceLocation, SpawnList> spawnLists = new HashMap<>();

    public static void addSpawn(ResourceLocation entityName, MobDefinition definition, double weight) {
        if (!spawnLists.containsKey(entityName)) {
            spawnLists.put(entityName, new SpawnList(new ResourceLocation(MKUltra.MODID,
                    "default_" + entityName.getNamespace() + "_" + entityName.getPath())));
        }
        spawnLists.get(entityName).addOption(definition, weight);
    }

    @Nullable
    public static SpawnList getSpawnListForEntity(ResourceLocation entityName) {
        if (spawnLists.containsKey(entityName)) {
            if (!spawnLists.get(entityName).isEmpty()) {
                return spawnLists.get(entityName);
            }
        }
        return null;
    }

}
