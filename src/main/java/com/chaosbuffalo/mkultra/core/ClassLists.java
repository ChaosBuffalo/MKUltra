package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.MKConfig;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassLists {

    private static Map<ResourceLocation, ClassList> classListMap = new HashMap<>();

    public static void initFromConfig() {
        classListMap.clear();
        for (String mapping : MKConfig.classConfig.CLASS_MAP) {
            if (!mapping.contains("="))
                continue;
            String[] parts = mapping.split("=");
            if (parts.length != 2)
                continue;
            ResourceLocation id = new ResourceLocation(parts[0]);
            ResourceLocation[] classes = stringToClasses(parts[1]);

            ClassList list = getOrCreate(id);
            Arrays.stream(classes)
                    .filter(MKConfig::isClassEnabled)
                    .forEach(list::addClass);
        }
    }

    @Nonnull
    public static ClassList getOrCreate(ResourceLocation id) {
        return classListMap.computeIfAbsent(id, ClassList::new);
    }

    public static String classesToString(ResourceLocation listId, ResourceLocation... classIds) {
        return String.format("%s=%s", listId.toString(), Arrays.stream(classIds).map(ResourceLocation::toString).collect(Collectors.joining(",")));
    }

    public static ResourceLocation[] stringToClasses(String str) {
        String[] parts = str.split(",");
        return Arrays.stream(parts).map(ResourceLocation::new).toArray(ResourceLocation[]::new);
    }
}
