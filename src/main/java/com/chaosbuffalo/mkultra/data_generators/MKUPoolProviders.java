package com.chaosbuffalo.mkultra.data_generators;

import com.chaosbuffalo.mknpc.data.TemplatePoolProvider;
import com.chaosbuffalo.mkultra.world.gen.feature.structure.DesertTempleVillagePools;
import com.chaosbuffalo.mkultra.world.gen.feature.structure.IntroCastlePools;
import com.chaosbuffalo.mkultra.world.gen.feature.structure.NecrotideAlterPools;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;

import java.io.IOException;

public class MKUPoolProviders extends TemplatePoolProvider {

    public MKUPoolProviders(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void run(HashCache cache) throws IOException {
        writePool(DesertTempleVillagePools.DESERT_TEMPLE_VILLAGE_BASE, cache);
        writePool(DesertTempleVillagePools.DESERT_TEMPLE_STREETS, cache);
        writePool(DesertTempleVillagePools.DESERT_TEMPLES, cache);
        writePool(IntroCastlePools.ISLAND_POOL, cache);
        writePool(IntroCastlePools.INTRO_CASTLE_TOP, cache);
        writePool(IntroCastlePools.INTRO_CASTLE_BASE, cache);
        writePool(NecrotideAlterPools.BASE, cache);
        writePool(NecrotideAlterPools.TOWERS, cache);
    }
}
