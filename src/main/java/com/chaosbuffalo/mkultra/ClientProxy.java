package com.chaosbuffalo.mkultra;

import com.chaosbuffalo.mkultra.client.gui.AbilityBar;
import com.chaosbuffalo.mkultra.client.gui.OrbMotherGui;
import com.chaosbuffalo.mkultra.client.gui.PartyPanel;
import com.chaosbuffalo.mkultra.client.gui.PlayerClassScreen;
import com.chaosbuffalo.mkultra.client.render.entities.EntityRenderRegister;
import com.chaosbuffalo.mkultra.core.events.PlayerClassEvent;
import com.chaosbuffalo.mkultra.event.ClientKeyHandler;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.party.PartyData;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Jacob on 3/15/2016.
 */
@Mod.EventBusSubscriber
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

    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void handlePlayerDataUpdate(PlayerClassEvent.Updated event) {
        if (!event.getPlayer().getEntityWorld().isRemote || event.getPlayerData() == null)
            return;

        Minecraft mc = Minecraft.getMinecraft();
        if (mc.currentScreen instanceof PlayerClassScreen) {
            PlayerClassScreen screen = (PlayerClassScreen) mc.currentScreen;
            screen.handlePlayerDataUpdate(event);
        } else if (mc.currentScreen instanceof OrbMotherGui) {
            OrbMotherGui screen = (OrbMotherGui) mc.currentScreen;
            screen.handlePlayerDataUpdate(event);
        }
    }
}
