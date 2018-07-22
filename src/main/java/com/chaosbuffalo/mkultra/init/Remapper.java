package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.log.Log;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

@Mod.EventBusSubscriber
public class Remapper {

    /*
    mkultra:copper_threaded_leggings
    mkultra:copper_threaded_helmet
    mkultra:obsidian_chain_chestplate
    mkultra:steel_infused_bone_chestplate
    mkultra:diamond_dusted_invar_leggings
    mkultra:mana_regen_idol_brass
    mkultra:diamond_dusted_invar_chestplate
    mkultra:copper_threaded_boots
    mkultra:steel_infused_bone_leather
    mkultra:diamond_dusted_invar_helmet
    mkultra:steel_infused_bone_leggings
    mkultra:obsidian_chain_boots
    mkultra:mana_regen_idol_silver
    mkultra:obsidian_chain_leggings
    mkultra:diamond_dusted_invar_boots
    mkultra:mana_regen_idol_copper
    mkultra:copper_threaded_cloth
    mkultra:mana_regen_idol_bronze
    mkultra:copper_threaded_chestplate
    mkultra:steel_infused_bone_boots
    mkultra:steel_infused_bone_helmet
    mkultra:obsidian_chain_helmet
     */

    private static ResourceLocation mkxRes(String path) {
        return new ResourceLocation("mkultrax", path);
    }

    private static boolean remapToMKX(RegistryEvent.MissingMappings.Mapping<Item> entry) {

        if (!entry.key.getResourceDomain().equals(MKUltra.MODID)) {
            return false;
        }

        Item newItem = ForgeRegistries.ITEMS.getValue(mkxRes(entry.key.getResourcePath()));

        Log.info("Remapping %s to mkx: %b", entry.key.toString(), newItem != null);

        if (newItem != null) {
            entry.remap(newItem);
            return true;
        } else {
            Log.info("Failed to find remap target for %s", entry.key.toString());
            Runtime.getRuntime().exit(1);
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
            }

            if (entry.key.getResourceDomain().equals("minekampf")) {
                Log.info("Removing old MK Item %s", entry.key.toString());
                entry.ignore();
                continue;
            }

            if (remapToMKX(entry)) {
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
