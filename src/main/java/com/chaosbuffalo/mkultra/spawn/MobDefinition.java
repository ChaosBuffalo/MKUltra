package com.chaosbuffalo.mkultra.spawn;
import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MKUMobData;
import com.chaosbuffalo.mkultra.init.ModSpawn;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;


public class MobDefinition extends IForgeRegistryEntry.Impl<MobDefinition> {

    public final Class<? extends EntityLivingBase> entityClass;
    public final int spawnWeight;
    private final HashSet<AttributeRange> attributeRanges;
    private final HashSet<ItemOption> itemOptions;
    private final ArrayList<AIModifier> aiModifiers;
    private final HashSet<MobAbility> mobAbilities;
    private String mobName;

    public MobDefinition(ResourceLocation name, Class<? extends EntityLivingBase> entityClass, int spawnWeight){
        setRegistryName(name);
        this.entityClass = entityClass;
        this.spawnWeight = spawnWeight;
        attributeRanges = new HashSet<>();
        itemOptions = new HashSet<>();
        aiModifiers = new ArrayList<>();
        mobAbilities = new HashSet<>();
    }

    public MobDefinition withAttributeRanges(AttributeRange... ranges){
        attributeRanges.addAll(Arrays.asList(ranges));
        return this;
    }

    public MobDefinition withMobName(String name){
        mobName = name;
        return this;
    }

    public MobDefinition withAbilities(MobAbility... abilities){
        mobAbilities.addAll(Arrays.asList(abilities));
        return this;
    }

    public MobDefinition withItemOptions(ItemOption... options){
        itemOptions.addAll(Arrays.asList(options));
        return this;
    }

    public MobDefinition withAIModifiers(AIModifier... modifiers){
        aiModifiers.addAll(Arrays.asList(modifiers));
        return this;
    }

    public void applyDefinition(EntityLivingBase entity, int level){
        if (mobName != null){
            entity.setCustomNameTag(mobName);
        }

        for (AttributeRange range : attributeRanges){
            range.apply(entity, level, ModSpawn.MAX_LEVEL);
        }
        for (ItemOption option : itemOptions){
            option.apply(entity, level, ModSpawn.MAX_LEVEL);
        }
        IMobData mobData = MKUMobData.get(entity);
        mobData.setMobLevel(level);
        if (mobData != null){
            for (MobAbility ability : mobAbilities){
                if (ability != null) {
                    mobData.addAbility(ability);
                }
            }
        }

        for (AIModifier mod : aiModifiers){
            mod.apply(entity, level, ModSpawn.MAX_LEVEL);
        }
    }
}
