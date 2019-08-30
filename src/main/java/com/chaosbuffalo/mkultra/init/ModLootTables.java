package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;

public final class ModLootTables {

    public static void init() {
        LootTableList.register(new ResourceLocation(MKUltra.MODID, "entities/skeletal_ranger"));
    }
}
