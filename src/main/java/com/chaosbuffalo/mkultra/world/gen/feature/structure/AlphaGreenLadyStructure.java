//package com.chaosbuffalo.mkultra.world.gen.feature.structure;
//
//import com.chaosbuffalo.mknpc.world.gen.feature.structure.*;
//import com.chaosbuffalo.mkultra.init.MKUWorldGen;
//import com.mojang.serialization.Codec;
//import net.minecraft.util.math.MutableBoundingBox;
//import net.minecraft.world.gen.feature.structure.Structure;
//
//public class AlphaGreenLadyStructure extends SingleChunkStructure {
//
//
//    public AlphaGreenLadyStructure(Codec<ChunkPosConfig> config) {
//        super(config);
//    }
//
//    @Override
//    public IStartFactory getStartFactory() {
//        return AlphaGreenLadyStructure.Start::new;
//    }
//
////    @Override
////    public String getStructureName() {
////        return MKUWorldGen.ALPHA_GREEN_LADY_NAME.toString();
////    }
//
//
//    public static class Start extends MKStructureStart<ChunkPosConfig> {
//
//        public Start(Structure<ChunkPosConfig> structure, int chunkX, int chunkY,
//                     MutableBoundingBox boundingBox, int refCount, long seed) {
//            super(structure, chunkX, chunkY, boundingBox, refCount, seed);
//        }
//
//        @Override
//        public void getComponents(MKStructurePieceArgs args, ChunkPosConfig config) {
//            AlphaGreenLadyStructurePieces.getPieces(args, config);
//        }
//    }
//}