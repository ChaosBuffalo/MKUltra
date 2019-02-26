package com.chaosbuffalo.mkultra;

import com.chaosbuffalo.mkultra.client.gui.AbilityBar;
import com.chaosbuffalo.mkultra.client.gui.PartyPanel;
import com.chaosbuffalo.mkultra.client.render.entities.EntityRenderRegister;
import com.chaosbuffalo.mkultra.event.ClientKeyHandler;
import com.chaosbuffalo.mkultra.party.PartyData;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by Jacob on 3/15/2016.
 */
public class ClientProxy extends CommonProxy {

    public static PartyData partyData = new PartyData();

    @Override
    public void preInit(FMLPreInitializationEvent e) {
        ClientKeyHandler.initKeybinds();
        EntityRenderRegister.registerEntityRenderers();
    }

    @Override
    public void init(FMLInitializationEvent e) {
    }

    @Override
    public void postInit(FMLPostInitializationEvent e) {
        MinecraftForge.EVENT_BUS.register(new AbilityBar(Minecraft.getMinecraft()));
        MinecraftForge.EVENT_BUS.register(new PartyPanel(Minecraft.getMinecraft()));
    }

}
