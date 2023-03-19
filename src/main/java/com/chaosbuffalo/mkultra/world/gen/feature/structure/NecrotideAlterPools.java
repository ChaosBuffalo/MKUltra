package com.chaosbuffalo.mkultra.world.gen.feature.structure;

import com.chaosbuffalo.mknpc.world.gen.feature.structure.MKSingleJigsawPiece;
import com.chaosbuffalo.mkultra.MKUltra;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPatternRegistry;

public class NecrotideAlterPools {
    private static final ResourceLocation BASE_NAME = new ResourceLocation(MKUltra.MODID, "necrotide_alter/base");
    private static final ResourceLocation TOWER_LEFT = new ResourceLocation(MKUltra.MODID, "necrotide_alter/tower_left");
    private static final ResourceLocation TOWER_RIGHT = new ResourceLocation(MKUltra.MODID, "necrotide_alter/tower_right");

    public static final int GEN_DEPTH = 7;

    public static final JigsawPattern BASE = JigsawPatternRegistry.func_244094_a(
            new JigsawPattern(new ResourceLocation(MKUltra.MODID, "necrotide_alter/base"),
                    new ResourceLocation("empty"), ImmutableList.of(
                    Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(BASE_NAME, false), 1)), JigsawPattern.PlacementBehaviour.TERRAIN_MATCHING));

    public static void registerPatterns() {
        JigsawPatternRegistry.func_244094_a(new JigsawPattern(new ResourceLocation(MKUltra.MODID, "necrotide_alter/towers"), new ResourceLocation("empty"),
                ImmutableList.of(
                        Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(TOWER_LEFT, false), 1),
                        Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(TOWER_RIGHT, false), 1)
                ),
                JigsawPattern.PlacementBehaviour.RIGID));
    }
}
