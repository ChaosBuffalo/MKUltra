package com.chaosbuffalo.mkultra.client.gui;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.packets.client.MKSpawnerSetPacket;
import com.chaosbuffalo.mkultra.spawn.MobFaction;
import com.chaosbuffalo.mkultra.tiles.TileEntityMKSpawner;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class MKSpawnerGui extends GuiScreen {

    enum Modes {
        DEFAULT,
        SET_FACTION,
        SET_MOB_GROUP,
        SET_TIME
    }

    private static final int UP_SECONDS = 0;
    private static final int DOWN_SECONDS = 1;
    private static final int SYNC = 2;

    private ResourceLocation factionName;
    private int spawnTime;
    private MobFaction.MobGroups mobGroup;
    TileEntityMKSpawner spawner;

    public MKSpawnerGui(TileEntityMKSpawner spawner){
        this.spawner = spawner;
        this.spawnTime = spawner.getSpawnTimeSeconds();
        this.factionName = spawner.getFactionName();
        this.mobGroup = spawner.getMobGroup();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        int panelWidth = 256;
        int panelHeight = 256;
        int chooseButtonHeight = 23;
        int xPos = width / 2 - panelWidth / 2;
        int yPos = height / 2 - panelHeight / 2;
        ResourceLocation loc = new ResourceLocation(MKUltra.MODID, "textures/gui/full_background.png");
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(loc);
        GL11.glDisable(GL11.GL_LIGHTING);
        drawModalRectWithCustomSizedTexture(xPos, yPos, 0, 0, panelWidth, panelHeight,
                256, 256);
        int titleHeight = 16;
        this.fontRenderer.drawString("Configure Spawner:", xPos + 15, yPos + 4, 8129636);
        this.fontRenderer.drawString(String.format("Current Faction: %s", factionName),
                xPos + 15, yPos + 4 + titleHeight, 8129636);
        this.fontRenderer.drawString(String.format("Mob Group: %s", mobGroup.name()),
                xPos + 15, yPos + 4 + (titleHeight * 2), 8129636);
        this.fontRenderer.drawString(String.format("Spawn Time: %d Seconds", spawnTime),
                xPos + 15, yPos + 4 + (titleHeight * 3), 8129636);
        GuiButton upButton = new GuiButton(UP_SECONDS, xPos + panelWidth - 64,
                yPos + 4 + (titleHeight * 3), 16, 18, "+");
        GuiButton downButton = new GuiButton(DOWN_SECONDS, xPos + panelWidth - 40,
                yPos + 4 + (titleHeight * 3), 16, 18, "-");
        GuiButton syncButton = new GuiButton(SYNC, xPos + 15, yPos + 4 + (titleHeight * 4),
                64, 18, "Sync");
        buttonList.clear();
        buttonList.add(upButton);
        buttonList.add(downButton);
        buttonList.add(syncButton);
        upButton.drawButton(mc, mouseX, mouseY, partialTicks);
        downButton.drawButton(mc, mouseX, mouseY, partialTicks);
        syncButton.drawButton(mc, mouseX, mouseY, partialTicks);

    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id){
            case UP_SECONDS:
                spawnTime++;
                Log.info("incrementing spawn time %d", spawnTime);
                break;
            case DOWN_SECONDS:
                spawnTime--;
                break;
            case SYNC:
                MKUltra.packetHandler.sendToServer(new MKSpawnerSetPacket(factionName, mobGroup.ordinal(),
                        spawnTime, spawner.getPos().getX(), spawner.getPos().getY(), spawner.getPos().getZ()));
                this.mc.displayGuiScreen(null);
                if (this.mc.currentScreen == null)
                    this.mc.setIngameFocus();
                break;
            default:
                break;
        }
    }

}
