package com.chaosbuffalo.mkultra.world.gen.feature.structure;

import com.chaosbuffalo.mknpc.world.gen.feature.structure.MKJigsawStructure;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;

public class IntroCastleJigsawStructure extends MKJigsawStructure {

    @Override
    protected boolean isFeatureChunk(ChunkGenerator p_160455_, BiomeSource p_160456_, long p_160457_, WorldgenRandom p_160458_, ChunkPos p_160459_, Biome p_160460_, ChunkPos p_160461_, JigsawConfiguration p_160462_, LevelHeightAccessor p_160463_) {
        return isInChunk(p_160459_.x, p_160459_.z);
    }

    private boolean isInChunk(int chunkX, int chunkZ){
        return chunkX == 0 && chunkZ == 0;
    }

    public IntroCastleJigsawStructure(Codec<JigsawConfiguration> codec, int groundLevel, boolean offsetVertical,
                                      boolean offsetFromWorldSurface, boolean allowSpawns) {
        super(codec, groundLevel, offsetVertical, offsetFromWorldSurface, allowSpawns);
    }
}
