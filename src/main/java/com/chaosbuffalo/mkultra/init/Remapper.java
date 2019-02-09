package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.log.Log;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.Map;

@Mod.EventBusSubscriber
public class Remapper {

    private static final String MKX_MOD_ID = "mkultrax";

    private static Map<ResourceLocation, ResourceLocation> replacements = Maps.newHashMap();
    private static ArrayList<ResourceLocation> drops = Lists.newArrayList();

    static {
        internalReplacement("sunicon", "sun_icon");
        internalReplacement("bonedleather", "boned_leather");
        internalReplacement("bonedleatherchestplate", "boned_leather_chestplate");
        internalReplacement("bonedleatherhelmet", "boned_leather_helmet");
        internalReplacement("bonedleatherleggings", "boned_leather_leggings");
        internalReplacement("bonedleatherboots", "boned_leather_boots");

        drops.add(new ResourceLocation(MKUltra.MODID, "steampoweredorbblock"));
        drops.add(new ResourceLocation(MKUltra.MODID, "portalblock"));
        drops.add(new ResourceLocation(MKUltra.MODID, "ropeblock"));
        drops.add(new ResourceLocation(MKUltra.MODID, "hempfibers"));
        drops.add(new ResourceLocation(MKUltra.MODID, "hempleaves"));
        drops.add(new ResourceLocation(MKUltra.MODID, "hempblock"));
    }

    private static void internalReplacement(String oldName, String newName) {
        replacements.put(new ResourceLocation(MKUltra.MODID, oldName), new ResourceLocation(MKUltra.MODID, newName));
    }

    public static void replace(ResourceLocation oldName, ResourceLocation newName) {
        replacements.put(oldName, newName);
    }

    private static boolean tryRemapToMKX(RegistryEvent.MissingMappings.Mapping<Item> entry) {

        // Only remap items that used to be in the mkultra core
        if (!entry.key.getNamespace().equals(MKUltra.MODID)) {
            return false;
        }

        ResourceLocation mkxPath = new ResourceLocation(MKX_MOD_ID, entry.key.getPath());

        if (tryRemap(entry, mkxPath)) {
            return true;
        }
        else if (!Loader.isModLoaded(MKX_MOD_ID)) {
            Log.info("MKCompat not found! Dropping item %s", entry.key.toString());
            entry.ignore();
            return true;
        }
        else {
            Log.info("Failed to find remap target for %s", entry.key.toString());
            FMLCommonHandler.instance().exitJava(1, false);
        }

        return false;
    }

    static boolean tryRemap(RegistryEvent.MissingMappings.Mapping<Item> entry, ResourceLocation newKey) {
        Item replItem = ForgeRegistries.ITEMS.getValue(newKey);
        Log.info("Remapping %s to %s: %b", entry.key.toString(), newKey.toString(), replItem != null);
        if (replItem != null) {
            entry.remap(replItem);
            return true;
        }
        return false;
    }


    @SubscribeEvent
    public static void missingItemMapping(RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> entry : event.getAllMappings()) {
            Log.info("Remapping request for Item %s", entry.key.toString());

            if (entry.key.getNamespace().equals(MKUltra.MODID)) {
                String key = entry.key.getPath();
                if (key.endsWith("spear")) {
                    Log.info("Ignoring spear %s", entry.key.toString());
                    entry.ignore();
                    continue;
                }

                if (drops.contains(entry.key)) {
                    Log.info("Removing old MK Item %s", entry.key.toString());
                    entry.ignore();
                    continue;
                }



            } else if (entry.key.getNamespace().equals("minekampf")) {
                Log.info("Removing old MK Item %s", entry.key.toString());
                entry.ignore();
                continue;
            }

            if (replacements.containsKey(entry.key)) {
                ResourceLocation newKey = replacements.get(entry.key);
                if (tryRemap(entry, newKey)) {
                    continue;
                }
            }

            if (tryRemapToMKX(entry)) {
                continue;
            }
        }
    }

    @SubscribeEvent
    public static void missingBlockMapping(RegistryEvent.MissingMappings<Block> event) {
        for (RegistryEvent.MissingMappings.Mapping<Block> entry : event.getAllMappings()) {
            if (entry.key.getNamespace().equals("minekampf")) {
                Log.info("Removing old MK Block %s", entry.key.toString());
                entry.ignore();
                continue;
            }

            if (drops.contains(entry.key)) {
                Log.info("Removing old MK Block %s", entry.key.toString());
                entry.ignore();
                continue;
            }
        }
    }
}
