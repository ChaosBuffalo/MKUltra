package com.chaosbuffalo.mkultra.client.gui;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.client.gui.lib.*;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.chaosbuffalo.mkultra.core.PlayerData;
import com.chaosbuffalo.mkultra.core.events.PlayerClassEvent;
import com.chaosbuffalo.mkultra.core.talents.TalentRecord;
import com.chaosbuffalo.mkultra.core.talents.TalentTreeRecord;
import com.chaosbuffalo.mkultra.network.packets.AddTalentRequestPacket;
import com.chaosbuffalo.mkultra.tiles.TileEntityNPCSpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.Arrays;

public class OrbMotherGui extends MKScreen {

    private EntityPlayer player;
    private TileEntityNPCSpawner spawner;
    private ResourceLocation selectedTree;
    private MKScrollView treeView;
    private int PANEL_WIDTH = 320;
    private int PANEL_HEIGHT = 256;

    public OrbMotherGui(TileEntityNPCSpawner spawner, EntityPlayer player) {
        super();
        this.player = player;
        selectedTree = null;
        this.spawner = spawner;
        treeView = null;
    }

    public void handlePlayerDataUpdate(PlayerClassEvent.Updated event) {
        if (!event.isCurrentClass())
            return;
        this.flagNeedSetup();
    }

    @Override
    public void addRestoreStateCallbacks() {
        super.addRestoreStateCallbacks();
        ResourceLocation tree = selectedTree;
        addPostSetupCallback(() -> selectedTree = tree);
        if (treeView != null) {
            int offsetX = treeView.getOffsetX();
            int offsetY = treeView.getOffsetY();
            addPostSetupCallback(() -> {
                treeView.setOffsetX(offsetX);
                treeView.setOffsetY(offsetY);
            });
        }
    }

    @Override
    public void onResize(Minecraft minecraft, int width, int height) {
        super.onResize(minecraft, width, height);
        addPostSetupCallback(() -> {
            if (treeView != null) {
                treeView.centerContentX();
                treeView.setToTop();
            }
        });
    }

    @Override
    public void setupScreen() {
        super.setupScreen();
        int panelWidth = PANEL_WIDTH;
        int panelHeight = PANEL_HEIGHT;
        int xPos = width / 2 - panelWidth / 2;
        int yPos = height / 2 - panelHeight / 2;
        ScaledResolution scaledRes = new ScaledResolution(mc);
        MKWidget selectRoot = new MKWidget(xPos, yPos, panelWidth, panelHeight);
        addState("select", selectRoot);
        MKWidget treeRoot = new MKWidget(xPos, yPos, panelWidth, panelHeight);
        addState("tree", treeRoot);
        int scrollViewSpace = 30;
        MKScrollView scrollView = new MKScrollView(xPos + 5, yPos + 5 + scrollViewSpace,
                panelWidth - 10, panelHeight - 10 - scrollViewSpace, scaledRes.getScaleFactor(),
                true)
                .setScrollMarginY(10);
        scrollView.setDoScrollX(false);
        treeView = scrollView;
        treeRoot.addWidget(scrollView);
        MKButton backButton = new MKButton(xPos + 10, yPos + 10, 30, 20, "Back")
                .setPressedCallback((MKButton button, Integer mouseButton) -> {
                    setState("select");
                    return true;
                });
        treeRoot.addWidget(backButton);
        MKWidget title = new MKText(mc.fontRenderer, "Select A Talent Tree to Edit:")
                .setIsCentered(true)
                .setColor(8129636)
                .setWidth(panelWidth)
                .setX(xPos)
                .setY(yPos + 4);
        selectRoot.addWidget(title);
        MKWidget textLayout = new MKStackLayoutVertical(xPos, title.getY() + title.getHeight(), panelWidth)
                .doSetWidth(true)
                .setMarginTop(8)
                .setMarginBot(8)
                .setPaddingTop(4)
                .setMarginLeft(25)
                .setMarginRight(25)
                .setPaddingBot(4);
        PlayerData data = (PlayerData) MKUPlayerData.get(player);
        if (data != null) {
            String unspentText = String.format("Unspent Points: %d", data.getUnspentTalentPoints());
            MKWidget unspentPoints = new MKText(mc.fontRenderer, unspentText)
                    .setColor(8129636);
            textLayout.addWidget(unspentPoints);
            String totalText = String.format("Total Points: %d", data.getTotalTalentPoints());
            MKWidget totalPoints = new MKText(mc.fontRenderer, totalText)
                    .setColor(8129636);
            textLayout.addWidget(totalPoints);
            String nextPointText = String.format("Next Point Will Cost: %d", data.getTotalTalentPoints());
            MKWidget nextPoint = new MKText(mc.fontRenderer, nextPointText)
                    .setColor(8129636);
            textLayout.addWidget(nextPoint);
            MKWidget buyButton = new MKButton("Buy Talent Point")
                    .setPressedCallback((button, mouseButton) -> {
                        MKUltra.packetHandler.sendToServer(new AddTalentRequestPacket());
                        return true;
                    })
                    .setEnabled(data.canGainTalentPoint())
                    .setSizeHintWidth(0.6f)
                    .setPosHintX(0.2f);
            textLayout.addWidget(buyButton);
            setupTalentTree(scrollView, data, 0, 0);
            scrollView.centerContentX();
            scrollView.setToTop();
            MKWidget unspentPointsTree = new MKText(mc.fontRenderer, unspentText)
                    .setColor(8129636)
                    .setX(backButton.getRight() + 10)
                    .setY(backButton.getTop() + backButton.getHeight() / 2);
            treeRoot.addWidget(unspentPointsTree);
        }
        selectRoot.addWidget(textLayout);
        int ltop = textLayout.getY() + textLayout.getHeight();
        int lwidth = 160;
        int layoutX = xPos + (panelWidth - lwidth) / 2;
        MKScrollView svTreeList = new MKScrollView(layoutX, ltop,
                lwidth, 160, scaledRes.getScaleFactor(), true);
        svTreeList.setScrollMarginX(20).setDoScrollX(false);
        MKStackLayoutVertical layout = new MKStackLayoutVertical(0, 0, lwidth - 40).doSetWidth(true);
        svTreeList.addWidget(layout);
        layout.setPaddingBot(2).setPaddingTop(2).setMarginTop(4);
        ArrayList<ResourceLocation> treeLocs = new ArrayList<>(MKURegistry.REGISTRY_TALENT_TREES.getKeys());
        treeLocs.sort(ResourceLocation::compareTo);
        for (ResourceLocation loc : treeLocs) {
            MKButton locButton = new MKButton(I18n.format(String.format("%s.%s.name",
                    loc.getNamespace(), loc.getPath())));
            locButton.setPressedCallback((MKButton button, Integer mouseButton) -> {
                selectedTree = loc;
                IPlayerData pdata = MKUPlayerData.get(player);
                if (pdata != null) {
                    setupTalentTree(scrollView, pdata, 0, 0);
                    scrollView.centerContentX();
                    scrollView.setToTop();
                }
                setState("tree");
                return true;
            });
            layout.addWidget(locButton);
        }
        svTreeList.centerContentX();
        svTreeList.setToTop();
        selectRoot.addWidget(svTreeList);
        setState("select");
    }


    private boolean pressTalentButton(MKButton button, Integer mouseButton) {
        TalentButton talentButton = (TalentButton) button;
        PlayerData data = (PlayerData) MKUPlayerData.get(player);
        if (data != null) {
            if (mouseButton == UIConstants.MOUSE_BUTTON_RIGHT) {
                data.refundTalentPoint(selectedTree, talentButton.line, talentButton.index);
            } else if (mouseButton == UIConstants.MOUSE_BUTTON_LEFT) {
                data.spendTalentPoint(selectedTree, talentButton.line, talentButton.index);
            }
        }
        return true;
    }

    private MKWidget setupTalentTree(MKScrollView container, IPlayerData data, int xPos, int yPos) {
        container.clearWidgets();
        TalentTreeRecord record = data.getTalentTree(selectedTree);
        int treeRenderingMarginX = 10;
        int treeRenderingPaddingX = 10;
        int talentButtonHeight = TalentButton.HEIGHT;
        int talentButtonWidth = TalentButton.WIDTH;
        int talentButtonYMargin = 6;
        MKWidget widget = new MKWidget(xPos, yPos);
        if (record != null) {

            int count = record.getRecords().size();
            int talentWidth = talentButtonWidth * count + treeRenderingMarginX * 2 + (count - 1) * treeRenderingPaddingX;
            int spacePerColumn = talentWidth / count;
            int columnOffset = (spacePerColumn - talentButtonWidth) / 2;
            int i = 0;
            String[] keys = record.getRecords().keySet().toArray(new String[0]);
            Arrays.sort(keys);
            int largestIndex = 0;
            int columnOffsetTotal = 0;
            for (String name : keys) {
                ArrayList<TalentRecord> talents = record.getRecords().get(name);
                int talentIndex = 0;
                for (TalentRecord talent : talents) {
                    TalentButton button = new TalentButton(talentIndex, name, talent,
                            xPos + spacePerColumn * i + columnOffsetTotal,
                            yPos + talentIndex * talentButtonHeight + talentButtonYMargin
                    );
                    button.setPressedCallback(this::pressTalentButton);
                    widget.addWidget(button);
                    if (talentIndex > largestIndex) {
                        largestIndex = talentIndex;
                    }
                    talentIndex++;
                }
                i++;
                columnOffsetTotal += columnOffset;
            }
            widget.setWidth(talentWidth);
            widget.setHeight((largestIndex + 1) * talentButtonHeight + talentButtonYMargin);
        }
        container.addWidget(widget);
        return widget;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int panelWidth = PANEL_WIDTH;
        int panelHeight = PANEL_HEIGHT;
        int xPos = width / 2 - panelWidth / 2;
        int yPos = height / 2 - panelHeight / 2;

        ResourceLocation loc = new ResourceLocation(MKUltra.MODID, "textures/gui/background_320.png");
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(loc);
        GL11.glDisable(GL11.GL_LIGHTING);
        drawModalRectWithCustomSizedTexture(xPos, yPos,
                0, 0,
                panelWidth, panelHeight,
                512, 512);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
