package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.log.Log;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Remapper {


    @SubscribeEvent
    public static void missingItemMapping(RegistryEvent.MissingMappings<Item> event)
    {
        for (RegistryEvent.MissingMappings.Mapping<Item> entry : event.getAllMappings())
        {
            Log.info("Remapping request for Item %s", entry.key.toString());

            if (entry.key.getResourceDomain().equals(MKUltra.MODID)) {
                if (entry.key.getResourcePath().endsWith("spear")) {
                    Log.info("Ignoring spear %s", entry.key.toString());
                    entry.ignore();
                }
            }
        }
    }

    @SubscribeEvent
    public static void missingBlockMapping(RegistryEvent.MissingMappings<Block> event)
    {
        for (RegistryEvent.MissingMappings.Mapping<Block> entry : event.getAllMappings())
        {
        }
    }
}
