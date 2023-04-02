package com.chaosbuffalo.mkultra.world.gen.feature.structure;

import com.chaosbuffalo.mknpc.world.gen.feature.structure.MKJigsawStructure;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;

public class IntroCastleJigsawStructure extends MKJigsawStructure {

    public static final ResourceLocation INTRO_CASTLE_SET_NAME = new ResourceLocation("configured_intro_castle");

    public static final ResourceLocation INTRO_CASTLE_STRUCTURE_NAME = new ResourceLocation("configured_intro_castle");

    private static final ResourceKey<StructureSet> SET_KEY = ResourceKey.create(Registry.STRUCTURE_SET_REGISTRY, INTRO_CASTLE_SET_NAME);

    private static boolean canGenerateInChunk(int chunkX, int chunkZ) {
        return chunkX == 0 && chunkZ == 0;
    }

    public IntroCastleJigsawStructure(Codec<JigsawConfiguration> codec, int groundLevel, boolean offsetVertical,
                                      boolean offsetFromWorldSurface,
                                      boolean allowSpawns) {
        super(codec, groundLevel, offsetVertical, offsetFromWorldSurface, IntroCastleJigsawStructure::checkLocation, allowSpawns);
    }

    private static boolean checkLocation(PieceGeneratorSupplier.Context<JigsawConfiguration> context) {
        ChunkPos chunkPos = context.chunkPos();
        return canGenerateInChunk(chunkPos.x, chunkPos.z) &&
                !context.chunkGenerator().hasFeatureChunkInRange(SET_KEY, context.seed(), chunkPos.x, chunkPos.z, 10);
    }
}
