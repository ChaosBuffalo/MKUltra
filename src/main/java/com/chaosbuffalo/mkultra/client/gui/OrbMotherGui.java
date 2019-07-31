package com.chaosbuffalo.mkultra.client.gui;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.chaosbuffalo.mkultra.core.talents.TalentRecord;
import com.chaosbuffalo.mkultra.core.talents.TalentTreeRecord;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.packets.AddTalentRequestPacket;
import com.chaosbuffalo.mkultra.tiles.TileEntityNPCSpawner;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public class OrbMotherGui extends MKScreen {

    enum Modes {
        SELECT_TREE,
        EDIT_TREE
    }

    private Modes mode;
    private EntityPlayer player;
    private TileEntityNPCSpawner spawner;
    private ButtonList<ResourceLocation> treeList;
    private static final Function<ResourceLocation, String> getResourceName = ResourceLocation::toString;
    private static final int TREE_LIST = 0;
    // buttons
    private static final int BUY_POINT = 0;
    private static final int BACK = 1;
    private static final int TALENT_BUTTON = 2;
    private MKStackLayoutVertical layout;
    private ResourceLocation selectedTree;

    public OrbMotherGui(TileEntityNPCSpawner spawner, EntityPlayer player) {
        super();
        this.player = player;
        this.mode = Modes.SELECT_TREE;
        selectedTree = null;
        treeList = null;
        this.spawner = spawner;
    }

    @Override
    public void setupScreen() {
        super.setupScreen();
        int panelWidth = 256;
        int panelHeight = 256;
        int xPos = width / 2 - panelWidth / 2;
        int yPos = height / 2 - panelHeight / 2;
        int titleHeight = 13;
        int ltop = yPos + 4 + (titleHeight + 68);
        int lwidth = 100;
        int layoutX = xPos + (panelWidth - lwidth) / 2;
        MKStackLayoutVertical layout = new MKStackLayoutVertical(layoutX, ltop, lwidth, 4, 4);
        ArrayList<ResourceLocation> treeLocs = new ArrayList<>(MKURegistry.REGISTRY_TALENT_TREES.getKeys());
        for (ResourceLocation loc : treeLocs){
            MKButton locButton = new MKButton(loc.toString(), lwidth, 20);
            locButton.setPressedCallback((MKButton button)-> {
                Log.info("Tree list clicked %s", loc.toString());
                selectedTree = loc;
                mode = Modes.EDIT_TREE;
                layout.visible = false;
                return true;
            });
            layout.addWidget(locButton);
        }
        this.layout = layout;
        addWidget(layout);

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int width = this.width;
        int height = this.height;
        int panelWidth = 256;
        int panelHeight = 256;
        int xPos = width / 2 - panelWidth / 2;
        int yPos = height / 2 - panelHeight / 2;
        int buttonHeight = 20;
        int treeRenderingMarginX = 10;
        int talentButtonHeight = 30;
        int talentButtonWidth = 24;
        int talentButtonYMargin = 6;
        int talentWidth = 256 - treeRenderingMarginX * 2;
        ResourceLocation loc = new ResourceLocation(MKUltra.MODID, "textures/gui/full_background.png");
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(loc);
        GL11.glDisable(GL11.GL_LIGHTING);
        drawModalRectWithCustomSizedTexture(xPos, yPos, 0, 0, panelWidth, panelHeight,
                256, 256);
        int titleHeight = 13;
        this.fontRenderer.drawString("Select A Talent Tree to Edit:", xPos + 15, yPos + 4, 8129636);
        IPlayerData data = MKUPlayerData.get(player);
        if (data == null){
            return;
        }
        buttonList.clear();
        String unspentText = String.format("Unspent Points: %d", data.getUnspentTalentPoints());
        this.fontRenderer.drawString(unspentText, xPos + 15, yPos + 20, 8129636);
        String totalText = String.format("Total Points: %d", data.getTotalTalentPoints());
        this.fontRenderer.drawString(totalText, xPos + 15, yPos + 36, 8129636);
        switch(mode){
            case SELECT_TREE:
                String nextPoint = String.format("Next Point Will Cost: %d", data.getTotalTalentPoints());
                int xPosBuy = this.fontRenderer.drawString(nextPoint, xPos + 15, yPos + 52, 8129636);
                GuiButton buyButton = new GuiButton(BUY_POINT, xPosBuy + 6,
                        yPos + 50, 24, buttonHeight, "Buy");
                buttonList.add(buyButton);
                buyButton.drawButton(mc, mouseX, mouseY, partialTicks);
//                if (treeList == null){
//                    ArrayList<ResourceLocation> treeLocs = new ArrayList<>(MKURegistry.REGISTRY_TALENT_TREES.getKeys());
//                    int ltop = yPos + 4 + (titleHeight + 68);
//                    int lheight = 140;
//                    int lbot = ltop + lheight;
//                    int lwidth = 100;
//                    treeList = new ButtonList<>(
//                            treeLocs, this::handleTreeListButtonClicked, TREE_LIST, getResourceName, mc, lwidth,
//                            ltop, ltop, lbot, 22, xPos + (panelWidth - lwidth) / 2);
//                } else {
//                    treeList.drawScreen(mouseX, mouseY, partialTicks);
//                }
                break;
            case EDIT_TREE:
                GuiButton backButton = new GuiButton(BACK, xPos + 15,
                        yPos + 50, 30, buttonHeight, "Back");
                buttonList.add(backButton);
                backButton.drawButton(mc, mouseX, mouseY, partialTicks);
                TalentTreeRecord record = data.getTalentTree(selectedTree);
                if (record != null){
                    int count = record.records.size();
                    int spacePerColumn = talentWidth / count;
                    int columnOffset = (spacePerColumn - talentButtonWidth) / 2;
                    int i = 0;
                    String[] keys = record.records.keySet().toArray(new String[0]);
                    Arrays.sort(keys);
                    for (String name : keys){
                        ArrayList<TalentRecord> talents = record.records.get(name);
                        int talentIndex = 0;
                        for (TalentRecord talent : talents){
                            TalentButton button = new TalentButton(talentIndex, name, TALENT_BUTTON, talent,
                                    xPos + spacePerColumn * i + columnOffset,
                                    yPos + 64 + talentIndex * talentButtonHeight + talentButtonYMargin
                                    );
                            buttonList.add(button);
                            button.drawButton(mc, mouseX, mouseY, partialTicks);
                            talentIndex++;
                        }
                        i++;
                    }

                }
                break;
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

//    public void handleTreeListButtonClicked(ResourceLocation result, int list) {
//        switch (list){
//            case TREE_LIST:
//                Log.info("Tree list clicked %s", result.toString());
//                selectedTree = result;
//                mode = Modes.EDIT_TREE;
//            default:
//                break;
//        }
//    }

//    @Override
//    public void handleMouseInput() throws IOException
//    {
//        int mouseX = Mouse.getEventX() * this.width / this.mc.displayWidth;
//        int mouseY = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
//
//        super.handleMouseInput();
//        if (mode == Modes.SELECT_TREE && this.treeList != null){
//            this.treeList.handleMouseInput();
//        }
//    }

//    @Override
//    protected void mouseClickMove(int mouseX, int mouseY, int mouseButton, long dt) {
//        super.mouseClickMove(mouseX, mouseY, mouseButton, dt);
//    }
//
//    @Override
//    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
//    {
//        super.mouseClicked(mouseX, mouseY, mouseButton);
//        if (mode == Modes.SELECT_TREE && this.treeList != null){
//            this.treeList.mouseClicked(mouseX, mouseY, mouseButton);
//        }
//    }

    /**
     * Called when a mouse button is released.
     */
//    @Override
//    protected void mouseReleased(int mouseX, int mouseY, int state)
//    {
//        super.mouseReleased(mouseX, mouseY, state);
//        if (mode == Modes.SELECT_TREE && this.treeList != null){
//            this.treeList.mouseReleased(mouseX, mouseY, state);
//        }
//
//    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id){
            case BUY_POINT:
                MKUltra.packetHandler.sendToServer(new AddTalentRequestPacket());
                break;
            case BACK:
                this.mode = Modes.SELECT_TREE;
                layout.visible = true;
                break;
        }
    }
}
