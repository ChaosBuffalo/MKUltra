package com.chaosbuffalo.mkultra.spawn;

import com.chaosbuffalo.mkultra.choice.RandomCollection;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Set;

public class MobFaction extends IForgeRegistryEntry.Impl<MobFaction> {

    private HashMap<String, RandomCollection<SpawnList>> spawnLists;

    public MobFaction(ResourceLocation name){
        setRegistryName(name);
        spawnLists = new HashMap<>();
        registerDefaultMobGroups();
    }

    private void registerDefaultMobGroups(){
    }


    public void addSpawnList(String group, SpawnList list, double weight){
        if (!spawnLists.containsKey(group)){
            spawnLists.put(group, new RandomCollection<>());
        }
        spawnLists.get(group).add(weight, list);
    }

    @Nullable
    public Set<String> getMobGroups(){
        return spawnLists.keySet();
    }

    @Nullable
    public SpawnList getSpawnListForGroup(String group){
        if (spawnLists.get(group).size() > 0){
            return spawnLists.get(group).next();
        } else {
            return null;
        }
    }

    public boolean isSpawnListEmpty(String group){
        if (!spawnLists.containsKey(group)){
            return true;
        }
        SpawnList list = getSpawnListForGroup(group);
        if (list == null){
            return true;
        }
        return list.isEmpty();
    }

    public MobFaction withSpawnList(String group, SpawnList list, double weight){
        addSpawnList(group, list, weight);
        return this;
    }

    public MobFaction(String domain, String name){
        this(new ResourceLocation(domain, name));
    }


}
