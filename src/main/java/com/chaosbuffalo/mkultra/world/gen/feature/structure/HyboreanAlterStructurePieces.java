package com.chaosbuffalo.mkultra.world.gen.feature.structure;

import com.chaosbuffalo.mknpc.world.gen.StructureUtils;
import com.chaosbuffalo.mknpc.world.gen.feature.structure.ChunkPosConfig;
import com.chaosbuffalo.mknpc.world.gen.feature.structure.MKStructurePieceArgs;
import com.chaosbuffalo.mknpc.world.gen.feature.structure.MKTemplateStructurePiece;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.MKUWorldGen;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.Random;
import java.util.UUID;

public class HyboreanAlterStructurePieces {

    private static final ResourceLocation PIECE_ONE = new ResourceLocation(MKUltra.MODID, "hyborean_alter");


    public static void getPieces(MKStructurePieceArgs args, NoFeatureConfig config){
        args.componentsOut.add(new HyboreanAlterStructurePieces.Piece(
                args.templateManager,
                PIECE_ONE, args.blockPos, args.rotation,
                args.structure.getRegistryName(), args.structureId));
    }


    public static class Piece extends MKTemplateStructurePiece {
        private final ResourceLocation loc;
        private final Rotation rotation;


        public Piece(TemplateManager templateManager, ResourceLocation loc,
                     BlockPos blockPos, Rotation rotation, ResourceLocation structureName, UUID instanceId) {
            super(MKUWorldGen.HYBOREAN_ALTER_TYPE, 0, structureName, instanceId);
            this.loc = loc;
            this.rotation = rotation;
            this.templatePosition = blockPos;
            setManager(templateManager);
        }

        public Piece(TemplateManager templateManager, CompoundNBT nbt){
            super(MKUWorldGen.HYBOREAN_ALTER_TYPE, nbt);
            this.loc = new ResourceLocation(nbt.getString("template"));
            this.rotation = Rotation.valueOf(nbt.getString("rot"));
            setManager(templateManager);
        }

        //this is actually writeAdditional
        protected void readAdditional(CompoundNBT tagCompound) {
            super.readAdditional(tagCompound);
            tagCompound.putString("template", this.loc.toString());
            tagCompound.putString("rot", this.rotation.name());
        }


        @Override
        public boolean func_230383_a_(ISeedReader seedReader, StructureManager structureManager, ChunkGenerator chunkGenerator,
                                      Random random, MutableBoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {

            int x = chunkPos.x * 16;
            int z = chunkPos.z * 16;
            int y = seedReader.getHeight(Heightmap.Type.WORLD_SURFACE_WG,  x, z) - 1;
            int y2 = seedReader.getHeight(Heightmap.Type.WORLD_SURFACE_WG, x + 10, z + 10) - 1;
            int y3 = seedReader.getHeight(Heightmap.Type.WORLD_SURFACE_WG, x + 5, z + 5) - 1;
            y = Math.min(Math.min(y, y2), y3);
            this.templatePosition = new BlockPos(x, y, z).add(StructureUtils.getCorrectionForEvenRotation(rotation));
            return super.func_230383_a_(seedReader, structureManager, chunkGenerator, random, boundingBox, chunkPos, blockPos);
        }

        private void setManager(TemplateManager manager) {
            Template template = manager.getTemplateDefaulted(loc);
            PlacementSettings placementsettings = (new PlacementSettings())
                    .setRotation(rotation).setMirror(Mirror.NONE)
                    .setCenterOffset(new BlockPos(5, 0, 4))
                    .addProcessor(BlockIgnoreStructureProcessor.STRUCTURE_BLOCK);
            this.setup(template, this.templatePosition, placementsettings);
        }
    }
}
