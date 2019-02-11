package com.chaosbuffalo.mkultra.utils;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.spawner.*;
import jline.internal.Nullable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.function.BiFunction;


public class SpawnerUtils {

    public static final int MAX_LEVEL = 10;

    public static final MobDefinition EMPTY_MOB = new MobDefinition(null, 0);

    public static final HashMap<ResourceLocation, BiFunction<EntityLivingBase, AttributeRange, Boolean>>
            ATTRIBUTE_FUNCTIONS = new HashMap<>();

    public static final HashMap<ResourceLocation, BiFunction<EntityLivingBase, ItemChoice, Boolean>>
            ITEM_ASSIGNERS = new HashMap<>();

    private static final HashMap<ResourceLocation, MobDefinition> MOB_DEFINITIONS = new HashMap<>();

    public static void setup(){
        BaseSpawnAttributes.setup();
        ItemAssigners.setup();
        MOB_DEFINITIONS.put(
                new ResourceLocation(MKUltra.MODID, "test_skeleton"),
                new MobDefinition(EntitySkeleton.class, 10)
                        .withAttributeRanges(
                                new AttributeRange(BaseSpawnAttributes.MAX_HEALTH, 20.0, 50.0))
                        .withItemOptions(new ItemOption(ItemAssigners.MAINHAND,
                                new ItemChoice(new ItemStack(Items.IRON_SWORD, 1), 5, 0),
                                new ItemChoice(new ItemStack(Items.BOW, 1), 5, 0))));
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

    public static void registerItemAssigner(ResourceLocation loc,
                                            BiFunction<EntityLivingBase, ItemChoice, Boolean> func){
        ITEM_ASSIGNERS.put(loc, func);
    }

    @Nullable
    public static BiFunction<EntityLivingBase, ItemChoice, Boolean> getItemAssigner(ResourceLocation loc) {
        return ITEM_ASSIGNERS.get(loc);
    }

}
