package com.chaosbuffalo.mkultra.core;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ClassList extends IForgeRegistryEntry.Impl<ClassList> {

    private List<ResourceLocation> classIds = new ArrayList<>();

    public ClassList(ResourceLocation identity) {
        setRegistryName(identity);
    }

    public void addClass(ResourceLocation classId) {
        classIds.add(classId);
    }

    @Nonnull
    public List<ResourceLocation> getClasses() {
        return classIds;
    }

}
