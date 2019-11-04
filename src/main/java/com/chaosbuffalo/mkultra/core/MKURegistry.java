package com.chaosbuffalo.mkultra.core;


import com.chaosbuffalo.mkultra.MKConfig;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.talents.BaseTalent;
import com.chaosbuffalo.mkultra.core.talents.PassiveAbilityTalent;
import com.chaosbuffalo.mkultra.core.talents.RangedAttributeTalent;
import com.chaosbuffalo.mkultra.core.talents.TalentTree;
import com.chaosbuffalo.mkultra.spawn.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class MKURegistry {
    public static IForgeRegistry<PlayerClass> REGISTRY_CLASSES = null;
    public static IForgeRegistry<PlayerAbility> REGISTRY_ABILITIES = null;
    public static IForgeRegistry<ItemOption> REGISTRY_MOB_ITEMS = null;
    public static IForgeRegistry<AttributeRange> REGISTRY_MOB_ATTRS = null;
    public static IForgeRegistry<MobDefinition> REGISTRY_MOB_DEF = null;
    public static IForgeRegistry<AIModifier> REGISTRY_MOB_AI_MODS = null;
    public static IForgeRegistry<MobAbility> REGISTRY_MOB_ABILITIES = null;
    public static IForgeRegistry<SpawnList> REGISTRY_SPAWN_LISTS = null;
    public static IForgeRegistry<MobFaction> REGISTRY_MOB_FACTIONS = null;
    public static IForgeRegistry<AIGenerator> REGISTRY_AI_GENERATORS = null;
    public static IForgeRegistry<AttributeSetter> REGISTRY_ATTRIBUTE_SETTERS = null;
    public static IForgeRegistry<CustomSetter> REGISTRY_CUSTOM_SETTERS = null;
    public static IForgeRegistry<BaseTalent> REGISTRY_TALENTS = null;
    public static IForgeRegistry<TalentTree> REGISTRY_TALENT_TREES = null;
    public static IForgeRegistry<ClassClientData> REGISTRY_CLASS_CLIENT_DATA = null;


    public static ResourceLocation INVALID_CLASS = new ResourceLocation(MKUltra.MODID, "class.invalid");
    public static ResourceLocation INVALID_ABILITY = new ResourceLocation(MKUltra.MODID, "ability.invalid");
    public static ResourceLocation INVALID_MOB = new ResourceLocation(MKUltra.MODID, "mob.invalid");
    public static final MobDefinition EMPTY_MOB = new MobDefinition(INVALID_MOB, null);
    public static ResourceLocation INVALID_MOB_ABILITY = new ResourceLocation(MKUltra.MODID, "mob_ability.invalid");
    public static ResourceLocation INVALID_SPAWN_LIST = new ResourceLocation(MKUltra.MODID, "spawn_list.invalid");
    public static ResourceLocation INVALID_FACTION = new ResourceLocation(MKUltra.MODID, "mob_faction.invalid");

    @Nullable
    public static PlayerClass getClass(ResourceLocation classId) {
        return MKConfig.isClassEnabled(classId) ? REGISTRY_CLASSES.getValue(classId) : null;
    }

    @Nullable
    public static ItemOption getItemOption(ResourceLocation name) {
        return REGISTRY_MOB_ITEMS.getValue(name);
    }

    @Nullable
    public static AttributeRange getAttributeRange(ResourceLocation name) {
        return REGISTRY_MOB_ATTRS.getValue(name);
    }

    @Nullable
    public static AIGenerator getAIGenerator(ResourceLocation name) {
        return REGISTRY_AI_GENERATORS.getValue(name);
    }

    @Nullable
    public static CustomSetter getCustomSetter(ResourceLocation name) {
        return REGISTRY_CUSTOM_SETTERS.getValue(name);
    }

    @Nullable
    public static MobAbility getMobAbility(ResourceLocation abilityId) {
        return REGISTRY_MOB_ABILITIES.getValue(abilityId);
    }

    public static MobDefinition getMobDefinition(ResourceLocation name) {
        MobDefinition val = REGISTRY_MOB_DEF.getValue(name);
        if (val != null) {
            return val;
        }
        return EMPTY_MOB;
    }

    @Nullable
    public static AIModifier getAIModifier(ResourceLocation name) {
        return REGISTRY_MOB_AI_MODS.getValue(name);
    }

    @Nullable
    public static MobFaction getFaction(ResourceLocation name) {
        return REGISTRY_MOB_FACTIONS.getValue(name);
    }

    @Nullable
    public static SpawnList getSpawnList(ResourceLocation name) {
        return REGISTRY_SPAWN_LISTS.getValue(name);
    }

    @Nullable
    public static BaseTalent getTalent(ResourceLocation name) {
        return REGISTRY_TALENTS.getValue(name);
    }

    public static ArrayList<RangedAttributeTalent> getAllAttributeTalents() {
        ArrayList<RangedAttributeTalent> talents = new ArrayList<>();
        for (BaseTalent talent : REGISTRY_TALENTS.getValuesCollection()) {
            if (talent.getTalentType() == BaseTalent.TalentType.ATTRIBUTE) {
                talents.add((RangedAttributeTalent) talent);
            }
        }
        return talents;
    }

    public static ArrayList<PassiveAbilityTalent> getAllPassiveTalents() {
        ArrayList<PassiveAbilityTalent> talents = new ArrayList<>();
        for (BaseTalent talent : REGISTRY_TALENTS.getValuesCollection()) {
            if (talent.getTalentType() == BaseTalent.TalentType.PASSIVE) {
                talents.add((PassiveAbilityTalent) talent);
            }
        }
        return talents;
    }


    public static List<ResourceLocation> getValidClasses(Collection<ResourceLocation> classes) {
        return REGISTRY_CLASSES.getKeys().stream()
                .filter(MKConfig::isClassEnabled)
                .filter(classes::contains)
                .collect(Collectors.toList());
    }

    public static List<ResourceLocation> getAllClasses() {
        return REGISTRY_CLASSES.getKeys().stream()
                .filter(MKConfig::isClassEnabled)
                .collect(Collectors.toList());
    }

    public static String getClassName(ResourceLocation classId) {
        PlayerClass cls = getClass(classId);
        return cls != null ? cls.getClassName() : "<NULL CLASS>";
    }

    @Nullable
    public static PlayerAbility getAbility(ResourceLocation abilityId) {
        return REGISTRY_ABILITIES.getValue(abilityId);
    }

    @Nullable
    public static AttributeSetter getAttributeSetter(ResourceLocation name) {
        return REGISTRY_ATTRIBUTE_SETTERS.getValue(name);
    }

    @Nullable
    public static ClassClientData getClassClientData(ResourceLocation name){
        return REGISTRY_CLASS_CLIENT_DATA.getValue(name);
    }


    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void createRegistries(RegistryEvent.NewRegistry event) {
        REGISTRY_CLASSES = new RegistryBuilder<PlayerClass>()
                .setName(new ResourceLocation(MKUltra.MODID, "classes"))
                .setType(PlayerClass.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .allowModification()
                .create();

        REGISTRY_ABILITIES = new RegistryBuilder<PlayerAbility>()
                .setName(new ResourceLocation(MKUltra.MODID, "abilities"))
                .setType(PlayerAbility.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .create();

        REGISTRY_MOB_ITEMS = new RegistryBuilder<ItemOption>()
                .setName(new ResourceLocation(MKUltra.MODID, "mob_items"))
                .setType(ItemOption.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .allowModification()
                .create();

        REGISTRY_MOB_ATTRS = new RegistryBuilder<AttributeRange>()
                .setName(new ResourceLocation(MKUltra.MODID, "mob_attr"))
                .setType(AttributeRange.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .allowModification()
                .create();

        REGISTRY_MOB_AI_MODS = new RegistryBuilder<AIModifier>()
                .setName(new ResourceLocation(MKUltra.MODID, "mob_ai_mods"))
                .setType(AIModifier.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .allowModification()
                .create();

        REGISTRY_MOB_ABILITIES = new RegistryBuilder<MobAbility>()
                .setName(new ResourceLocation(MKUltra.MODID, "mob_abilities"))
                .setType(MobAbility.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .allowModification()
                .create();

        // registries are alphabetical so we want to visit this one after all the other mob config ones
        REGISTRY_MOB_DEF = new RegistryBuilder<MobDefinition>()
                .setName(new ResourceLocation(MKUltra.MODID, "w_mob_def"))
                .setType(MobDefinition.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .allowModification()
                .create();

        REGISTRY_SPAWN_LISTS = new RegistryBuilder<SpawnList>()
                .setName(new ResourceLocation(MKUltra.MODID, "x_mob_spawn_lists"))
                .setType(SpawnList.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .allowModification()
                .create();

        REGISTRY_MOB_FACTIONS = new RegistryBuilder<MobFaction>()
                .setName(new ResourceLocation(MKUltra.MODID, "y_mob_factions"))
                .setType(MobFaction.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .allowModification()
                .create();

        REGISTRY_AI_GENERATORS = new RegistryBuilder<AIGenerator>()
                .setName(new ResourceLocation(MKUltra.MODID, "mob_ai_generators"))
                .setType(AIGenerator.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .allowModification()
                .create();

        REGISTRY_ATTRIBUTE_SETTERS = new RegistryBuilder<AttributeSetter>()
                .setName(new ResourceLocation(MKUltra.MODID, "mob_aattr_setters"))
                .setType(AttributeSetter.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .allowModification()
                .create();

        REGISTRY_CUSTOM_SETTERS = new RegistryBuilder<CustomSetter>()
                .setName(new ResourceLocation(MKUltra.MODID, "mob_custom_setters"))
                .setType(CustomSetter.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .allowModification()
                .create();

        REGISTRY_TALENTS = new RegistryBuilder<BaseTalent>()
                .setName(new ResourceLocation(MKUltra.MODID, "talents"))
                .setType(BaseTalent.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .allowModification()
                .create();

        REGISTRY_TALENT_TREES = new RegistryBuilder<TalentTree>()
                .setName(new ResourceLocation(MKUltra.MODID, "talent_trees"))
                .setType(TalentTree.class)
                .setIDRange(0, Integer.MAX_VALUE - 1)
                .allowModification()
                .create();

        REGISTRY_CLASS_CLIENT_DATA = new RegistryBuilder<ClassClientData>()
                .setName(new ResourceLocation(MKUltra.MODID, "aclass_client_data"))
                .setType(ClassClientData.class)
                .setIDRange(0, Integer.MAX_VALUE -1)
                .allowModification()
                .create();
    }
}
