package com.chaosbuffalo.mkultra.client.gui;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.tiles.TileEntityNPCSpawner;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;

public class NPCSpawnerEquipmentGUI extends GuiContainer {
    public static final int WIDTH = 176;
    public static final int HEIGHT = 133;

    private static final ResourceLocation background = new ResourceLocation(MKUltra.MODID,
            "textures/gui/npc_equipment.png");

    public NPCSpawnerEquipmentGUI(TileEntityNPCSpawner tileEntity, Container container) {
        super(container);
        xSize = WIDTH;
        ySize = HEIGHT;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float v, int i, int i1) {
        mc.getTextureManager().bindTexture(background);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}
