package com.chaosbuffalo.mkultra.spawner;
import com.chaosbuffalo.mkultra.utils.SpawnerUtils;
import net.minecraft.entity.EntityLivingBase;

import java.util.Arrays;
import java.util.HashSet;


public class MobDefinition {

    public final Class<? extends EntityLivingBase> entityClass;
    public final int spawnWeight;
    private final HashSet<AttributeRange> attributeRanges;
    private final HashSet<ItemOption> itemOptions;

    public MobDefinition(Class<? extends EntityLivingBase> entityClass, int spawnWeight){
        this.entityClass = entityClass;
        this.spawnWeight = spawnWeight;
        attributeRanges = new HashSet<>();
        itemOptions = new HashSet<>();

    }

    public MobDefinition withAttributeRanges(AttributeRange... ranges){
        attributeRanges.addAll(Arrays.asList(ranges));
        return this;
    }

    public MobDefinition withItemOptions(ItemOption... options){
        itemOptions.addAll(Arrays.asList(options));
        return this;
    }

    public void applyDefinition(EntityLivingBase entity, int level){
        for (AttributeRange range : attributeRanges){
            range.apply(entity, level, SpawnerUtils.MAX_LEVEL);
        }
        for (ItemOption option : itemOptions){
            option.apply(entity, level, SpawnerUtils.MAX_LEVEL);
        }
    }
}
