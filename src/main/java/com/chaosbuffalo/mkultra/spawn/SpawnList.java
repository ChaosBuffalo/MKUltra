package com.chaosbuffalo.mkultra.spawn;

import com.chaosbuffalo.mkultra.choice.RandomCollection;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class SpawnList extends IForgeRegistryEntry.Impl<SpawnList> {

    RandomCollection<MobDefinition> options;

    public SpawnList(ResourceLocation name){
        setRegistryName(name);
        options = new RandomCollection<>();
    }

    public void addOption(MobDefinition mob){
        options.add(mob.spawnWeight, mob);
    }

    public SpawnList withOptions(MobDefinition... mobs){
        for (MobDefinition mob : mobs){
            options.add(mob.spawnWeight, mob);
        }
        return this;
    }

    public MobDefinition getNextDefinition(){
        return options.next();
    }
}
