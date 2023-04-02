package com.chaosbuffalo.mkultra.init;


import com.chaosbuffalo.mknpc.entity.MKEntity;
import com.chaosbuffalo.mknpc.world.gen.feature.structure.*;
import com.chaosbuffalo.mknpc.world.gen.feature.structure.events.StructureEvent;
import com.chaosbuffalo.mknpc.world.gen.feature.structure.events.event.SpawnNpcDefinitionEvent;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.world.gen.feature.structure.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class MKUWorldGen {

    public static final DeferredRegister<StructureFeature<?>> STRUCTURE_REGISTRY = DeferredRegister.create(
            ForgeRegistries.STRUCTURE_FEATURES, MKUltra.MODID);

    public static final RegistryObject<StructureFeature<JigsawConfiguration>> INTRO_CASTLE = STRUCTURE_REGISTRY.register("intro_castle",
            () -> new IntroCastleJigsawStructure(JigsawConfiguration.CODEC, 0, true, true, false));

    public static final RegistryObject<StructureFeature<JigsawConfiguration>> DESERT_TEMPLE_VILLAGE_STRUCTURE = STRUCTURE_REGISTRY.register("desert_temple_village",
            () -> new MKJigsawStructure(JigsawConfiguration.CODEC, 0, true, true, (x) -> true, false));

    public static final RegistryObject<StructureFeature<JigsawConfiguration>> NECROTIDE_ALTER = STRUCTURE_REGISTRY.register("necrotide_alter",
            () -> new MKJigsawStructure(JigsawConfiguration.CODEC, 0, true, true, (piece) -> true, false)
                    .addEvent("summon_golem", new SpawnNpcDefinitionEvent(new ResourceLocation(MKUltra.MODID, "necrotide_golem"),
                            "golem_spawn", "golem_look", MKEntity.NonCombatMoveType.STATIONARY)
                            .addNotableDeadCondition(new ResourceLocation(MKUltra.MODID, "skeletal_lock"), true)
                            .addTrigger(StructureEvent.EventTrigger.ON_DEATH)));


    public static void register() {
        STRUCTURE_REGISTRY.register(FMLJavaModLoadingContext.get().getModEventBus());
    }


}
