package com.chaosbuffalo.mkultra.spawner;
import com.chaosbuffalo.mkultra.utils.SpawnerUtils;
import net.minecraft.entity.EntityLivingBase;

import java.util.Arrays;
import java.util.HashSet;


public class MobDefinition {

    public final Class<? extends EntityLivingBase> entityClass;
    public final int spawnWeight;
    public final HashSet<AttributeRange> attributeRanges;

    public MobDefinition(Class<? extends EntityLivingBase> entityClass, int spawnWeight){
        this.entityClass = entityClass;
        this.spawnWeight = spawnWeight;
        attributeRanges = new HashSet<>();

    }

    public MobDefinition withAttributeRanges(AttributeRange... ranges){
        attributeRanges.addAll(Arrays.asList(ranges));
        return this;
    }

    public void applyStats(EntityLivingBase entity, int level){
        for (AttributeRange range : attributeRanges){
            range.apply(entity, level, SpawnerUtils.MAX_LEVEL);
        }
    }
}
