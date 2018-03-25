package com.chaosbuffalo.mkultra.client.gui;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerDataProvider;
import com.chaosbuffalo.mkultra.network.packets.client.LevelUpRequestPacket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Jacob on 3/22/2016.
 */
public class XpTableScreen extends GuiScreen {


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        IPlayerData pData = PlayerDataProvider.get(this.mc.player);
        if (pData == null)
            return;

        this.drawDefaultBackground();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        int panelWidth = 119;
        int panelHeight = 165;
        int xPos = width / 2 - panelWidth / 2;
        int yPos = height / 2 - panelHeight / 2;
        ResourceLocation loc = new ResourceLocation(MKUltra.MODID, "textures/gui/xp_table_background.png");
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(loc);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.drawTexturedModalRect(xPos, yPos, 0, 0, panelWidth, panelHeight);
        float scaleFactor = 1.5f;
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0f / scaleFactor, 1.0f / scaleFactor, 1.0f / scaleFactor);
        String text = "Give your Brouzoufs to Solarius." +
                "  Receive his blessings.";
        this.fontRenderer.drawSplitString(
                text,
                (int)((xPos + 20) * scaleFactor),
                (int)((yPos + 110) * scaleFactor),
                (int) (90 * scaleFactor), 0);
        GlStateManager.popMatrix();
        int buttonStartY = yPos + 110 + 25;
        int buttonStartX = xPos + 26;
        this.buttonList.clear();
        GuiButton button = new GuiButton(0, buttonStartX,
                buttonStartY, 69, 20, "Sacrifice");
        button.enabled = pData.canLevelUp();
        this.buttonList.add(button);
        button.drawButton(this.mc, mouseX, mouseY, 0f);
        scaleFactor = 0.5f;
        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0f / scaleFactor, 1.0f / scaleFactor, 1.0f / scaleFactor);
        int xCoord = (int) ((xPos + 50) * scaleFactor);
        int yCoord = (int) ((yPos + 70) * scaleFactor);
        String levelText;
        if (pData.getLevel() < 10){
            levelText = Integer.toString(pData.getLevel() + 1);
        } else {
            levelText = "10";
        }
        if (pData.getLevel() <= 8){
            levelText = " " + levelText;
        }
        this.fontRenderer.drawString(levelText, xCoord, yCoord, 38600);
        GlStateManager.popMatrix();
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        MKUltra.packetHandler.sendToServer(new LevelUpRequestPacket());
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }


}

