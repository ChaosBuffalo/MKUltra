package com.chaosbuffalo.mkultra.spawn;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.BiFunction;

public class AIGenerator extends IForgeRegistryEntry.Impl<AIGenerator> {
    private final BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> generator;

    public AIGenerator(ResourceLocation name, BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> func) {
        setRegistryName(name);
        generator = func;
    }

    public AIGenerator(String domain, String name, BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> func) {
        this(new ResourceLocation(domain, name), func);
    }

    public AIGenerator(String stringName, BiFunction<EntityLiving, BehaviorChoice, EntityAIBase> func) {
        this(new ResourceLocation(stringName), func);
    }

    public EntityAIBase getAI(EntityLiving entity, BehaviorChoice choice) {
        return generator.apply(entity, choice);
    }
}
