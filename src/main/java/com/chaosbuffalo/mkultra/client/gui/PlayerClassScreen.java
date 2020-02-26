package com.chaosbuffalo.mkultra.client.gui;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.client.gui.lib.*;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.core.events.PlayerClassEvent;
import com.chaosbuffalo.mkultra.network.packets.LevelAbilityPacket;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.*;

/**
 * Created by Jacob on 3/16/2016.
 */
public class PlayerClassScreen extends MKScreen {

    private int PANEL_WIDTH = 320;
    private int PANEL_HEIGHT = 256;
    private int BACKGROUND_WIDTH = 512;
    private int BACKGROUND_HEIGHT = 512;
    private int STAT_PANEL_START_X = 162;
    private int STAT_PANEL_START_Y = 6;
    private int STAT_PANEL_WIDTH = 148;
    private int STAT_PANEL_HEIGHT = 126;
    private int ABILITY_SCROLL_WIDTH = STAT_PANEL_START_X - 8;
    private int X_POS_ICON_SLOT_TEX = 0;
    private int Y_POS_ICON_SLOT_TEX = 259;
    private int ICON_SLOT_WIDTH = 20;
    private int ICON_SLOT_HEIGHT = 20;
    private int DESCRIPTION_WIDTH = ABILITY_SCROLL_WIDTH - 36;
    private MKWidget passivePanel;
    private MKWidget mainRoot;
    private MKWidget ultimatePanel;

    private static final ArrayList<IAttribute> STAT_PANEL_ATTRIBUTES = new ArrayList<>();

    static {
        STAT_PANEL_ATTRIBUTES.add(SharedMonsterAttributes.MAX_HEALTH);
        STAT_PANEL_ATTRIBUTES.add(PlayerAttributes.HEALTH_REGEN);
        STAT_PANEL_ATTRIBUTES.add(PlayerAttributes.MAX_MANA);
        STAT_PANEL_ATTRIBUTES.add(PlayerAttributes.MANA_REGEN);
        STAT_PANEL_ATTRIBUTES.add(SharedMonsterAttributes.ARMOR);
        STAT_PANEL_ATTRIBUTES.add(SharedMonsterAttributes.ARMOR_TOUGHNESS);
        STAT_PANEL_ATTRIBUTES.add(PlayerAttributes.MAGIC_ARMOR);
        STAT_PANEL_ATTRIBUTES.add(SharedMonsterAttributes.ATTACK_DAMAGE);
        STAT_PANEL_ATTRIBUTES.add(PlayerAttributes.MAGIC_ATTACK_DAMAGE);
        STAT_PANEL_ATTRIBUTES.add(PlayerAttributes.HEAL_BONUS);
        STAT_PANEL_ATTRIBUTES.add(SharedMonsterAttributes.ATTACK_SPEED);
        STAT_PANEL_ATTRIBUTES.add(SharedMonsterAttributes.MOVEMENT_SPEED);
        STAT_PANEL_ATTRIBUTES.add(PlayerAttributes.COOLDOWN);
        STAT_PANEL_ATTRIBUTES.add(PlayerAttributes.MELEE_CRIT);
        STAT_PANEL_ATTRIBUTES.add(PlayerAttributes.MELEE_CRITICAL_DAMAGE);
        STAT_PANEL_ATTRIBUTES.add(PlayerAttributes.SPELL_CRIT);
        STAT_PANEL_ATTRIBUTES.add(PlayerAttributes.SPELL_CRITICAL_DAMAGE);
        STAT_PANEL_ATTRIBUTES.add(PlayerAttributes.BUFF_DURATION);
    }

    public PlayerClassScreen() {
        super();
    }

    public void handlePlayerDataUpdate(PlayerClassEvent.Updated event) {
        if (!event.isCurrentClass())
            return;
        IPlayerData pData = event.getPlayerData();
        if (passivePanel != null) {
            mainRoot.removeWidget(passivePanel);
        }
        if (ultimatePanel != null) {
            mainRoot.removeWidget(ultimatePanel);
        }
        int xPos = width / 2 - PANEL_WIDTH / 2;
        int yPos = height / 2 - PANEL_HEIGHT / 2;
        drawSlottedTalentTrays(pData, xPos, yPos);
    }

    private void drawSlottedTalentTrays(IPlayerData pData, int xPos, int yPos) {
        MKWidget passiveTray = drawPassivePanel(pData, xPos + STAT_PANEL_START_X,
                yPos + STAT_PANEL_START_Y + STAT_PANEL_HEIGHT + 4);
        passivePanel = passiveTray;
        int passiveTrayHeight = 0;
        if (passiveTray != null) {
            passiveTrayHeight = passiveTray.getHeight();
            mainRoot.addWidget(passiveTray);
        }

        MKWidget ultTray = drawUltimatePanel(pData, xPos + STAT_PANEL_START_X,
                yPos + STAT_PANEL_START_Y + STAT_PANEL_HEIGHT + 4 + passiveTrayHeight);
        ultimatePanel = ultTray;
        if (ultTray != null) {
            mainRoot.addWidget(ultTray);
        }
    }

    public int drawAbility(PlayerAbility ability, IPlayerData pData, MKWidget abilityList, int abilityHeight,
                           int index, boolean doLevelUp){
        int panelX = 10;
        MKImage iconSlot = new MKImage(
                panelX, abilityHeight,
                ICON_SLOT_WIDTH, ICON_SLOT_HEIGHT,
                GuiTextures.CLASS_BACKGROUND_GRAPHIC)
                .setTexWidth(BACKGROUND_WIDTH).setTexHeight(BACKGROUND_HEIGHT)
                .setTexU(X_POS_ICON_SLOT_TEX).setTexV(Y_POS_ICON_SLOT_TEX);
        abilityList.addWidget(iconSlot);
        MKImage abilityIcon = new MKImage(panelX + 2, abilityHeight + 2, 16, 16,
                ability.getAbilityIcon());
        abilityList.addWidget(abilityIcon);
        int level = pData.getAbilityRank(ability.getAbilityId());
        int displayLevel = Math.max(1, level); // Don't show the stats for a 0-level spell
        String name;
        if (level > 0) {
            name = String.format("%s %d", ability.getAbilityName(), level);
        } else {
            name = ability.getAbilityName();
        }
        MKText nameWid = new MKText(mc.fontRenderer, name);
        nameWid.setX(panelX + 22).setY(abilityHeight + (mc.fontRenderer.FONT_HEIGHT + 4) / 2);
        MKText manaCost = new MKText(mc.fontRenderer, String.format("Mana: %.2f", ability.getManaCost(displayLevel)));
        manaCost.setX(panelX).setY(abilityHeight + (mc.fontRenderer.FONT_HEIGHT + 2) * 2);
        manaCost.setColor(4934475);
        MKText cooldown = new MKText(mc.fontRenderer, String.format("Cooldown: %.2f",
                (float) pData.getCooldownForLevel(ability, displayLevel) / (float) GameConstants.TICKS_PER_SECOND));
        cooldown.setX(panelX).setY(abilityHeight + (mc.fontRenderer.FONT_HEIGHT + 2) * 3);
        cooldown.setColor(4934475);
        MKText reqLev = new MKText(mc.fontRenderer, "Req. Level: " + ability.getRequiredLevel(level));
        reqLev.setX(panelX).setY(abilityHeight + (mc.fontRenderer.FONT_HEIGHT + 2) * 4);
        reqLev.setColor(4934475);
        MKText description = new MKText(mc.fontRenderer, ability.getAbilityDescription());
        description.setMultiline(true).setWidth(DESCRIPTION_WIDTH).setX(panelX)
                .setY(abilityHeight + (mc.fontRenderer.FONT_HEIGHT + 2) * 5);
        description.setColor(2631720);
        abilityList.addWidget(nameWid);
        abilityList.addWidget(manaCost);
        abilityList.addWidget(cooldown);
        abilityList.addWidget(reqLev);
        abilityList.addWidget(description);
        if (doLevelUp){
            MKButton upButton = new MKButton(panelX + DESCRIPTION_WIDTH + 2,
                    abilityHeight + (mc.fontRenderer.FONT_HEIGHT + 2) * 2,
                    20, 20, "+");
            upButton.setPressedCallback((button, buttonType) -> pressUpDownButton(ability, true));
            abilityList.addWidget(upButton);
            MKButton downButton = new MKButton(panelX + DESCRIPTION_WIDTH + 2,
                    abilityHeight + (mc.fontRenderer.FONT_HEIGHT + 2) * 2 + 24,
                    20, 20, "-");
            downButton.setPressedCallback((button, buttonType) -> pressUpDownButton(ability, false));
            abilityList.addWidget(downButton);
            addPreDrawRunnable(() -> {
                int lvl = pData.getAbilityRank(ability.getAbilityId());
                upButton.setEnabled(pData.getUnspentPoints() > 0 &&
                        pData.getLevel() >= ability.getRequiredLevel(lvl) && lvl < ability.getMaxRank());
                downButton.setEnabled(lvl > 0);
            });
        }
        addPreDrawRunnable(() -> {
            int lvl = pData.getAbilityRank(ability.getAbilityId());
            int displayLvl = Math.max(1, lvl); // Don't show the stats for a 0-level spell
            String newName;
            if (lvl > 0) {
                newName = String.format("%s %d", ability.getAbilityName(), lvl);
            } else {
                newName = ability.getAbilityName();
            }
            nameWid.setText(newName);
            manaCost.setText(String.format("Mana: %.2f", ability.getManaCost(displayLvl)));
            cooldown.setText(String.format("Cooldown: %.2f",
                    (float) pData.getCooldownForLevel(ability, displayLvl) / (float) GameConstants.TICKS_PER_SECOND));
            reqLev.setText("Req. Level: " + Math.max(1, ability.getRequiredLevel(lvl)));
        });
        return description.getY() + description.getHeight() + 12;
    }

    @Override
    public void setupScreen() {
        super.setupScreen();
        int xPos = width / 2 - PANEL_WIDTH / 2;
        int yPos = height / 2 - PANEL_HEIGHT / 2;
        ScaledResolution scaledRes = new ScaledResolution(mc);
        mainRoot = new MKWidget(xPos, yPos, PANEL_WIDTH, PANEL_HEIGHT);
        addState("main", mainRoot);

        IPlayerData pData = MKUPlayerData.get(this.mc.player);
        if (pData == null)
            return;
        PlayerClass playerClass = MKURegistry.getClass(pData.getClassId());
        if (playerClass == null)
            return;

        // Title and Class Icon
        MKWidget className = new MKText(mc.fontRenderer, playerClass.getClassName())
                .setColor(8129636)
                .setX(xPos + 28)
                .setY(yPos + 9);
        mainRoot.addWidget(className);
        IClassClientData classProvider = playerClass.getClientData();
        ResourceLocation iconLoc1 = classProvider.getIcon();
        int iconHeight = yPos + 6;
        MKImage classIcon = new MKImage(xPos + 6, iconHeight, 16, 16, iconLoc1);
        mainRoot.addWidget(classIcon);

        // Stat Panel
        MKScrollView statScrollView = new MKScrollView(xPos + STAT_PANEL_START_X + 3,
                yPos + STAT_PANEL_START_Y + 3,
                STAT_PANEL_WIDTH - 6, STAT_PANEL_HEIGHT - 6,
                scaledRes.getScaleFactor(), true);
        statScrollView.addWidget(drawStatPanel(pData, 0, 0));
        statScrollView.setToTop();
        statScrollView.setToRight();
        mainRoot.addWidget(statScrollView);

        // Abilities
        MKScrollView abilityScrollView = new MKScrollView(xPos + 4,
                yPos + 28,
                ABILITY_SCROLL_WIDTH, PANEL_HEIGHT - 32,
                scaledRes.getScaleFactor(), true);
        MKWidget abilityList = new MKWidget(0, 0);
        int abilityHeight = 0;
        int lastIndex = 0;
        for (int i = 0; i < GameConstants.CLASS_ACTION_BAR_SIZE; i++) {
            PlayerAbility ability = playerClass.getOfferedAbilityBySlot(i);
            if (ability == null)
                continue;
            abilityHeight = drawAbility(ability, pData, abilityList, abilityHeight, i, true);
            lastIndex += 1;
        }
        Set<PlayerAbility> learnedUlts = pData.getLearnedUltimates();
        if (learnedUlts != null){
            List<PlayerAbility> ultimates = new ArrayList<>(pData.getLearnedUltimates());
            ultimates.sort((a1, a2) -> a1.getAbilityName().compareToIgnoreCase(a2.getAbilityName()));
            for (PlayerAbility ult : ultimates){
                abilityHeight = drawAbility(ult, pData, abilityList, abilityHeight, lastIndex, false);
                lastIndex += 1;
            }
        }

        abilityList.setWidth(ABILITY_SCROLL_WIDTH);
        abilityList.setHeight(abilityHeight);
        abilityScrollView.addWidget(abilityList);
        abilityScrollView.setToRight();
        abilityScrollView.setToTop();
        mainRoot.addWidget(abilityScrollView);
        drawSlottedTalentTrays(pData, xPos, yPos);
        setState("main");
    }

    private boolean pressUpDownButton(PlayerAbility ability, boolean up) {
        MKUltra.packetHandler.sendToServer(new LevelAbilityPacket(ability.getAbilityId(), up));
        return true;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        int xPos = width / 2 - PANEL_WIDTH / 2;
        int yPos = height / 2 - PANEL_HEIGHT / 2;

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(GuiTextures.CLASS_BACKGROUND_GRAPHIC);
        GL11.glDisable(GL11.GL_LIGHTING);
        drawModalRectWithCustomSizedTexture(xPos, yPos,
                0, 0,
                PANEL_WIDTH, PANEL_HEIGHT,
                BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    private MKWidget drawUltimatePanel(IPlayerData pData, int xPos, int yPos){
        List<ResourceLocation> ultimates = pData.getActiveUltimates();
        if (ultimates == null) {
            return null;
        } else {
            MKWidget root = new MKWidget(xPos, yPos, STAT_PANEL_WIDTH,
                    UIConstants.TEXT_HEIGHT);
            MKText title = new MKText(fontRenderer, I18n.format("mkultra.ui_msg.ultimates")).setColor(16777215);
            title.setX(xPos).setY(yPos).setWidth(STAT_PANEL_WIDTH);
            title.setIsCentered(true);
            root.addWidget(title);
            MKStackLayoutVertical layout = new MKStackLayoutVertical(xPos,
                    yPos + UIConstants.TEXT_HEIGHT, STAT_PANEL_WIDTH);
            layout.doSetWidth(true).setMarginTop(4);
            root.addWidget(layout);
            int count = 0;
            for (ResourceLocation ultimate : ultimates) {
                PlayerAbilityButton button;
                if (ultimate.equals(MKURegistry.INVALID_ABILITY)) {
                    button = new PlayerAbilityButton(null,
                            PlayerAbilityButton.AbilityType.ULTIMATE, pData, count, 0, 0);
                } else {
                    PlayerAbility ability = MKURegistry.getAbility(ultimate);
                    button = new PlayerAbilityButton(ability,
                            PlayerAbilityButton.AbilityType.ULTIMATE, pData, count, 0, 0);
                }
                count++;
                layout.addWidget(button);
            }
            root.setHeight(layout.getHeight() + UIConstants.TEXT_HEIGHT + 4);
            return root;
        }
    }

    private MKWidget drawPassivePanel(IPlayerData pData, int xPos, int yPos) {
        List<ResourceLocation> passives = pData.getActivePassives();
        if (passives == null) {
            return null;
        } else {
            MKWidget root = new MKWidget(xPos, yPos, STAT_PANEL_WIDTH,
                    UIConstants.TEXT_HEIGHT);
            MKText title = new MKText(fontRenderer, I18n.format("mkultra.ui_msg.passives")).setColor(16777215);
            title.setX(xPos).setY(yPos).setWidth(STAT_PANEL_WIDTH);
            title.setIsCentered(true);
            root.addWidget(title);
            MKStackLayoutVertical layout = new MKStackLayoutVertical(xPos,
                    yPos + UIConstants.TEXT_HEIGHT, STAT_PANEL_WIDTH);
            layout.doSetWidth(true).setMarginTop(4);
            root.addWidget(layout);
            int passiveCount = 0;
            for (ResourceLocation passive : passives) {
                PlayerAbilityButton button;
                if (passive.equals(MKURegistry.INVALID_ABILITY)) {
                    button = new PlayerAbilityButton(null,
                            PlayerAbilityButton.AbilityType.PASSIVE, pData, passiveCount, 0, 0);
                } else {
                    PlayerAbility ability = MKURegistry.getAbility(passive);
                    button = new PlayerAbilityButton(ability,
                            PlayerAbilityButton.AbilityType.PASSIVE, pData, passiveCount, 0, 0);
                }
                passiveCount++;
                layout.addWidget(button);
            }
            root.setHeight(layout.getHeight() + UIConstants.TEXT_HEIGHT + 4);
            return root;
        }
    }

    private MKWidget drawStatPanel(IPlayerData pData, int xPos, int yPos) {
        AbstractAttributeMap attributes = this.mc.player.getAttributeMap();
        MKWidget stackLayout = new MKStackLayoutVertical(xPos, yPos, STAT_PANEL_WIDTH - 6)
                .doSetWidth(true)
                .setMarginTop(4)
                .setMarginBot(4)
                .setPaddingTop(2)
                .setMarginLeft(4)
                .setMarginRight(4)
                .setPaddingBot(2);

        MKText level = new MKText(mc.fontRenderer,
                String.format("%s: %d", I18n.format("mkultra.ui_msg.level"), pData.getLevel()));
        level.setColor(16777215);
        MKText unspentPoints = new MKText(mc.fontRenderer,
                String.format("%s: %d/%d", I18n.format("mkultra.ui_msg.ability_points"),
                        pData.getUnspentPoints(), pData.getLevel()));
        unspentPoints.setColor(16777215);
        MKText talentPoints = new MKText(mc.fontRenderer,
                String.format("%s: %d/%d",
                        I18n.format("mkultra.ui_msg.talent_points"),
                        pData.getUnspentTalentPoints(), pData.getTotalTalentPoints()));
        talentPoints.setColor(16777215);
        String armorClassString = String.format("%s: %s",
                I18n.format("mkultra.ui_msg.armor_class"),
                pData.getArmorClass().getName());
        MKText armorClass = new MKText(mc.fontRenderer, armorClassString);
        armorClass.setColor(16777215);
        stackLayout.addWidget(level);
        stackLayout.addWidget(unspentPoints);
        stackLayout.addWidget(talentPoints);
        stackLayout.addWidget(armorClass);
        addPreDrawRunnable(() -> {
            talentPoints.setText(String.format("%s: %d/%d",
                    I18n.format("mkultra.ui_msg.talent_points"),
                    pData.getUnspentTalentPoints(), pData.getTotalTalentPoints()));
            unspentPoints.setText(String.format("%s: %d/%d",
                    I18n.format("mkultra.ui_msg.ability_points"),
                    pData.getUnspentPoints(), pData.getLevel()));
            level.setText(String.format("%s: %d", I18n.format("mkultra.ui_msg.level"),
                    pData.getLevel()));
            armorClass.setText(String.format("%s: %s",
                    I18n.format("mkultra.ui_msg.armor_class"),
                    pData.getArmorClass().getName()));
        });

        for (IAttribute attr : STAT_PANEL_ATTRIBUTES) {
            IAttributeInstance attribute = attributes.getAttributeInstance(attr);
            String text = String.format("%s: %.2f", I18n.format(String.format("attribute.name.%s",
                    attribute.getAttribute().getName())), attribute.getAttributeValue());
            MKText textWidget = new MKText(mc.fontRenderer, text).setMultiline(true);
            addPreDrawRunnable(() -> {
                String newText = String.format("%s: %.2f", I18n.format(String.format("attribute.name.%s",
                        attribute.getAttribute().getName())), attribute.getAttributeValue());
                textWidget.setText(newText);
                if (attribute.getAttributeValue() < attribute.getBaseValue()) {
                    textWidget.setColor(13111115);
                } else if (attribute.getAttributeValue() > attribute.getBaseValue()) {
                    textWidget.setColor(3334475);
                } else {
                    textWidget.setColor(16777215);
                }
            });
            stackLayout.addWidget(textWidget);
        }
        return stackLayout;
    }
}
