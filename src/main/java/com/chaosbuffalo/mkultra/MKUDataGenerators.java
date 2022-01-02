package com.chaosbuffalo.mkultra;

import com.chaosbuffalo.mkcore.DataGenerators;
import com.chaosbuffalo.mkultra.data_generators.*;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MKUDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper helper = event.getExistingFileHelper();
        if (event.includeServer()) {
            generator.addProvider(new DataGenerators.AbilityDataGenerator(generator, MKUltra.MODID));
            generator.addProvider(new MKUNpcProvider(generator));
            generator.addProvider(new MKUDialogueProvider(generator));
            generator.addProvider(new MKUTalentTreeProvider(generator));
            generator.addProvider(new MKULootTierProvider(generator));
            generator.addProvider(new MKUFactionProvider(generator));

        }
        if (event.includeClient()){
            generator.addProvider(new MKUItemModelProvider(generator, helper));
        }
    }
}
