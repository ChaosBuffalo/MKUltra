package com.chaosbuffalo.mkultra.world.gen.feature.structure;

import com.chaosbuffalo.mknpc.world.gen.StructureUtils;
import com.chaosbuffalo.mknpc.world.gen.feature.structure.ChunkPosConfig;
import com.chaosbuffalo.mknpc.world.gen.feature.structure.MKStructurePieceArgs;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.MKUWorldGen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.Random;
import java.util.UUID;

//public class HyboreanAlterStructurePieces {
//
//    private static final ResourceLocation PIECE_ONE = new ResourceLocation(MKUltra.MODID, "hyborean_alter");
//
//
//    public static void getPieces(MKStructurePieceArgs args, NoneFeatureConfiguration config){
//        args.componentsOut.add(new HyboreanAlterStructurePieces.Piece(
//                args.templateManager,
//                PIECE_ONE, args.blockPos, args.rotation,
//                args.structure.getRegistryName(), args.structureId));
//    }
//
//
//    public static class Piece extends MKTemplateStructurePiece {
//        private final ResourceLocation loc;
//        private final Rotation rotation;
//
//
//        public Piece(StructureManager templateManager, ResourceLocation loc,
//                     BlockPos blockPos, Rotation rotation, ResourceLocation structureName, UUID instanceId) {
//            super(MKUWorldGen.HYBOREAN_ALTER_TYPE, 0, structureName, instanceId);
//            this.loc = loc;
//            this.rotation = rotation;
//            this.templatePosition = blockPos;
//            setManager(templateManager);
//        }
//
//        public Piece(StructureManager templateManager, CompoundTag nbt){
//            super(MKUWorldGen.HYBOREAN_ALTER_TYPE, nbt);
//            this.loc = new ResourceLocation(nbt.getString("template"));
//            this.rotation = Rotation.valueOf(nbt.getString("rot"));
//            setManager(templateManager);
//        }
//
//        //this is actually writeAdditional
//        protected void addAdditionalSaveData(CompoundTag tagCompound) {
//            super.addAdditionalSaveData(tagCompound);
//            tagCompound.putString("template", this.loc.toString());
//            tagCompound.putString("rot", this.rotation.name());
//        }
//
//
//        @Override
//        public boolean postProcess(WorldGenLevel seedReader, StructureFeatureManager structureManager, ChunkGenerator chunkGenerator,
//                                      Random random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
//
//            int x = chunkPos.x * 16;
//            int z = chunkPos.z * 16;
//            int y = seedReader.getHeight(Heightmap.Types.WORLD_SURFACE_WG,  x, z) - 1;
//            int y2 = seedReader.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x + 10, z + 10) - 1;
//            int y3 = seedReader.getHeight(Heightmap.Types.WORLD_SURFACE_WG, x + 5, z + 5) - 1;
//            y = Math.min(Math.min(y, y2), y3);
//            this.templatePosition = new BlockPos(x, y, z).offset(StructureUtils.getCorrectionForEvenRotation(rotation));
//            return super.postProcess(seedReader, structureManager, chunkGenerator, random, boundingBox, chunkPos, blockPos);
//        }
//
//        private void setManager(StructureManager manager) {
//            StructureTemplate template = manager.getOrCreate(loc);
//            StructurePlaceSettings placementsettings = (new StructurePlaceSettings())
//                    .setRotation(rotation).setMirror(Mirror.NONE)
//                    .setRotationPivot(new BlockPos(5, 0, 4))
//                    .addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
//            this.setup(template, this.templatePosition, placementsettings);
//        }
//    }
//}
