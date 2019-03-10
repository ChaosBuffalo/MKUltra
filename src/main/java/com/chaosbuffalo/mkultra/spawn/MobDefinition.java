package com.chaosbuffalo.mkultra.spawn;
import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MKUMobData;
import com.chaosbuffalo.mkultra.core.MobAbilityTracker;
import com.chaosbuffalo.mkultra.init.ModSpawn;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;


public class MobDefinition extends IForgeRegistryEntry.Impl<MobDefinition> {

    public final Class<? extends EntityLivingBase> entityClass;
    private final HashSet<AttributeRange> attributeRanges;
    private final HashSet<ItemOption> itemOptions;
    private final ArrayList<AIModifier> aiModifiers;
    private final HashSet<MobAbility> mobAbilities;
    private final ArrayList<CustomModifier> customModifiers;
    private String mobName;
    private ResourceLocation additionalLootTable;

    public MobDefinition(ResourceLocation name, Class<? extends EntityLivingBase> entityClass){
        setRegistryName(name);
        this.entityClass = entityClass;
        attributeRanges = new HashSet<>();
        itemOptions = new HashSet<>();
        aiModifiers = new ArrayList<>();
        mobAbilities = new HashSet<>();
        customModifiers = new ArrayList<>();
    }

    public MobDefinition withAttributeRanges(AttributeRange... ranges){
        attributeRanges.addAll(Arrays.asList(ranges));
        return this;
    }

    public void setAdditionalLootTable(ResourceLocation table){
        additionalLootTable = table;
    }

    public MobDefinition withCustomModifiers(CustomModifier... modifiers){
        customModifiers.addAll(Arrays.asList(modifiers));
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

    public void applyDefinition(World world, EntityLivingBase entity, int level){
        if (mobName != null){
            entity.setCustomNameTag(mobName);
        }
        // Lets make it so the mobs cant change their loot
        // (which would trigger an ai change in some mobs like skeletons).
        if (entity instanceof EntityLiving){
            ((EntityLiving) entity).setCanPickUpLoot(false);
        }
        for (AttributeRange range : attributeRanges){
            range.apply(entity, level, ModSpawn.MAX_LEVEL);
        }

        for (ItemOption option : itemOptions){
            option.apply(entity, level, ModSpawn.MAX_LEVEL);
        }

        IMobData mobData = MKUMobData.get(entity);
        if (mobData != null){
            if (additionalLootTable != null){
                mobData.setAdditionalLootTable(additionalLootTable);
            }
            mobData.setMobLevel(level);
            for (MobAbility ability : mobAbilities){
                if (ability != null) {
                    mobData.addAbility(ability);
                }
            }
            for (MobAbilityTracker tracker : mobData.getAbilityTrackers()){
                tracker.setCooldown(world.rand.nextInt(tracker.getAbility().getCooldown()));
            }
        }

        for (CustomModifier cMod : customModifiers){
            cMod.apply(entity, level, ModSpawn.MAX_LEVEL);
        }

        for (AIModifier mod : aiModifiers){
            mod.apply(entity, level, ModSpawn.MAX_LEVEL);
        }
    }
}
