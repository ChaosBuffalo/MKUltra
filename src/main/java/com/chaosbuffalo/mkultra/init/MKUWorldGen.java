package com.chaosbuffalo.mkultra.init;


import com.chaosbuffalo.mknpc.world.gen.feature.structure.*;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.world.gen.feature.structure.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.DimensionSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MKUWorldGen {
    public static MKJigsawStructure CRYPT_STRUCTURE;
    public static ResourceLocation CRYPT_NAME = new ResourceLocation(MKUltra.MODID, "crypt");
    private static StructureFeature<?, ?> CRYPT_FEATURE;

//    public static final ResourceLocation ALPHA_GREEN_LADY_NAME = new ResourceLocation(MKUltra.MODID, "alpha_green_lady");
//    public static IStructurePieceType ALPHA_GREEN_LADY_TYPE;
//    private static StructureFeature<?, ?> ALPHA_GREEN_LADY_FEATURE;
//    public static AlphaGreenLadyStructure ALPHA_GREEN_LADY_STRUCTURE;

    public static final ResourceLocation HYBOREAN_ALTER_NAME = new ResourceLocation(MKUltra.MODID, "hyborean_alter");
    public static IStructurePieceType HYBOREAN_ALTER_TYPE;
    private static StructureFeature<?, ?> HYBOREAN_ALTER_FEATURE;
    private static HyboreanAlterStructure HYBOREAN_ALTER_STRUCTURE;

    public static MKJigsawStructure INTRO_CASTLE;
    public static ResourceLocation INTRO_CASTLE_NAME = new ResourceLocation(MKUltra.MODID, "intro_castle");
    private static StructureFeature<?, ?> INTRO_CASTLE_FEATURE;


    public static void registerStructurePieces(){
//        ALPHA_GREEN_LADY_TYPE = Registry.register(Registry.STRUCTURE_PIECE, ALPHA_GREEN_LADY_NAME.toString(),
//                AlphaGreenLadyStructurePieces.Piece::new);
//        HYBOREAN_ALTER_TYPE = Registry.register(Registry.STRUCTURE_PIECE, HYBOREAN_ALTER_NAME.toString(),
//                HyboreanAlterStructurePieces.Piece::new);

    }

    @SubscribeEvent
    public static void registerStructure(RegistryEvent.Register<Structure<?>> evt){

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

//        CRYPT_STRUCTURE = new MKJigsawStructure(VillageConfig.field_236533_a_, -19, true, true, false);
//        CRYPT_STRUCTURE.setRegistryName(CRYPT_NAME);
//        Structure.NAME_STRUCTURE_BIMAP.put(CRYPT_NAME.toString(), CRYPT_STRUCTURE);
//        Structure.STRUCTURE_DECORATION_STAGE_MAP.put(CRYPT_STRUCTURE, GenerationStage.Decoration.SURFACE_STRUCTURES);
//        CRYPT_FEATURE = CRYPT_STRUCTURE.withConfiguration(new VillageConfig(
//                () -> CryptStructurePools.CRYPT_BASE, CryptStructurePools.GEN_DEPTH));
//        evt.getRegistry().register(CRYPT_STRUCTURE);

        INTRO_CASTLE = new IntroCastleJigsawStructure(VillageConfig.field_236533_a_, -19, true, true, false);
        INTRO_CASTLE.setRegistryName(INTRO_CASTLE_NAME);
        Structure.NAME_STRUCTURE_BIMAP.put(INTRO_CASTLE_NAME.toString(), INTRO_CASTLE);
        Structure.STRUCTURE_DECORATION_STAGE_MAP.put(INTRO_CASTLE, GenerationStage.Decoration.SURFACE_STRUCTURES);
        INTRO_CASTLE_FEATURE = INTRO_CASTLE.withConfiguration(new VillageConfig(
                () -> IntroCastlePools.INTRO_CASTLE_BASE, IntroCastlePools.GEN_DEPTH));
        evt.getRegistry().register(INTRO_CASTLE);
    }

    public static void worldSetup(FMLServerAboutToStartEvent event){
        event.getServer().getDynamicRegistries().getRegistry(Registry.NOISE_SETTINGS_KEY).getOptionalValue(DimensionSettings.OVERWORLD)
                .ifPresent(x -> x.getStructures().func_236195_a_().put(INTRO_CASTLE,
                        new StructureSeparationSettings(2, 1, 34222645)));
//        event.getServer().getDynamicRegistries().getRegistry(Registry.NOISE_SETTINGS_KEY).forEach(dimensionSettings -> {
////            dimensionSettings.getStructures().func_236195_a_().put(ALPHA_GREEN_LADY_STRUCTURE,
////                    new StructureSeparationSettings(2, 1, 34222645));
////            dimensionSettings.getStructures().func_236195_a_().put(CRYPT_STRUCTURE,
////                    new StructureSeparationSettings(20, 10, 32400244));
////            dimensionSettings.getStructures().func_236195_a_().put(HYBOREAN_ALTER_STRUCTURE,
////                    new StructureSeparationSettings(15, 5, 34244645));
//        });
    }

    public static void biomeSetup(BiomeLoadingEvent event){

//        event.getGeneration().withStructure(CRYPT_FEATURE);
//        event.getGeneration().withStructure(ALPHA_GREEN_LADY_FEATURE);
//        event.getGeneration().withStructure(HYBOREAN_ALTER_FEATURE);
        event.getGeneration().withStructure(INTRO_CASTLE_FEATURE);
    }
}
