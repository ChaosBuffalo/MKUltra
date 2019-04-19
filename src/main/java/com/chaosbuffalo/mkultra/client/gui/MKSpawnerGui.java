package com.chaosbuffalo.mkultra.client.gui;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.packets.MKSpawnerSetPacket;
import com.chaosbuffalo.mkultra.spawn.MobFaction;
import com.chaosbuffalo.mkultra.spawn.SpawnList;
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
import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

public class MKSpawnerGui extends GuiScreen {

    enum Modes {
        DEFAULT,
        SET_FACTION,
        SET_MOB_GROUP,
        SET_SPAWN_LIST
    }
    private static final Function<ResourceLocation, String> getResourceName = ResourceLocation::toString;
    private static final Function<String, String> getMobGroupName = (x)-> x;

    // lists
    private static final int FACTION_LIST = 0;
    private static final int MOB_GROUP_LIST = 1;
    private static final int SPAWN_LIST_LIST = 2;
    // buttons
    private static final int UP_SECONDS = 0;
    private static final int DOWN_SECONDS = 1;
    private static final int SYNC = 2;
    private static final int CHOOSE_FACTION = 3;
    private static final int CHOOSE_MOB_GROUP = 4;
    private static final int CHOOSE_SPAWN_LIST = 5;
    private Modes screenMode;

    private ResourceLocation factionName;
    private ResourceLocation spawnListName;
    private int spawnTime;
    private String mobGroup;
    private TileEntityMKSpawner spawner;
    private ButtonList<ResourceLocation> factionList;
    private ButtonList<String> mobGroupList;
    private ButtonList<ResourceLocation> spawnListList;

    public MKSpawnerGui(TileEntityMKSpawner spawner){
        this.spawner = spawner;
        this.spawnTime = spawner.getSpawnTimeSeconds();
        this.factionName = spawner.getFactionName();
        this.mobGroup = spawner.getMobGroup();
        this.spawnListName = spawner.getSpawnListName();
        screenMode = Modes.DEFAULT;
        factionList = null;
        mobGroupList = null;
        spawnListList = null;
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
        int titleHeight = 13;
        int buttonHeight = 20;
        this.fontRenderer.drawString("Configure Spawner:", xPos + 15, yPos + 4, 8129636);
        this.fontRenderer.drawString(String.format("Current Faction: %s", factionName),
                xPos + 15, yPos + 4 + titleHeight, 8129636);
        this.fontRenderer.drawString(String.format("Mob Group: %s", mobGroup),
                xPos + 15, yPos + 4 + (titleHeight * 2), 8129636);
        this.fontRenderer.drawString(String.format("Spawn Time: %d Seconds", spawnTime),
                xPos + 15, yPos + 4 + (titleHeight * 3), 8129636);
        this.fontRenderer.drawString(String.format("Spawn List: %s", spawnListName),
                xPos + 15, yPos + 4 + (titleHeight * 4), 8129636);
        GuiButton upButton = new GuiButton(UP_SECONDS, xPos + panelWidth - 96,
                yPos + (titleHeight * 2) + 6, 16, buttonHeight, "+");
        GuiButton downButton = new GuiButton(DOWN_SECONDS, xPos + panelWidth - 78,
                yPos + (titleHeight * 2) + 6, 16, buttonHeight, "-");
        GuiButton syncButton = new GuiButton(SYNC, xPos + 10, yPos + 4 + (titleHeight * 5),
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
                            140, yPos + 4 + (titleHeight * 7), panelHeight, 22, xPos + 28);
                } else {
                    factionList.drawScreen(mouseX, mouseY, partialTicks);
                }
                break;
            case SET_MOB_GROUP:
                MobFaction currentFaction = MKURegistry.getFaction(factionName);
                if (mobGroupList == null && currentFaction != null){
                    ArrayList<String> groups = new ArrayList<>();
                    Set<String> mobGroups = currentFaction.getMobGroups();
                    if (mobGroups != null){
                        for (String group : mobGroups){
                            if (group != null && !currentFaction.isSpawnListEmpty(group)){
                                Log.info("Adding group %s", group);
                                groups.add(group);
                            }
                        }
                    }

                    mobGroupList = new ButtonList<>(
                            groups, this::handleMobGroupButton, MOB_GROUP_LIST, getMobGroupName, mc, 200,
                            140, yPos + 4 + (titleHeight * 7), panelHeight,
                            22, xPos + 28);
                } else if (mobGroupList != null){
                    mobGroupList.drawScreen(mouseX, mouseY, partialTicks);
                }
                break;
            case SET_SPAWN_LIST:
                currentFaction = MKURegistry.getFaction(factionName);
                String currentGroup = mobGroup;
                if (spawnListList == null && currentFaction != null && currentGroup != null){
                    Collection<SpawnList> spawnLists = currentFaction.getSpawnListsForGroup(currentGroup);
                    ArrayList<ResourceLocation> spawnListNames = new ArrayList<>();
                    if (spawnLists != null){
                        for (SpawnList list : spawnLists){
                            if (list != null){
                                Log.info("Adding spawn list %s", list.getRegistryName().toString());
                                spawnListNames.add(list.getRegistryName());
                            }
                        }
                    }
                    spawnListList = new ButtonList<>(
                            spawnListNames, this::handleSpawnListButtonClicked, SPAWN_LIST_LIST,
                            getResourceName, mc, 200,
                            140, yPos + 4 + (titleHeight * 7), panelHeight,
                            22, xPos + 28);
                } else if (spawnListList != null){
                    spawnListList.drawScreen(mouseX, mouseY, partialTicks);
                }
                break;
            case DEFAULT:
            default:
                GuiButton chooseFaction = new GuiButton(CHOOSE_FACTION, xPos + 10 + 60, yPos + 4 + (titleHeight * 5),
                        60, 20, "Faction");
                buttonList.add(chooseFaction);
                chooseFaction.drawButton(mc, mouseX, mouseY, partialTicks);
                GuiButton chooseMobGroup = new GuiButton(CHOOSE_MOB_GROUP, xPos + 10 + 120, yPos + 4 + (titleHeight * 5),
                        60, 20, "Group");
                buttonList.add(chooseMobGroup);
                chooseMobGroup.drawButton(mc, mouseX, mouseY, partialTicks);
                GuiButton chooseSpawnList = new GuiButton(CHOOSE_SPAWN_LIST, xPos + 10 + 180, yPos + 4 + (titleHeight * 5),
                        60, 20, "Spawn List");
                buttonList.add(chooseSpawnList);
                chooseSpawnList.drawButton(mc, mouseX, mouseY, partialTicks);
                break;
        }

    }

    public void handleMobGroupButton(String group, int list){
        switch (list){
            case MOB_GROUP_LIST:
                mobGroup = group;
                screenMode = Modes.DEFAULT;
                spawnListName = MKURegistry.INVALID_SPAWN_LIST;
                spawnListList = null;
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
                mobGroup = TileEntityMKSpawner.GROUP_NOT_SELECTED;
                spawnListList = null;
                spawnListName = MKURegistry.INVALID_SPAWN_LIST;
            default:
                break;
        }
    }

    public void handleSpawnListButtonClicked(ResourceLocation result, int list){
        switch (list){
            case SPAWN_LIST_LIST:
                spawnListName = result;
                screenMode = Modes.DEFAULT;
                spawnListList = null;
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
        if (screenMode == Modes.SET_SPAWN_LIST && this.spawnListList != null){
            this.spawnListList.handleMouseInput();
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
        if (screenMode == Modes.SET_SPAWN_LIST && this.spawnListList != null){
            this.spawnListList.mouseClicked(mouseX, mouseY, mouseButton);
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
        if (screenMode == Modes.SET_SPAWN_LIST && this.spawnListList != null){
            this.spawnListList.mouseReleased(mouseX, mouseY, state);
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
                MKUltra.packetHandler.sendToServer(new MKSpawnerSetPacket(factionName, mobGroup,
                        spawnTime, spawner.getPos(), spawnListName));
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
            case CHOOSE_SPAWN_LIST:
                screenMode = Modes.SET_SPAWN_LIST;
            default:
                break;
        }
    }

}
