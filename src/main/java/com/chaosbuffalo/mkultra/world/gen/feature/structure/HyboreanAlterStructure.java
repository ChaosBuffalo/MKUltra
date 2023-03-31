package com.chaosbuffalo.mkultra.world.gen.feature.structure;

import com.chaosbuffalo.mknpc.world.gen.feature.structure.MKStructurePieceArgs;
import com.chaosbuffalo.mkultra.init.MKUWorldGen;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.StructureFeature;

import net.minecraft.world.level.levelgen.feature.StructureFeature.StructureStartFactory;

//public class HyboreanAlterStructure extends StructureFeature<NoneFeatureConfiguration> {
//
//
//    public HyboreanAlterStructure(Codec<NoneFeatureConfiguration> config) {
//        super(config);
//    }
//
//    @Override
//    public StructureStartFactory getStartFactory() {
//        return HyboreanAlterStructure.Start::new;
//    }
//
//    @Override
//    public String getFeatureName() {
//        return MKUWorldGen.HYBOREAN_ALTER_NAME.toString();
//    }
//
//
//    public static class Start extends MKStructureStart<NoneFeatureConfiguration> {
//
//        public Start(StructureFeature<NoneFeatureConfiguration> structure, int chunkX, int chunkY,
//                     BoundingBox boundingBox, int refCount, long seed) {
//            super(structure, chunkX, chunkY, boundingBox, refCount, seed);
//        }
//
//        @Override
//        public void getComponents(MKStructurePieceArgs args, NoneFeatureConfiguration config) {
//            HyboreanAlterStructurePieces.getPieces(args, config);
//        }
//    }
//}
