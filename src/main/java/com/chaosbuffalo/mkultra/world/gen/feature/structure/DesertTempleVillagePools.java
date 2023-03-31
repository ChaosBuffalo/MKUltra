package com.chaosbuffalo.mkultra.world.gen.feature.structure;

import com.chaosbuffalo.mknpc.world.gen.feature.structure.MKSingleJigsawPiece;
import com.chaosbuffalo.mkultra.MKUltra;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.data.worldgen.Pools;

public class DesertTempleVillagePools {

    private static final ResourceLocation DESERT_TEMPLE_STREET_1 = new ResourceLocation(MKUltra.MODID, "desert_temple_village/desert_temple_road_1");
    private static final ResourceLocation DESERT_TEMPLE_SMALL = new ResourceLocation(MKUltra.MODID, "desert_temple_village/cleric_temple_small");
    private static final ResourceLocation DESERT_TEMPLE_VILLAGE_WELL = new ResourceLocation(MKUltra.MODID, "desert_temple_village/desert_well");

    public static final int GEN_DEPTH = 6;

    public static final StructureTemplatePool DESERT_TEMPLE_VILLAGE_BASE = Pools.register(
            new StructureTemplatePool(new ResourceLocation(MKUltra.MODID, "desert_temple_village"),
                    new ResourceLocation("empty"), ImmutableList.of(
                    Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(DESERT_TEMPLE_VILLAGE_WELL, true), 1)), StructureTemplatePool.Projection.TERRAIN_MATCHING));

    public static void registerPatterns() {
        Pools.register(new StructureTemplatePool(new ResourceLocation(MKUltra.MODID, "desert_temple_streets"), new ResourceLocation("empty"),
                ImmutableList.of(
                        Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(DESERT_TEMPLE_STREET_1, true), 1)
                ),
                StructureTemplatePool.Projection.TERRAIN_MATCHING));
        Pools.register(new StructureTemplatePool(new ResourceLocation(MKUltra.MODID, "desert_temples"), new ResourceLocation("empty"),
                ImmutableList.of(
                        Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(DESERT_TEMPLE_SMALL, false), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
    }
}
