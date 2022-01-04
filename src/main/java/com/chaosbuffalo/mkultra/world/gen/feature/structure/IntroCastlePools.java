package com.chaosbuffalo.mkultra.world.gen.feature.structure;

import com.chaosbuffalo.mknpc.world.gen.feature.structure.MKSingleJigsawPiece;
import com.chaosbuffalo.mkultra.MKUltra;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;

public class IntroCastlePools {
    private static final ResourceLocation GREEN_KNIGHT_ISLAND = new ResourceLocation(MKUltra.MODID, "intro_castle/green_knight_island");
    private static final ResourceLocation CASTLE_TOP = new ResourceLocation(MKUltra.MODID, "intro_castle/castle_top");
    private static final ResourceLocation CASTLE_BASE = new ResourceLocation(MKUltra.MODID, "intro_castle/castle_base");

    public static final int GEN_DEPTH = 7;

    public static final JigsawPattern INTRO_CASTLE_BASE = JigsawPatternRegistry.func_244094_a(
            new JigsawPattern(new ResourceLocation(MKUltra.MODID, "intro_castle.green_knight_island"),
                    new ResourceLocation("empty"), ImmutableList.of(
                    Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(GREEN_KNIGHT_ISLAND, false), 1)), JigsawPattern.PlacementBehaviour.RIGID));

    public static void registerPatterns() {
        JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation(MKUltra.MODID, "intro_castle.castle_top"), new ResourceLocation("empty"),
                ImmutableList.of(
                        Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(CASTLE_TOP, false), 1)
                ),
                JigsawPattern.PlacementBehaviour.RIGID));
        JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation(MKUltra.MODID, "intro_castle.castle_base"), new ResourceLocation("empty"),
                ImmutableList.of(
                        Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(CASTLE_BASE, false), 1)
                ),
                JigsawPattern.PlacementBehaviour.RIGID));
//        JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation(MKUltra.MODID, "intro_castle.green_knight_island"), new ResourceLocation("empty"),
//                ImmutableList.of(
//                        Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(GREEN_KNIGHT_ISLAND, false), 1)
//                ),
//                JigsawPattern.PlacementBehaviour.RIGID));
    }
}
