package com.chaosbuffalo.mkultra.spawn;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.BiConsumer;


public class AttributeRange extends IForgeRegistryEntry.Impl<AttributeRange> {

    private final BiConsumer<EntityLivingBase, AttributeRange> applyFunc;
    public double start;
    public double stop;
    public int level;
    public int maxLevel;


    public AttributeRange(ResourceLocation name,
                          BiConsumer<EntityLivingBase, AttributeRange> applyFunc,
                          double start, double stop) {
        setRegistryName(name);
        this.applyFunc = applyFunc;
        this.start = start;
        this.stop = stop;
    }

    public void apply(EntityLivingBase entity, int level, int maxLevel) {
        this.level = level;
        this.maxLevel = maxLevel;
        this.applyFunc.accept(entity, this);
    }
}
