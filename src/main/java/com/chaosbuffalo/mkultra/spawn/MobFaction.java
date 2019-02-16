package com.chaosbuffalo.mkultra.spawn;

import com.chaosbuffalo.mkultra.choice.RandomCollection;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.HashMap;

public class MobFaction extends IForgeRegistryEntry.Impl<MobFaction> {

    public enum MobGroups {
        MELEE_GRUNT,
        RANGE_GRUNT,
        SUPPORT_GRUNT,
        MELEE_CAPTAIN,
        RANGE_CAPTAIN,
        SUPPORT_CAPTAIN,
        BOSS
    }

    private HashMap<MobGroups, RandomCollection<SpawnList>> spawnLists;

    public MobFaction(ResourceLocation name){
        setRegistryName(name);
        spawnLists = new HashMap<>();
        registerDefaultMobGroups();
    }

    private void registerDefaultMobGroups(){
        spawnLists.put(MobGroups.MELEE_GRUNT, new RandomCollection<>());
        spawnLists.put(MobGroups.RANGE_GRUNT, new RandomCollection<>());
        spawnLists.put(MobGroups.SUPPORT_GRUNT, new RandomCollection<>());
        spawnLists.put(MobGroups.MELEE_CAPTAIN, new RandomCollection<>());
        spawnLists.put(MobGroups.RANGE_CAPTAIN, new RandomCollection<>());
        spawnLists.put(MobGroups.SUPPORT_CAPTAIN, new RandomCollection<>());
        spawnLists.put(MobGroups.BOSS, new RandomCollection<>());
    }

    public void addSpawnList(MobGroups group, SpawnList list, double weight){
        spawnLists.get(group).add(weight, list);
    }

    public MobFaction withSpawnList(MobGroups group, SpawnList list, double weight){
        spawnLists.get(group).add(weight, list);
        return this;
    }

    public MobFaction(String domain, String name){
        this(new ResourceLocation(domain, name));
    }


}
