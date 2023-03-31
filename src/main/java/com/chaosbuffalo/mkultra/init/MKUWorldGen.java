package com.chaosbuffalo.mkultra.init;


import com.chaosbuffalo.mknpc.entity.MKEntity;
import com.chaosbuffalo.mknpc.world.gen.feature.structure.*;
import com.chaosbuffalo.mknpc.world.gen.feature.structure.events.StructureEvent;
import com.chaosbuffalo.mknpc.world.gen.feature.structure.events.event.SpawnNpcDefinitionEvent;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.world.gen.feature.structure.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.StructureFeatureConfiguration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fmlserverevents.FMLServerAboutToStartEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MKUWorldGen {
//    public static MKJigsawStructure CRYPT_STRUCTURE;
//    public static ResourceLocation CRYPT_NAME = new ResourceLocation(MKUltra.MODID, "crypt");
//    private static StructureFeature<?, ?> CRYPT_FEATURE;

//    public static final ResourceLocation ALPHA_GREEN_LADY_NAME = new ResourceLocation(MKUltra.MODID, "alpha_green_lady");
//    public static IStructurePieceType ALPHA_GREEN_LADY_TYPE;
//    private static StructureFeature<?, ?> ALPHA_GREEN_LADY_FEATURE;
//    public static AlphaGreenLadyStructure ALPHA_GREEN_LADY_STRUCTURE;

//    public static final ResourceLocation HYBOREAN_ALTER_NAME = new ResourceLocation(MKUltra.MODID, "hyborean_alter");
//    public static StructurePieceType HYBOREAN_ALTER_TYPE;
//    private static ConfiguredStructureFeature<?, ?> HYBOREAN_ALTER_FEATURE;
//    private static HyboreanAlterStructure HYBOREAN_ALTER_STRUCTURE;

    public static MKJigsawStructure INTRO_CASTLE;
    public static ResourceLocation INTRO_CASTLE_NAME = new ResourceLocation(MKUltra.MODID, "intro_castle");
    private static ConfiguredStructureFeature<?, ?> INTRO_CASTLE_FEATURE;

    public static MKJigsawStructure DESERT_TEMPLE_VILLAGE_STRUCTURE;
    public static final ResourceLocation DESERT_TEMPLE_VILLAGE_NAME = new ResourceLocation(MKUltra.MODID, "desert_temple_village");
    private static ConfiguredStructureFeature<?, ?> DESERT_TEMPLE_VILLAGE_FEATURE;

    public static MKJigsawStructure NECROTIDE_ALTER;
    public static final ResourceLocation NECROTIDE_ALTER_NAME = new ResourceLocation(MKUltra.MODID, "necrotide_alter");
    private static ConfiguredStructureFeature<?, ?> NECROTIDE_ALTER_FEATURE;


    public static void registerStructurePieces(){
//        ALPHA_GREEN_LADY_TYPE = Registry.register(Registry.STRUCTURE_PIECE, ALPHA_GREEN_LADY_NAME.toString(),
//                AlphaGreenLadyStructurePieces.Piece::new);
//        HYBOREAN_ALTER_TYPE = Registry.register(Registry.STRUCTURE_PIECE, HYBOREAN_ALTER_NAME.toString(),
//                HyboreanAlterStructurePieces.Piece::new);

    }

    @SubscribeEvent
    public static void registerStructure(RegistryEvent.Register<StructureFeature<?>> evt){

//        ALPHA_GREEN_LADY_STRUCTURE = new AlphaGreenLadyStructure(ChunkPosConfig.CODEC);
//        ALPHA_GREEN_LADY_STRUCTURE.setRegistryName(ALPHA_GREEN_LADY_NAME);
//        ALPHA_GREEN_LADY_FEATURE = ALPHA_GREEN_LADY_STRUCTURE.withConfiguration(new ChunkPosConfig(0, 0));
//        Structure.NAME_STRUCTURE_BIMAP.put(ALPHA_GREEN_LADY_NAME.toString(), ALPHA_GREEN_LADY_STRUCTURE);
//        Structure.STRUCTURE_DECORATION_STAGE_MAP.put(ALPHA_GREEN_LADY_STRUCTURE, GenerationStage.Decoration.SURFACE_STRUCTURES);
//        evt.getRegistry().register(ALPHA_GREEN_LADY_STRUCTURE);

//        HYBOREAN_ALTER_STRUCTURE = new HyboreanAlterStructure(NoFeatureConfig.CODEC);
//        HYBOREAN_ALTER_STRUCTURE.setRegistryName(HYBOREAN_ALTER_NAME);
//        HYBOREAN_ALTER_FEATURE = HYBOREAN_ALTER_STRUCTURE.withConfiguration(new NoFeatureConfig());
//        Structure.NAME_STRUCTURE_BIMAP.put(HYBOREAN_ALTER_NAME.toString(), HYBOREAN_ALTER_STRUCTURE);
//
//        Structure.STRUCTURE_DECORATION_STAGE_MAP.put(HYBOREAN_ALTER_STRUCTURE, GenerationStage.Decoration.SURFACE_STRUCTURES);
//        evt.getRegistry().register(HYBOREAN_ALTER_STRUCTURE);

//        CRYPT_STRUCTURE = new MKJigsawStructure(VillageConfig.CODEC, -19, true, true, false);
//        CRYPT_STRUCTURE.setRegistryName(CRYPT_NAME);
//        Structure.NAME_STRUCTURE_BIMAP.put(CRYPT_NAME.toString(), CRYPT_STRUCTURE);
//        Structure.STRUCTURE_DECORATION_STAGE_MAP.put(CRYPT_STRUCTURE, GenerationStage.Decoration.SURFACE_STRUCTURES);
//        CRYPT_FEATURE = CRYPT_STRUCTURE.withConfiguration(new VillageConfig(
//                () -> CryptStructurePools.CRYPT_BASE, CryptStructurePools.GEN_DEPTH));
//        evt.getRegistry().register(CRYPT_STRUCTURE);

        INTRO_CASTLE = new IntroCastleJigsawStructure(JigsawConfiguration.CODEC, -19, true, true, false);
        INTRO_CASTLE.setRegistryName(INTRO_CASTLE_NAME);
        StructureFeature.STRUCTURES_REGISTRY.put(INTRO_CASTLE_NAME.toString(), INTRO_CASTLE);
        StructureFeature.STEP.put(INTRO_CASTLE, GenerationStep.Decoration.SURFACE_STRUCTURES);
        INTRO_CASTLE_FEATURE = INTRO_CASTLE.configured(new JigsawConfiguration(
                () -> IntroCastlePools.INTRO_CASTLE_BASE, IntroCastlePools.GEN_DEPTH));
        evt.getRegistry().register(INTRO_CASTLE);

        DESERT_TEMPLE_VILLAGE_STRUCTURE = new MKJigsawStructure(JigsawConfiguration.CODEC, 0, true, true, false);
        DESERT_TEMPLE_VILLAGE_STRUCTURE.setRegistryName(DESERT_TEMPLE_VILLAGE_NAME);
        StructureFeature.STRUCTURES_REGISTRY.put(DESERT_TEMPLE_VILLAGE_NAME.toString(), DESERT_TEMPLE_VILLAGE_STRUCTURE);
        StructureFeature.STEP.put(DESERT_TEMPLE_VILLAGE_STRUCTURE, GenerationStep.Decoration.SURFACE_STRUCTURES);
        DESERT_TEMPLE_VILLAGE_FEATURE = DESERT_TEMPLE_VILLAGE_STRUCTURE.configured(new JigsawConfiguration(() -> DesertTempleVillagePools.DESERT_TEMPLE_VILLAGE_BASE, DesertTempleVillagePools.GEN_DEPTH));
        evt.getRegistry().register(DESERT_TEMPLE_VILLAGE_STRUCTURE);

        NECROTIDE_ALTER = new MKJigsawStructure(JigsawConfiguration.CODEC, 0, true, true, false)
                .addEvent("summon_golem", new SpawnNpcDefinitionEvent(new ResourceLocation(MKUltra.MODID, "necrotide_golem"),
                        "golem_spawn", "golem_look", MKEntity.NonCombatMoveType.STATIONARY)
                        .addNotableDeadCondition(new ResourceLocation(MKUltra.MODID, "skeletal_lock"), true)
                        .addTrigger(StructureEvent.EventTrigger.ON_DEATH));
        NECROTIDE_ALTER.setRegistryName(NECROTIDE_ALTER_NAME);
        StructureFeature.STRUCTURES_REGISTRY.put(NECROTIDE_ALTER_NAME.toString(), NECROTIDE_ALTER);
        StructureFeature.STEP.put(NECROTIDE_ALTER, GenerationStep.Decoration.SURFACE_STRUCTURES);
        NECROTIDE_ALTER_FEATURE = NECROTIDE_ALTER.configured(new JigsawConfiguration(
                () -> NecrotideAlterPools.BASE, NecrotideAlterPools.GEN_DEPTH));
        evt.getRegistry().register(NECROTIDE_ALTER);

    }

    public static void worldSetup(FMLServerAboutToStartEvent event){
        event.getServer().registryAccess().registryOrThrow(Registry.NOISE_GENERATOR_SETTINGS_REGISTRY).getOptional(NoiseGeneratorSettings.OVERWORLD)
                .ifPresent(x ->  {
                    x.structureSettings().structureConfig().put(INTRO_CASTLE, new StructureFeatureConfiguration(2, 1, 34222645));
                    x.structureSettings().structureConfig().put(DESERT_TEMPLE_VILLAGE_STRUCTURE, new StructureFeatureConfiguration(36, 8, 14444012));
                    x.structureSettings().structureConfig().put(NECROTIDE_ALTER, new StructureFeatureConfiguration(50, 24, 132321313));
                });
//        event.getServer().getDynamicRegistries().getRegistry(Registry.NOISE_SETTINGS_KEY).forEach(dimensionSettings -> {
////            dimensionSettings.getStructures().structureConfig().put(ALPHA_GREEN_LADY_STRUCTURE,
////                    new StructureSeparationSettings(2, 1, 34222645));
////            dimensionSettings.getStructures().structureConfig().put(CRYPT_STRUCTURE,
////                    new StructureSeparationSettings(20, 10, 32400244));
////            dimensionSettings.getStructures().structureConfig().put(HYBOREAN_ALTER_STRUCTURE,
////                    new StructureSeparationSettings(15, 5, 34244645));
//        });
    }

    public static void biomeSetup(BiomeLoadingEvent event){

//        event.getGeneration().withStructure(CRYPT_FEATURE);
//        event.getGeneration().withStructure(ALPHA_GREEN_LADY_FEATURE);
//        event.getGeneration().withStructure(HYBOREAN_ALTER_FEATURE);
        event.getGeneration().addStructureStart(INTRO_CASTLE_FEATURE);
        if (event.getCategory() == Biome.BiomeCategory.DESERT) {
            event.getGeneration().addStructureStart(DESERT_TEMPLE_VILLAGE_FEATURE);
            event.getGeneration().addStructureStart(NECROTIDE_ALTER_FEATURE);
        }

    }
}
