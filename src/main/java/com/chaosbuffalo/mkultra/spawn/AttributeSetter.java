package com.chaosbuffalo.mkultra.spawn;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.BiConsumer;

public class AttributeSetter extends IForgeRegistryEntry.Impl<AttributeSetter> {
    private final BiConsumer<EntityLivingBase, AttributeRange> setter;

    public AttributeSetter(ResourceLocation name, BiConsumer<EntityLivingBase, AttributeRange> setterIn) {
        this.setRegistryName(name);
        setter = setterIn;
    }

    public AttributeSetter(String domain, String name, BiConsumer<EntityLivingBase, AttributeRange> setterIn) {
        this(new ResourceLocation(domain, name), setterIn);
    }

    public void apply(EntityLivingBase entity, AttributeRange range) {
        setter.accept(entity, range);
    }
}
