package com.chaosbuffalo.mkultra.utils;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.spawner.BaseSpawnAttributes;
import com.chaosbuffalo.mkultra.spawner.MobDefinition;
import com.chaosbuffalo.mkultra.spawner.AttributeRange;
import jline.internal.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.function.BiFunction;


public class SpawnerUtils {

    public static final int MAX_LEVEL = 10;

    public static final MobDefinition EMPTY_MOB = new MobDefinition(null, 0);

    public static final HashMap<ResourceLocation, BiFunction<EntityLivingBase, AttributeRange, Boolean>>
            ATTRIBUTE_FUNCTIONS = new HashMap<>();

    private static final HashMap<ResourceLocation, MobDefinition> MOB_DEFINITIONS = new HashMap<>();
    static {
        BaseSpawnAttributes.setup();
        MOB_DEFINITIONS.put(
                new ResourceLocation(MKUltra.MODID, "test_skeleton"),
                new MobDefinition(EntitySkeleton.class, 10)
                        .withAttributeRanges(
                                new AttributeRange(BaseSpawnAttributes.MAX_HEALTH, 20.0, 200.0)));

    }


    public static MobDefinition getDefinition(ResourceLocation loc){
        return MOB_DEFINITIONS.getOrDefault(loc, EMPTY_MOB);
    }

    @Nullable
    public static BiFunction<EntityLivingBase, AttributeRange, Boolean> getAttributeFunction(ResourceLocation loc){
        return ATTRIBUTE_FUNCTIONS.get(loc);
    }

    public static void registerMobDefinition(ResourceLocation loc, MobDefinition definition){
        MOB_DEFINITIONS.put(loc, definition);
    }

    public static void registerAttributeFunction(ResourceLocation loc,
                                                 BiFunction<EntityLivingBase, AttributeRange, Boolean> func){
        ATTRIBUTE_FUNCTIONS.put(loc, func);
    }

}
