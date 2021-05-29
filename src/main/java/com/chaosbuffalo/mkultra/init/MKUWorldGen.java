package com.chaosbuffalo.mkultra.init;

import com.chaosbuffalo.mknpc.init.MKNpcWorldGen;
import com.chaosbuffalo.mknpc.world.gen.feature.MKSpringFeature;
import com.chaosbuffalo.mknpc.world.gen.feature.structure.*;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.world.gen.feature.structure.CryptStructurePools;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.StructureFeature;
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


    @SubscribeEvent
    public static void registerStructure(RegistryEvent.Register<Structure<?>> evt){

        CRYPT_STRUCTURE = new MKJigsawStructure(VillageConfig.field_236533_a_, -19, true, true);
        CRYPT_STRUCTURE.setRegistryName(CRYPT_NAME);
        Structure.NAME_STRUCTURE_BIMAP.put(CRYPT_NAME.toString(), CRYPT_STRUCTURE);
        Structure.STRUCTURE_DECORATION_STAGE_MAP.put(CRYPT_STRUCTURE, GenerationStage.Decoration.SURFACE_STRUCTURES);
        CRYPT_FEATURE = CRYPT_STRUCTURE.withConfiguration(new VillageConfig(
                () -> CryptStructurePools.CRYPT_BASE, CryptStructurePools.GEN_DEPTH));
        evt.getRegistry().register(CRYPT_STRUCTURE);


    }

    public static void worldSetup(FMLServerAboutToStartEvent event){
        event.getServer().func_244267_aX().getRegistry(Registry.NOISE_SETTINGS_KEY).forEach(dimensionSettings -> {
            dimensionSettings.getStructures().func_236195_a_().put(CRYPT_STRUCTURE,
                    new StructureSeparationSettings(30, 10, 32400244));
        });
    }

    public static void biomeSetup(BiomeLoadingEvent event){
        event.getGeneration().withStructure(CRYPT_FEATURE);
    }
}
