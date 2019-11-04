package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.core.classes.JsonConfiguredClass;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.utils.JsonLoader;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;

import javax.annotation.Resource;

@Mod.EventBusSubscriber
@GameRegistry.ObjectHolder(MKUltra.MODID)
public class ModPlayerClasses {

    @GameRegistry.ObjectHolder("client_data.sun_icon")
    public static ClassClientData sunIcon;
    @GameRegistry.ObjectHolder("client_data.moon_icon")
    public static ClassClientData moonIcon;
    @GameRegistry.ObjectHolder("client_data.desperate_icon")
    public static ClassClientData desperateIcon;
    @GameRegistry.ObjectHolder("client_data.ranger_icon")
    public static ClassClientData rangerIcon;

    public static void postInitJsonRegistration() {
        ModContainer old = Loader.instance().activeModContainer();
        JsonLoader.loadModsForType("mk_classes",
                "mk_overrides", "assets",
                ModPlayerClasses::loadPlayerClass, MKURegistry.REGISTRY_CLASSES);
        Loader.instance().setActiveModContainer(old);
    }

    @SubscribeEvent
    public static void registerClassClientData(RegistryEvent.Register<ClassClientData> evt){
        evt.getRegistry().register(new ClassClientData(
                new ResourceLocation(MKUltra.MODID, "client_data.sun_icon"),
                new ResourceLocation(MKUltra.MODID, "textures/class/icons/sun.png"),
                new ResourceLocation(MKUltra.MODID, "textures/gui/xp_table_background.png"),
                38600
                ));
        evt.getRegistry().register(new ClassClientData(
                new ResourceLocation(MKUltra.MODID, "client_data.moon_icon"),
                new ResourceLocation(MKUltra.MODID, "textures/class/icons/moon.png"),
                new ResourceLocation(MKUltra.MODID, "textures/gui/xp_table_background_moon.png"),
                4404838
        ));
        evt.getRegistry().register(new ClassClientData(
                new ResourceLocation(MKUltra.MODID, "client_data.desperate_icon"),
                new ResourceLocation(MKUltra.MODID, "textures/class/icons/desperate.png"),
                new ResourceLocation(MKUltra.MODID, "textures/gui/xp_table_background_desperate.png"),
                32025
        ));
        evt.getRegistry().register(new ClassClientData(
                new ResourceLocation(MKUltra.MODID, "client_data.ranger_icon"),
                new ResourceLocation(MKUltra.MODID,  "textures/class/icons/ranger.png"),
                new ResourceLocation(MKUltra.MODID,  "textures/gui/xp_table_background_ranger.png"),
                16707252
        ));
        for (ResourceLocation key : evt.getRegistry().getKeys()){
            Log.info("registered client data: %s", key.toString());
        }
    }


    private static ArmorClass getArmorClassFromName(String name) {
        switch (name) {
            case "heavy":
                return ArmorClass.HEAVY;
            case "medium":
                return ArmorClass.MEDIUM;
            case "light":
                return ArmorClass.LIGHT;
            case "robes":
                return ArmorClass.ROBES;
            case "all":
            default:
                return ArmorClass.ALL;
        }
    }

    public static void loadPlayerClass(ResourceLocation name, JsonObject obj,
                                      IForgeRegistry<PlayerClass> registry) {
        String[] keys = {"baseHealth", "healthPerLevel", "baseManaRegen", "manaRegenPerLevel",
                         "baseMana", "manaPerLevel", "armorClass", "abilities", "clientData"};
        if (!JsonLoader.checkKeysExist(obj, keys)) {
            return;
        }
        ResourceLocation realName = new ResourceLocation(name.getNamespace(), "class." + name.getPath());
        JsonConfiguredClass newClass = new JsonConfiguredClass(realName);
        newClass.setBaseHealth(obj.get("baseHealth").getAsInt());
        newClass.setHealthPerLevel(obj.get("healthPerLevel").getAsInt());
        newClass.setBaseManaRegen(obj.get("baseManaRegen").getAsFloat());
        newClass.setManaRegenPerLevel(obj.get("manaRegenPerLevel").getAsFloat());
        newClass.setBaseMana(obj.get("baseMana").getAsInt());
        newClass.setManaPerLevel(obj.get("manaPerLevel").getAsInt());
        String armorClassType = obj.get("armorClass").getAsString();
        newClass.setArmorClass(getArmorClassFromName(armorClassType));
        ResourceLocation clientDataName = new ResourceLocation(obj.get("clientData").getAsString());
        ClassClientData clientData = MKURegistry.getClassClientData(clientDataName);
        if (clientData != null) {
            newClass.setClientData(clientData);
        } else {
            Log.info("Failed to load client data for Player Class: %s, %s", realName.toString(),
                    obj.get("clientData").getAsString());
            return;
        }
        JsonArray abilities = obj.get("abilities").getAsJsonArray();
        for (JsonElement abilityEle : abilities){
            ResourceLocation abilityName = new ResourceLocation(abilityEle.getAsString());
            PlayerAbility ability = MKURegistry.getAbility(abilityName);
            if (ability != null){
                newClass.addAbility(ability);
            } else {
                Log.info("Failed to load ability for Player Class: %s, %s", realName.toString(),
                        abilityName.toString());
            }
        }
        Log.info("Registering Player Class: %s", realName.toString());
        if (registry instanceof IForgeRegistryModifiable) {
            IForgeRegistryModifiable modRegistry = (IForgeRegistryModifiable) registry;
            modRegistry.remove(realName);
        }
        newClass.setRegistryName(realName);
        registry.register(newClass);
    }
}

