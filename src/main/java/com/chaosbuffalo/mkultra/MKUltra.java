package com.chaosbuffalo.mkultra;


import com.chaosbuffalo.mkultra.extensions.MKUNpcExtensions;
import com.chaosbuffalo.mkultra.init.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(MKUltra.MODID)
public class MKUltra {
    public static final String MODID = "mkultra";
    public static final Logger LOGGER = LogManager.getLogger();

    public MKUltra() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        MKUEntities.register();
        MKUAbilities.register();
        MKUWorldGen.register();
        MKUEntitlements.register(modBus);
        MKUTalents.register(modBus);
        modBus.addListener(this::enqueueIMC);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        MKUNpcExtensions.sendExtension();
    }
}
