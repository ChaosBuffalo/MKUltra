package com.chaosbuffalo.mkultra.spawn;

public class MobChoice{
    public final double spawnWeight;
    public final MobDefinition mob;

    public MobChoice(MobDefinition mob, double spawnWeight){
        this.mob = mob;
        this.spawnWeight = spawnWeight;
    }
}
