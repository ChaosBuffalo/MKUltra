package com.chaosbuffalo.mkultra;

import com.chaosbuffalo.mkultra.event.GameplayEventHandler;
import com.chaosbuffalo.mkultra.party.PartyManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Jacob on 3/15/2016.
 */
public class ServerProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent e) {
    }

    @Override
    public void init(FMLInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new PartyManager());
        GameplayEventHandler.neuterEndermen();
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
    }

}
