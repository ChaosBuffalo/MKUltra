package com.chaosbuffalo.mkultra.client.gui;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.chaosbuffalo.mkultra.network.packets.client.MKSpawnerSetPacket;
import com.chaosbuffalo.mkultra.spawn.MobFaction;
import com.chaosbuffalo.mkultra.tiles.TileEntityMKSpawner;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Function;

public class MKSpawnerGui extends GuiScreen {

    enum Modes {
        DEFAULT,
        SET_FACTION,
        SET_MOB_GROUP,
        SET_TIME
    }
    private static final Function<ResourceLocation, String> getResourceName = ResourceLocation::toString;
    private static final Function<MobFaction.MobGroups, String> getMobGroupName = Enum::name;

    // lists
    private static final int FACTION_LIST = 0;
    private static final int MOB_GROUP_LIST = 1;
    // buttons
    private static final int UP_SECONDS = 0;
    private static final int DOWN_SECONDS = 1;
    private static final int SYNC = 2;
    private static final int CHOOSE_FACTION = 3;
    private static final int CHOOSE_MOB_GROUP = 4;
    private Modes screenMode;

    private ResourceLocation factionName;
    private int spawnTime;
    private MobFaction.MobGroups mobGroup;
    private TileEntityMKSpawner spawner;
    private ButtonList<ResourceLocation> factionList;
    private ButtonList<MobFaction.MobGroups> mobGroupList;

    public MKSpawnerGui(TileEntityMKSpawner spawner){
        this.spawner = spawner;
        this.spawnTime = spawner.getSpawnTimeSeconds();
        this.factionName = spawner.getFactionName();
        this.mobGroup = spawner.getMobGroup();
        screenMode = Modes.DEFAULT;
        factionList = null;
        mobGroupList = null;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        int panelWidth = 256;
        int panelHeight = 256;
        int xPos = width / 2 - panelWidth / 2;
        int yPos = height / 2 - panelHeight / 2;
        ResourceLocation loc = new ResourceLocation(MKUltra.MODID, "textures/gui/full_background.png");
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(loc);
        GL11.glDisable(GL11.GL_LIGHTING);
        drawModalRectWithCustomSizedTexture(xPos, yPos, 0, 0, panelWidth, panelHeight,
                256, 256);
        int titleHeight = 22;
        int buttonHeight = 20;
        this.fontRenderer.drawString("Configure Spawner:", xPos + 15, yPos + 4, 8129636);
        this.fontRenderer.drawString(String.format("Current Faction: %s", factionName),
                xPos + 15, yPos + 4 + titleHeight, 8129636);
        this.fontRenderer.drawString(String.format("Mob Group: %s", mobGroup.name()),
                xPos + 15, yPos + 4 + (titleHeight * 2), 8129636);
        this.fontRenderer.drawString(String.format("Spawn Time: %d Seconds", spawnTime),
                xPos + 15, yPos + 4 + (titleHeight * 3), 8129636);
        GuiButton upButton = new GuiButton(UP_SECONDS, xPos + panelWidth - 64,
                yPos + (titleHeight * 3), 16, buttonHeight, "+");
        GuiButton downButton = new GuiButton(DOWN_SECONDS, xPos + panelWidth - 40,
                yPos + (titleHeight * 3), 16, buttonHeight, "-");
        GuiButton syncButton = new GuiButton(SYNC, xPos + 10, yPos + 4 + (titleHeight * 4),
                64, buttonHeight, "Save");
        buttonList.clear();
        buttonList.add(upButton);
        buttonList.add(downButton);
        buttonList.add(syncButton);
        upButton.drawButton(mc, mouseX, mouseY, partialTicks);
        downButton.drawButton(mc, mouseX, mouseY, partialTicks);
        syncButton.drawButton(mc, mouseX, mouseY, partialTicks);
        switch (screenMode){
            case SET_FACTION:
                if (factionList == null){
                    ArrayList<ResourceLocation> factions = new ArrayList<>(MKURegistry.REGISTRY_MOB_FACTIONS.getKeys());
                    factionList = new ButtonList<>(
                            factions, this::handleFactionListButtonClicked, FACTION_LIST, getResourceName, mc, 200,
                            140, yPos + 4 + (titleHeight * 6), panelHeight, 22, xPos + 28);
                } else {
                    factionList.drawScreen(mouseX, mouseY, partialTicks);
                }
                break;
            case SET_MOB_GROUP:
                MobFaction currentFaction = MKURegistry.getFaction(factionName);
                if (mobGroupList == null){
                    ArrayList<MobFaction.MobGroups> groups = new ArrayList<>();
                    for (MobFaction.MobGroups group : MobFaction.MobGroups.values()){
                        if (group != MobFaction.MobGroups.INVALID && currentFaction != null && !currentFaction.isSpawnListEmpty(group)){
                            groups.add(group);
                        }
                    }
                    mobGroupList = new ButtonList<>(
                            groups, this::handleMobGroupButton, MOB_GROUP_LIST, getMobGroupName, mc, 200,
                            140, yPos + 4 + (titleHeight * 6), panelHeight,
                            22, xPos + 28);
                } else {
                    mobGroupList.drawScreen(mouseX, mouseY, partialTicks);
                }
                break;
            case DEFAULT:
            default:
                GuiButton chooseFaction = new GuiButton(CHOOSE_FACTION, xPos + 10 + 68, yPos + 4 + (titleHeight * 4),
                        75, 20, "Set Faction");
                buttonList.add(chooseFaction);
                chooseFaction.drawButton(mc, mouseX, mouseY, partialTicks);
                GuiButton chooseMobGroup = new GuiButton(CHOOSE_MOB_GROUP, xPos + 10 + 143, yPos + 4 + (titleHeight * 4),
                        75, 20, "Set Group");
                buttonList.add(chooseMobGroup);
                chooseMobGroup.drawButton(mc, mouseX, mouseY, partialTicks);
                break;
        }

    }

    public void handleMobGroupButton(MobFaction.MobGroups group, int list){
        switch (list){
            case MOB_GROUP_LIST:
                mobGroup = group;
                screenMode = Modes.DEFAULT;
            default:
                break;
        }
    }

    public void handleFactionListButtonClicked(ResourceLocation result, int list) {
        switch (list){
            case FACTION_LIST:
                factionName = result;
                screenMode = Modes.DEFAULT;
                mobGroupList = null;
            default:
                break;
        }
    }

    @Override
    public void handleMouseInput() throws IOException
    {
        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;

        super.handleMouseInput();
        if (screenMode == Modes.SET_FACTION && this.factionList != null){
            this.factionList.handleMouseInput();
        }
        if (screenMode == Modes.SET_MOB_GROUP && this.mobGroupList != null){
            this.mobGroupList.handleMouseInput();
        }

    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (screenMode == Modes.SET_FACTION && this.factionList != null){
            this.factionList.mouseClicked(mouseX, mouseY, mouseButton);
        }
        if (screenMode == Modes.SET_MOB_GROUP && this.mobGroupList != null){
            this.mobGroupList.mouseClicked(mouseX, mouseY, mouseButton);
        }
    }

    /**
     * Called when a mouse button is released.
     */
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);
        if (screenMode == Modes.SET_FACTION && this.factionList != null){
            this.factionList.mouseReleased(mouseX, mouseY, state);
        }
        if (screenMode == Modes.SET_MOB_GROUP && this.mobGroupList != null){
            this.mobGroupList.mouseReleased(mouseX, mouseY, state);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id){
            case UP_SECONDS:
                if (isShiftKeyDown()){
                    spawnTime += 30;
                } else {
                    spawnTime++;
                }
                break;
            case DOWN_SECONDS:
                if (isShiftKeyDown()){
                    spawnTime -= 30;
                } else {
                    spawnTime--;
                }
                spawnTime = Math.max(0, spawnTime);
                break;
            case SYNC:
                MKUltra.packetHandler.sendToServer(new MKSpawnerSetPacket(factionName, mobGroup.ordinal(),
                        spawnTime, spawner.getPos().getX(), spawner.getPos().getY(), spawner.getPos().getZ()));
                this.mc.displayGuiScreen(null);
                if (this.mc.currentScreen == null)
                    this.mc.setIngameFocus();
                break;
            case CHOOSE_FACTION:
                screenMode = Modes.SET_FACTION;
                break;
            case CHOOSE_MOB_GROUP:
                screenMode = Modes.SET_MOB_GROUP;
                break;
            default:
                break;
        }
    }

}
