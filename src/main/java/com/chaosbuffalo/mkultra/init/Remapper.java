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

    private static Map<String, String> internalReplacements = Maps.newHashMap();
    private static ArrayList<ResourceLocation> drops = Lists.newArrayList();

    static {
        internalReplacements.put("sunicon", "sun_icon");
        internalReplacements.put("bonedleather", "boned_leather");
        internalReplacements.put("bonedleatherchestplate", "boned_leather_chestplate");
        internalReplacements.put("bonedleatherhelmet", "boned_leather_helmet");
        internalReplacements.put("bonedleatherleggings", "boned_leather_leggings");
        internalReplacements.put("bonedleatherboots", "boned_leather_boots");

        drops.add(new ResourceLocation(MKUltra.MODID, "steampoweredorbblock"));
        drops.add(new ResourceLocation(MKUltra.MODID, "portalblock"));
    }

    private static boolean tryRemapToMKX(RegistryEvent.MissingMappings.Mapping<Item> entry) {

        // Only remap items that used to be in the mkultra core
        if (!entry.key.getResourceDomain().equals(MKUltra.MODID)) {
            return false;
        }

        ResourceLocation mkxPath = new ResourceLocation(MKX_MOD_ID, entry.key.getResourcePath());

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

            if (entry.key.getResourceDomain().equals(MKUltra.MODID)) {
                String key = entry.key.getResourcePath();
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

                if (internalReplacements.containsKey(key)) {
                    ResourceLocation newKey = new ResourceLocation(MKUltra.MODID, internalReplacements.get(key));
                    if (tryRemap(entry, newKey)) {
                        continue;
                    }
                }

            } else if (entry.key.getResourceDomain().equals("minekampf")) {
                Log.info("Removing old MK Item %s", entry.key.toString());
                entry.ignore();
                continue;
            }

            if (tryRemapToMKX(entry)) {
                continue;
            }
        }
    }

    @SubscribeEvent
    public static void missingBlockMapping(RegistryEvent.MissingMappings<Block> event) {
        for (RegistryEvent.MissingMappings.Mapping<Block> entry : event.getAllMappings()) {
            if (entry.key.getResourceDomain().equals("minekampf")) {
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
