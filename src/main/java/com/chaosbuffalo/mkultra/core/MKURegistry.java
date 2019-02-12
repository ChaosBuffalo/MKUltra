package com.chaosbuffalo.mkultra.core;


import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.spawner.*;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class MKURegistry {
    public static IForgeRegistry<BaseClass> REGISTRY_CLASSES = null;
    public static IForgeRegistry<BaseAbility> REGISTRY_ABILITIES = null;
    public static IForgeRegistry<ItemOption> REGISTRY_MOB_ITEMS = null;
    public static IForgeRegistry<AttributeRange> REGISTRY_MOB_ATTRS = null;
    public static IForgeRegistry<MobDefinition> REGISTRY_MOB_DEF = null;
    public static IForgeRegistry<AIModifier> REGISTRY_MOB_AI_MODS = null;

    public static ResourceLocation INVALID_CLASS = new ResourceLocation(MKUltra.MODID, "class.invalid");
    public static ResourceLocation INVALID_ABILITY = new ResourceLocation(MKUltra.MODID, "ability.invalid");
    public static ResourceLocation INVALID_MOB = new ResourceLocation(MKUltra.MODID, "mob.invalid");
    public static final MobDefinition EMPTY_MOB = new MobDefinition(INVALID_MOB,null, 0);


    public static BaseClass getClass(ResourceLocation classId) {
        return REGISTRY_CLASSES.getValue(classId);
    }

    public static ItemOption getItemOption(ResourceLocation name){
        return REGISTRY_MOB_ITEMS.getValue(name);
    }

    public static AttributeRange getAttributeRange(ResourceLocation name){
        return REGISTRY_MOB_ATTRS.getValue(name);
    }

    public static MobDefinition getMobDefinition(ResourceLocation name){
        MobDefinition val = REGISTRY_MOB_DEF.getValue(name);
        if (val != null) {
            return val;
        }
        return EMPTY_MOB;
    }

    public static List<ResourceLocation> getClassesProvidedByItem(Item held) {
        return REGISTRY_CLASSES.getEntries().stream()
                .filter(kv -> kv.getValue().getUnlockItem() == held)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    public static List<ResourceLocation> getValidClasses(List<ResourceLocation> classes) {
        return REGISTRY_CLASSES.getKeys().stream()
                .filter(classes::contains)
                .collect(Collectors.toList());
    }

    public static String getClassName(ResourceLocation classId) {
        BaseClass cls = getClass(classId);
        return cls != null ? cls.getClassName() : "<NULL CLASS>";
    }

    public static BaseAbility getAbility(ResourceLocation abilityId) {
        return REGISTRY_ABILITIES.getValue(abilityId);
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void createRegistries(RegistryEvent.NewRegistry event) {
        REGISTRY_CLASSES = new RegistryBuilder<BaseClass>()
                .setName(new ResourceLocation(MKUltra.MODID, "classes"))
                .setType(BaseClass.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .create();

        REGISTRY_ABILITIES = new RegistryBuilder<BaseAbility>()
                .setName(new ResourceLocation(MKUltra.MODID, "abilities"))
                .setType(BaseAbility.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .create();

        REGISTRY_MOB_ITEMS = new RegistryBuilder<ItemOption>()
                .setName(new ResourceLocation(MKUltra.MODID, "mob_items"))
                .setType(ItemOption.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .create();

        REGISTRY_MOB_ATTRS = new RegistryBuilder<AttributeRange>()
                .setName(new ResourceLocation(MKUltra.MODID, "mob_attr"))
                .setType(AttributeRange.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .create();

        // registries are alphabetical so we want to visit this one after all the other mob config ones
        REGISTRY_MOB_DEF = new RegistryBuilder<MobDefinition>()
                .setName(new ResourceLocation(MKUltra.MODID, "zzz_mob_def"))
                .setType(MobDefinition.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .create();

        REGISTRY_MOB_AI_MODS = new RegistryBuilder<AIModifier>()
                .setName(new ResourceLocation(MKUltra.MODID, "mob_ai_mods"))
                .setType(AIModifier.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .create();
    }
}
