package com.chaosbuffalo.mkultra.world.gen.feature.structure;

import com.chaosbuffalo.mknpc.world.gen.feature.structure.MKStructurePieceArgs;
import com.chaosbuffalo.mknpc.world.gen.feature.structure.MKStructureStart;
import com.chaosbuffalo.mkultra.init.MKUWorldGen;
import com.mojang.serialization.Codec;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;

public class HyboreanAlterStructure extends Structure<NoFeatureConfig> {


    public HyboreanAlterStructure(Codec<NoFeatureConfig> config) {
        super(config);
    }

    @Override
    public IStartFactory getStartFactory() {
        return HyboreanAlterStructure.Start::new;
    }

    @Override
    public String getStructureName() {
        return MKUWorldGen.HYBOREAN_ALTER_NAME.toString();
    }


    public static class Start extends MKStructureStart<NoFeatureConfig> {

        public Start(Structure<NoFeatureConfig> structure, int chunkX, int chunkY,
                     MutableBoundingBox boundingBox, int refCount, long seed) {
            super(structure, chunkX, chunkY, boundingBox, refCount, seed);
        }

        @Override
        public void getComponents(MKStructurePieceArgs args, NoFeatureConfig config) {
            HyboreanAlterStructurePieces.getPieces(args, config);
        }
    }
}
