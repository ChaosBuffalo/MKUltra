package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.log.Log;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod.EventBusSubscriber
public class Remapper {

    static final String MKX_MOD_ID = "mkultrax";


    private static ResourceLocation mkxRes(String path) {
        return new ResourceLocation(MKX_MOD_ID, path);
    }

    private static boolean tryRemapToMKX(RegistryEvent.MissingMappings.Mapping<Item> entry) {

        // Only remap items that used to be in the mkultra core
        if (!entry.key.getResourceDomain().equals(MKUltra.MODID)) {
            return false;
        }

        ResourceLocation mkxPath = mkxRes(entry.key.getResourcePath());
        Item newItem = ForgeRegistries.ITEMS.getValue(mkxPath);

        Log.info("Remapping %s to mkx %s: %b", entry.key.toString(), mkxPath.toString(), newItem != null);

        if (newItem != null) {
            entry.remap(newItem);
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

    @SubscribeEvent
    public static void missingItemMapping(RegistryEvent.MissingMappings<Item> event) {
        for (RegistryEvent.MissingMappings.Mapping<Item> entry : event.getAllMappings()) {
            Log.info("Remapping request for Item %s", entry.key.toString());

            if (entry.key.getResourceDomain().equals(MKUltra.MODID)) {
                if (entry.key.getResourcePath().endsWith("spear")) {
                    Log.info("Ignoring spear %s", entry.key.toString());
                    entry.ignore();
                    continue;
                }

                // sunicon -> sun_icon
                if (entry.key.getResourcePath().equals("sunicon")) {
                    entry.remap(ModItems.sun_icon);
                    continue;
                }
            }

            if (entry.key.getResourceDomain().equals("minekampf")) {
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
        }
    }
}
