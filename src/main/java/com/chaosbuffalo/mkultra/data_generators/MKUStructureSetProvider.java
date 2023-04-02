package com.chaosbuffalo.mkultra.data_generators;


import com.chaosbuffalo.mknpc.data.StructureSetProvider;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.HashCache;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.io.IOException;

public class MKUStructureSetProvider extends StructureSetProvider {
    public MKUStructureSetProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    public void run(HashCache cache) throws IOException {
        writeSet(new StructureSetData(new ResourceLocation(MKUltra.MODID, "configured_intro_castle"),
                new RandomSpreadStructurePlacement(2, 1, RandomSpreadType.LINEAR, 34222645))
                .withStructure(new ResourceLocation(MKUltra.MODID, "configured_intro_castle"), 1), cache);
        writeSet(new StructureSetData(new ResourceLocation(MKUltra.MODID, "configured_desert_temple_village"),
                new RandomSpreadStructurePlacement(36, 8, RandomSpreadType.LINEAR, 14444012))
                .withStructure(new ResourceLocation(MKUltra.MODID, "configured_desert_temple_village"), 1), cache);
        writeSet(new StructureSetData(new ResourceLocation(MKUltra.MODID, "configured_necrotide_alter"),
                new RandomSpreadStructurePlacement(50, 24, RandomSpreadType.LINEAR, 132321313))
                .withStructure(new ResourceLocation(MKUltra.MODID, "configured_necrotide_alter"), 1), cache);
    }
}
