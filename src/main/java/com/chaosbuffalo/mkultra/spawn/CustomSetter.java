package com.chaosbuffalo.mkultra.spawn;

import com.google.gson.JsonObject;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class CustomSetter extends IForgeRegistryEntry.Impl<CustomSetter> {

    private final BiFunction<JsonObject, CustomSetter, CustomModifier> deserializer;
    private final BiConsumer<EntityLivingBase, CustomModifier> applier;

    public CustomSetter(ResourceLocation name,
                        BiFunction<JsonObject, CustomSetter, CustomModifier> deserializerIn,
                        BiConsumer<EntityLivingBase, CustomModifier> applierIn) {
        deserializer = deserializerIn;
        applier = applierIn;
        setRegistryName(name);
    }

    public CustomModifier loadFromJson(JsonObject json) {
        return deserializer.apply(json, this);
    }

    public BiConsumer<EntityLivingBase, CustomModifier> getApplier() {
        return applier;
    }
}
