package com.chaosbuffalo.mkultra.data_generators;

import com.chaosbuffalo.mknpc.data.ConfiguredStructureProvider;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.MKUWorldGen;
import com.chaosbuffalo.mkultra.world.gen.feature.structure.DesertTempleVillagePools;
import com.chaosbuffalo.mkultra.world.gen.feature.structure.IntroCastlePools;
import com.chaosbuffalo.mkultra.world.gen.feature.structure.NecrotideAlterPools;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

import java.io.IOException;

public class MKUConfiguredStructureProvider extends ConfiguredStructureProvider {
    public MKUConfiguredStructureProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void run(HashCache cache) throws IOException {
        TagKey<Biome> hasIntroCastle = TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(MKUltra.MODID, "has_structure/has_intro_castle"));
        writeFeature(new ConfiguredStructureData(new ResourceLocation(MKUltra.MODID, "configured_intro_castle"),
                IntroCastlePools.ISLAND_POOL.getName(), IntroCastlePools.GEN_DEPTH, hasIntroCastle, MKUWorldGen.INTRO_CASTLE.get()), cache);
        writeFeature(new ConfiguredStructureData(new ResourceLocation(MKUltra.MODID, "configured_desert_temple_village"),
                DesertTempleVillagePools.DESERT_TEMPLE_VILLAGE_BASE.getName(), DesertTempleVillagePools.GEN_DEPTH,
                BiomeTags.HAS_VILLAGE_DESERT, MKUWorldGen.DESERT_TEMPLE_VILLAGE_STRUCTURE.get()), cache);
        writeFeature(new ConfiguredStructureData(new ResourceLocation(MKUltra.MODID, "configured_necrotide_alter"),
                NecrotideAlterPools.BASE.getName(), NecrotideAlterPools.GEN_DEPTH,
                BiomeTags.HAS_DESERT_PYRAMID, MKUWorldGen.NECROTIDE_ALTER.get()), cache);
    }
}
