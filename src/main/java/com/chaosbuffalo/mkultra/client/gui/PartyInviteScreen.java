package com.chaosbuffalo.mkultra.client.gui;

import com.chaosbuffalo.mkultra.ClientProxy;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.network.packets.PartyInviteResponsePacket;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;


public class PartyInviteScreen extends GuiScreen {

    private static int YES_RESPONSE = 1;
    private static int NO_RESPONSE = 0;


    public PartyInviteScreen() {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        int panelWidth = 195;
        int panelHeight = 74;
        int xPos = width / 2 - panelWidth / 2;
        int yPos = height / 2 - panelHeight / 2;
        ResourceLocation loc = new ResourceLocation(MKUltra.MODID, "textures/gui/demo_background.png");
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(loc);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.drawTexturedModalRect(xPos, yPos, 0, 168, panelWidth, panelHeight);

        String inviteText = String.format("%s is inviting you", ClientProxy.partyData.getInvitingName());
        this.fontRenderer.drawString(inviteText, xPos + 15, yPos + 6, 8129636);
        this.fontRenderer.drawString("to their party. Will you join?", xPos + 15, yPos + 20, 8129636);

        int buttonStartY = yPos + 40;
        int buttonWidth = 45;
        int buttonSpacing = 35;
        int buttonStartX = xPos + buttonSpacing;

        this.buttonList.clear();

        GuiButton yesButton = new GuiButton(YES_RESPONSE, buttonStartX, buttonStartY, buttonWidth, 20, "Yes");
        yesButton.drawButton(this.mc, mouseX, mouseY, partialTicks);
        GuiButton noButton = new GuiButton(NO_RESPONSE, buttonStartX + buttonWidth + buttonSpacing,
                buttonStartY, buttonWidth, 20, "No");
        noButton.drawButton(this.mc, mouseX, mouseY, partialTicks);
        this.buttonList.add(yesButton);
        this.buttonList.add(noButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        boolean doesAccept = button.id == YES_RESPONSE;
        MKUltra.packetHandler.sendToServer(new PartyInviteResponsePacket(
                ClientProxy.partyData.getInvitingUUID(), doesAccept));
        this.mc.displayGuiScreen(null);
        if (this.mc.currentScreen == null)
            this.mc.setIngameFocus();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
