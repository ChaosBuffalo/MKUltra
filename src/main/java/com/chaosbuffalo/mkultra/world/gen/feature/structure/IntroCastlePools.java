package com.chaosbuffalo.mkultra.world.gen.feature.structure;

import com.chaosbuffalo.mknpc.world.gen.feature.structure.MKSingleJigsawPiece;
import com.chaosbuffalo.mkultra.MKUltra;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.data.worldgen.Pools;

public class IntroCastlePools {
    private static final ResourceLocation GREEN_KNIGHT_ISLAND = new ResourceLocation(MKUltra.MODID, "intro_castle/green_knight_island");
    private static final ResourceLocation CASTLE_TOP = new ResourceLocation(MKUltra.MODID, "intro_castle/castle_top");
    private static final ResourceLocation CASTLE_BASE = new ResourceLocation(MKUltra.MODID, "intro_castle/castle_base");

    public static final int GEN_DEPTH = 7;

    public static final StructureTemplatePool INTRO_CASTLE_BASE = Pools.register(
            new StructureTemplatePool(new ResourceLocation(MKUltra.MODID, "intro_castle.green_knight_island"),
                    new ResourceLocation("empty"), ImmutableList.of(
                    Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(GREEN_KNIGHT_ISLAND, false), 1)), StructureTemplatePool.Projection.RIGID));

    public static void registerPatterns() {
        Pools.register(new StructureTemplatePool(new ResourceLocation(MKUltra.MODID, "intro_castle.castle_top"), new ResourceLocation("empty"),
                ImmutableList.of(
                        Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(CASTLE_TOP, false), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
        Pools.register(new StructureTemplatePool(new ResourceLocation(MKUltra.MODID, "intro_castle.castle_base"), new ResourceLocation("empty"),
                ImmutableList.of(
                        Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(CASTLE_BASE, false), 1)
                ),
                StructureTemplatePool.Projection.RIGID));
    }
}
