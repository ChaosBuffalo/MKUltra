package com.chaosbuffalo.mkultra.spawn;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;


public class AttributeRange extends IForgeRegistryEntry.Impl<AttributeRange> {

    private final AttributeSetter setter;
    public double start;
    public double stop;
    public int level;
    public int maxLevel;


    public AttributeRange(ResourceLocation name,
                          AttributeSetter setter,
                          double start, double stop) {
        setRegistryName(name);
        this.setter = setter;
        this.start = start;
        this.stop = stop;
    }

    public void apply(EntityLivingBase entity, int level, int maxLevel) {
        this.level = level;
        this.maxLevel = maxLevel;
        this.setter.apply(entity, this);
    }
}
