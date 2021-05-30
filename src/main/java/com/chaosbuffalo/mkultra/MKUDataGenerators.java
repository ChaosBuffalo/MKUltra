package com.chaosbuffalo.mkultra;

import com.chaosbuffalo.mkcore.DataGenerators;
import com.chaosbuffalo.mkultra.data_generators.MKUDialogueProvider;
import com.chaosbuffalo.mkultra.data_generators.MKUNpcProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class MKUDataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();

        if (event.includeServer()) {
            generator.addProvider(new DataGenerators.AbilityDataGenerator(generator, MKUltra.MODID));
            generator.addProvider(new MKUNpcProvider(generator));
            generator.addProvider(new MKUDialogueProvider(generator));
        }
    }
}
