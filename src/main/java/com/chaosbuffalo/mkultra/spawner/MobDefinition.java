package com.chaosbuffalo.mkultra.spawner;
import net.minecraft.entity.EntityLivingBase;

import java.util.HashSet;


public class MobDefinition {

    public final Class<? extends EntityLivingBase> entityClass;
    public final int spawnWeight;
    public final HashSet<StatRange> statRanges;

    public MobDefinition(Class<? extends EntityLivingBase> entityClass, int spawnWeight, StatRange... ranges){
        this.entityClass = entityClass;
        this.spawnWeight = spawnWeight;
        statRanges = new HashSet<>();
        for (StatRange range : ranges){
            statRanges.add(range);
        }
    }

    public void applyStats(EntityLivingBase entity){
        for (StatRange range : statRanges){
            range.applyRange(entity);
        }
    }
}
