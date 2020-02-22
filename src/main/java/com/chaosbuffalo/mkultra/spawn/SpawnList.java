package com.chaosbuffalo.mkultra.spawn;

import com.chaosbuffalo.mkultra.utils.RandomCollection;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Collection;

public class SpawnList extends IForgeRegistryEntry.Impl<SpawnList> {

    private RandomCollection<MobChoice> options;


    public SpawnList(ResourceLocation name) {
        setRegistryName(name);
        options = new RandomCollection<>();
    }

    public void addOption(MobDefinition mob, double spawnWeight) {
        options.add(spawnWeight, new MobChoice(mob, spawnWeight));
    }

    public SpawnList withOptions(Collection<MobChoice> mobs) {
        for (MobChoice mob : mobs) {
            options.add(mob.spawnWeight, mob);
        }
        return this;
    }

    public MobDefinition getNextDefinition() {
        return options.next().mob;
    }

    public boolean isEmpty() {
        return options.size() <= 0;
    }
}
