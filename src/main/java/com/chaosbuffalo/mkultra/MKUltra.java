package com.chaosbuffalo.mkultra;


import com.chaosbuffalo.mkultra.extensions.MKUNpcExtensions;
import com.chaosbuffalo.mkultra.init.MKUAbilities;
import com.chaosbuffalo.mkultra.init.MKUEntities;
import com.chaosbuffalo.mkultra.init.MKUWorldGen;
import com.chaosbuffalo.mkultra.world.gen.feature.structure.CryptStructurePools;
import com.chaosbuffalo.mkultra.world.gen.feature.structure.DesertTempleVillagePools;
import com.chaosbuffalo.mkultra.world.gen.feature.structure.IntroCastlePools;
import com.chaosbuffalo.mkultra.world.gen.feature.structure.NecrotideAlterPools;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


@Mod(MKUltra.MODID)
public class MKUltra {
    public static final String MODID = "mkultra";
    public static final Logger LOGGER = LogManager.getLogger();

    public MKUltra(){
        CryptStructurePools.registerPatterns();
        IntroCastlePools.registerPatterns();
        DesertTempleVillagePools.registerPatterns();
        NecrotideAlterPools.registerPatterns();
        MKUWorldGen.registerStructurePieces();
        MKUEntities.register();
        MKUAbilities.register();
        MinecraftForge.EVENT_BUS.addListener(MKUWorldGen::worldSetup);
        MinecraftForge.EVENT_BUS.addListener(MKUWorldGen::biomeSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {
        MKUNpcExtensions.sendExtension();
    }
}
