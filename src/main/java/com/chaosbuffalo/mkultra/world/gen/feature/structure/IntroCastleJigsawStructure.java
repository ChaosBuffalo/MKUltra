package com.chaosbuffalo.mkultra.world.gen.feature.structure;

import com.chaosbuffalo.mknpc.world.gen.feature.structure.MKJigsawStructure;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.function.Predicate;

public class IntroCastleJigsawStructure extends MKJigsawStructure {



    @Override
    public boolean canGenerate(RegistryAccess p_197172_, ChunkGenerator p_197173_, BiomeSource p_197174_, StructureManager p_197175_, long p_197176_, ChunkPos p_197177_, JigsawConfiguration p_197178_, LevelHeightAccessor p_197179_, Predicate<Holder<Biome>> p_197180_) {
        return isInChunk(p_197177_.x, p_197177_.z) && super.canGenerate(p_197172_, p_197173_, p_197174_, p_197175_, p_197176_, p_197177_, p_197178_, p_197179_, p_197180_);
    }

    private boolean isInChunk(int chunkX, int chunkZ){
        return chunkX == 0 && chunkZ == 0;
    }

    public IntroCastleJigsawStructure(Codec<JigsawConfiguration> codec, int groundLevel, boolean offsetVertical,
                                      boolean offsetFromWorldSurface, Predicate<PieceGeneratorSupplier.Context<JigsawConfiguration>> pieceSupplier,
                                      boolean allowSpawns) {
        super(codec, groundLevel, offsetVertical, offsetFromWorldSurface, pieceSupplier, allowSpawns);
    }
}
