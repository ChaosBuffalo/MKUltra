package com.chaosbuffalo.mkultra.world.gen.feature.structure;

import com.chaosbuffalo.mknpc.world.gen.feature.structure.MKJigsawStructure;
import com.mojang.serialization.Codec;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.VillageConfig;

public class IntroCastleJigsawStructure extends MKJigsawStructure {

    @Override
    protected boolean func_230363_a_(ChunkGenerator chunkGenerator, BiomeProvider biomeProvider,
                                     long seed, SharedSeedRandom random,
                                     int chunkX, int chunkZ, Biome biome,
                                     ChunkPos chunkPos, VillageConfig config) {
        return isInChunk(chunkX, chunkZ);
    }

    private boolean isInChunk(int chunkX, int chunkZ){
        return chunkX == 0 && chunkZ == 0;
    }

    public IntroCastleJigsawStructure(Codec<VillageConfig> codec, int groundLevel, boolean offsetVertical,
                                      boolean offsetFromWorldSurface, boolean allowSpawns) {
        super(codec, groundLevel, offsetVertical, offsetFromWorldSurface, allowSpawns);
    }
}
