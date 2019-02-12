package com.chaosbuffalo.mkultra.spawner;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.function.BiConsumer;

public class AIModifier extends IForgeRegistryEntry.Impl<AIModifier> {

    private final BiConsumer<EntityLivingBase, AIModifier> applyFunc;
    public int level;
    public int maxLevel;
    public final ArrayList<BehaviorChoice> behaviorChoices;


    public AIModifier(ResourceLocation name,
                      BiConsumer<EntityLivingBase, AIModifier> applyFunc,
                      BehaviorChoice... behaviors) {
        setRegistryName(name);
        this.applyFunc = applyFunc;
        behaviorChoices = new ArrayList<>();
        behaviorChoices.addAll(Arrays.asList(behaviors));
    }

    public void apply(EntityLivingBase entity, int level, int maxLevel) {
        this.level = level;
        this.maxLevel = maxLevel;
        this.applyFunc.accept(entity, this);
    }
}
