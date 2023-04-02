package com.chaosbuffalo.mkultra.world.gen.feature.structure;

import com.chaosbuffalo.mknpc.world.gen.feature.structure.MKSingleJigsawPiece;
import com.chaosbuffalo.mkultra.MKUltra;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class CryptStructurePools {
    private static final ResourceLocation CRYPT_CORNER_LEFT_1 = new ResourceLocation(MKUltra.MODID, "crypt/crypt_corner_left_1");
    private static final ResourceLocation CRYPT_CORNER_RIGHT_1 = new ResourceLocation(MKUltra.MODID, "crypt/crypt_corner_right_1");
    private static final ResourceLocation CRYPT_ENTRANCE = new ResourceLocation(MKUltra.MODID, "crypt/crypt_entrance");
    private static final ResourceLocation CRYPT_FLAT_HALLWAY_LONG_1 = new ResourceLocation(MKUltra.MODID, "crypt/crypt_flat_hallway_long_1");
    private static final ResourceLocation CRYPT_FLAT_HALLWAY_SHORT_1 = new ResourceLocation(MKUltra.MODID, "crypt/crypt_flat_hallway_short_1");
    private static final ResourceLocation CRYPT_HALLWAY_1 = new ResourceLocation(MKUltra.MODID, "crypt/crypt_hallway_1");
    private static final ResourceLocation CRYPT_ROOM_1 = new ResourceLocation(MKUltra.MODID, "crypt/crypt_room_1");
    private static final ResourceLocation CRYPT_ROOM_2 = new ResourceLocation(MKUltra.MODID, "crypt/crypt_room_2");
    private static final ResourceLocation CRYPT_ROOM_3 = new ResourceLocation(MKUltra.MODID, "crypt/crypt_room_3");
    private static final ResourceLocation CRYPT_ROOM_DECAYING = new ResourceLocation(MKUltra.MODID, "crypt/crypt_room_decaying");
    private static final ResourceLocation CRYPT_ROOM_REMEMBRANCE = new ResourceLocation(MKUltra.MODID, "crypt/crypt_room_remembrance_hall");
    private static final ResourceLocation CRYPT_ROOM_TOMBS = new ResourceLocation(MKUltra.MODID, "crypt/crypt_room_tombs");

    public static final int GEN_DEPTH = 7;

    public static final StructureTemplatePool CRYPT_BASE = new StructureTemplatePool(
            new ResourceLocation(MKUltra.MODID, "crypt/crypt_base"),
            new ResourceLocation("empty"),
            ImmutableList.of(
                    Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(CRYPT_ENTRANCE, false), 1)
            ),
            StructureTemplatePool.Projection.RIGID);

    public static final StructureTemplatePool CRYPT_HALLWAYS = new StructureTemplatePool(
            new ResourceLocation(MKUltra.MODID, "crypt_hallways"),
            new ResourceLocation("empty"),
            ImmutableList.of(
                    Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(CRYPT_HALLWAY_1, false), 1),
                    Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(CRYPT_FLAT_HALLWAY_LONG_1, false), 1),
                    Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(CRYPT_FLAT_HALLWAY_SHORT_1, false), 1),
                    Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(CRYPT_CORNER_RIGHT_1, false), 1),
                    Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(CRYPT_CORNER_LEFT_1, false), 1)
            ),
            StructureTemplatePool.Projection.RIGID);

    public static final StructureTemplatePool CRYPT_ROOMS = new StructureTemplatePool(
            new ResourceLocation(MKUltra.MODID, "crypt_rooms"),
            new ResourceLocation("empty"),
            ImmutableList.of(
                    Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(CRYPT_ROOM_1, false), 2),
                    Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(CRYPT_ROOM_2, false), 2),
                    Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(CRYPT_ROOM_3, false), 1),
                    Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(CRYPT_ROOM_DECAYING, false), 3),
                    Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(CRYPT_ROOM_TOMBS, false), 1),
                    Pair.of(MKSingleJigsawPiece.getMKSingleJigsaw(CRYPT_ROOM_REMEMBRANCE, false), 1)

            ),
            StructureTemplatePool.Projection.RIGID);

}