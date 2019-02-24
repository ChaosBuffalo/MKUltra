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
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.ArrayList;
import java.util.Map;

@Mod.EventBusSubscriber
public class Remapper {

    private static final String MKX_MOD_ID = "mkultrax";

    private static Map<ResourceLocation, ResourceLocation> itemReplacements = Maps.newHashMap();
    private static Map<ResourceLocation, ResourceLocation> blockReplacements = Maps.newHashMap();
    private static Map<ResourceLocation, ResourceLocation> entityReplacements = Maps.newHashMap();
    private static ArrayList<ResourceLocation> drops = Lists.newArrayList();

    static {
        internalItemReplacement("sunicon", "sun_icon");
        internalItemReplacement("bonedleather", "boned_leather");
        internalItemReplacement("bonedleatherchestplate", "boned_leather_chestplate");
        internalItemReplacement("bonedleatherhelmet", "boned_leather_helmet");
        internalItemReplacement("bonedleatherleggings", "boned_leather_leggings");
        internalItemReplacement("bonedleatherboots", "boned_leather_boots");
        internalItemReplacement("xptable", "xp_table");
        internalItemReplacement("drownProjectile", "drown_projectile");
        internalItemReplacement("geyserProjectile", "geyser_projectile");
        internalBlockReplacement("xptable", "xp_table");

        internalEntityReplacement("EntityMKAreaEffect", "mk_area_effect");
        internalEntityReplacement("EntityDrownProjectile", "drown_projectile");
        internalEntityReplacement("EntityGeyserProjectile", "geyser_projectile");
        internalEntityReplacement("EntityBallLightningProjectile", "ball_lightning_projectile");
        internalEntityReplacement("DualityRuneProjectile", "duality_rune_projectile");
        internalEntityReplacement("WhirlpoolProjectile", "whirlpool_projectile");
        internalEntityReplacement("FlameBladeProjectile", "flame_blade_projectile");
        internalEntityReplacement("FairyFireProjectile", "fairy_fire_projectile");
        internalEntityReplacement("CleansingSeedProjectile", "cleansing_seed_projectile");
        internalEntityReplacement("SpiritBombProjectile", "spirit_bomb_projectile");
        internalEntityReplacement("MobFireballProjectile", "mob_fireball_projectile");

        drops.add(new ResourceLocation(MKUltra.MODID, "steampoweredorbblock"));
        drops.add(new ResourceLocation(MKUltra.MODID, "portalblock"));
        drops.add(new ResourceLocation(MKUltra.MODID, "ropeblock"));
        drops.add(new ResourceLocation(MKUltra.MODID, "hempfibers"));
        drops.add(new ResourceLocation(MKUltra.MODID, "hempleaves"));
        drops.add(new ResourceLocation(MKUltra.MODID, "hempblock"));
    }

    private static void internalItemReplacement(String oldName, String newName) {
        itemReplacements.put(new ResourceLocation(MKUltra.MODID, oldName), new ResourceLocation(MKUltra.MODID, newName));
    }

    private static void internalBlockReplacement(String oldName, String newName) {
        blockReplacements.put(new ResourceLocation(MKUltra.MODID, oldName), new ResourceLocation(MKUltra.MODID, newName));
    }

    private static void internalEntityReplacement(String oldName, String newName) {
        entityReplacements.put(new ResourceLocation(MKUltra.MODID, oldName), new ResourceLocation(MKUltra.MODID, newName));
    }

    public static void replace(ResourceLocation oldName, ResourceLocation newName) {
        itemReplacements.put(oldName, newName);
    }

    private static boolean tryRemapToMKX(RegistryEvent.MissingMappings.Mapping<Item> entry) {

        // Only remap items that used to be in the mkultra core
        if (!entry.key.getNamespace().equals(MKUltra.MODID)) {
            return false;
        }

        ResourceLocation mkxPath = new ResourceLocation(MKX_MOD_ID, entry.key.getPath());

        if (tryRemapItem(entry, mkxPath)) {
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

    private static boolean tryRemapItem(RegistryEvent.MissingMappings.Mapping<Item> entry, ResourceLocation newKey) {
        Item item = ForgeRegistries.ITEMS.getValue(newKey);
        Log.info("Remapping Item %s to %s: %b", entry.key.toString(), newKey.toString(), item != null);
        if (item != null) {
            entry.remap(item);
            return true;
        }
        return false;
    }

    private static boolean tryRemapBlock(RegistryEvent.MissingMappings.Mapping<Block> entry, ResourceLocation newKey) {
        Block block = ForgeRegistries.BLOCKS.getValue(newKey);
        Log.info("Remapping Block %s to %s: %b", entry.key.toString(), newKey.toString(), block != null);
        if (block != null) {
            entry.remap(block);
            return true;
        }
        return false;
    }

    private static boolean tryRemapEntity(RegistryEvent.MissingMappings.Mapping<EntityEntry> entry, ResourceLocation newKey) {
        EntityEntry entity = ForgeRegistries.ENTITIES.getValue(newKey);
        Log.info("Remapping Entity %s to %s: %b", entry.key.toString(), newKey.toString(), entity != null);
        if (entity != null) {
            entry.remap(entity);
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

            if (itemReplacements.containsKey(entry.key)) {
                ResourceLocation newKey = itemReplacements.get(entry.key);
                if (tryRemapItem(entry, newKey)) {
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

            if (blockReplacements.containsKey(entry.key)) {
                ResourceLocation newKey = blockReplacements.get(entry.key);
                if (tryRemapBlock(entry, newKey)) {
                    continue;
                }
            }

            if (drops.contains(entry.key)) {
                Log.info("Removing old MK Block %s", entry.key.toString());
                entry.ignore();
                continue;
            }
        }
    }

    @SubscribeEvent
    public static void missingEntityMapping(RegistryEvent.MissingMappings<EntityEntry> event) {
        for (RegistryEvent.MissingMappings.Mapping<EntityEntry> entry : event.getAllMappings()) {
            if (entityReplacements.containsKey(entry.key)) {
                ResourceLocation newKey = entityReplacements.get(entry.key);
                if (tryRemapEntity(entry, newKey)) {
                    continue;
                }
            }
        }
    }
}
