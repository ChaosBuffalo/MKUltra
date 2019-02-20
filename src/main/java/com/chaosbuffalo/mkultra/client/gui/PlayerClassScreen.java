package com.chaosbuffalo.mkultra.client.gui;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.item.interfaces.IClassProvider;
import com.chaosbuffalo.mkultra.network.packets.client.LevelAbilityPacket;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Jacob on 3/16/2016.
 */
public class PlayerClassScreen extends GuiScreen {


    private int[] abilityPanelYs = {22, 88, 154, 88, 154};
    private int[] abilityPanelXs = {10, 10, 10, 138, 138};

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int width = scaledresolution.getScaledWidth();
        int height = scaledresolution.getScaledHeight();
        int panelWidth = 256;
        int panelHeight = 224;
        int xPos = width / 2 - panelWidth / 2;
        int yPos = height / 2 - panelHeight / 2;

        ResourceLocation loc = new ResourceLocation(MKUltra.MODID, "textures/gui/class_background.png");
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.renderEngine.bindTexture(loc);
        GL11.glDisable(GL11.GL_LIGHTING);
        this.drawTexturedModalRect(xPos, yPos, 0, 0, panelWidth, panelHeight);
        int titleHeight = 10;

        IPlayerData pData = MKUPlayerData.get(this.mc.player);
        if (pData == null)
            return;
        PlayerClass playerClass = MKURegistry.getClass(pData.getClassId());
        if (playerClass == null)
            return;

        this.fontRenderer.drawString(playerClass.getClassName(), xPos + 28, yPos + 9, 8129636);
        int iconHeight = yPos + 6;
        //drawing stats
        drawStatPanel(pData, xPos, yPos);

        //drawing ability
        //draw icons
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        IClassProvider classProvider = (IClassProvider) playerClass.getUnlockItem();
        ResourceLocation iconLoc1 = classProvider.getIconForProvider();
        this.mc.renderEngine.bindTexture(iconLoc1);
        Gui.drawModalRectWithCustomSizedTexture(xPos + 6, iconHeight, 0, 0, 16, 16, 16, 16);
        for (int i = 0; i < GameConstants.ACTION_BAR_SIZE; i++) {
            PlayerAbility ability = playerClass.getOfferedAbilityBySlot(i);
            if (ability == null)
                continue;

            int panelX = abilityPanelXs[i];
            int panelY = abilityPanelYs[i];
            mc.getTextureManager().bindTexture(ability.getAbilityIcon());
            Gui.drawModalRectWithCustomSizedTexture(xPos + panelX + 2,
                    yPos + panelY + 2 + 14,
                    0, 0, 16, 16, 16, 16);
        }
        //draw text
        this.buttonList.clear();
        int scaleFactor = 2;
        int unspent = pData.getUnspentPoints();
        for (int i = 0; i < GameConstants.ACTION_BAR_SIZE; i++) {
            PlayerAbility ability = playerClass.getOfferedAbilityBySlot(i);
            if (ability == null)
                continue;

            int level = pData.getAbilityRank(ability.getAbilityId());

            // Show req. level 1 at the minimum
            int reqLevel = ability.getRequiredLevel(level);
            reqLevel = reqLevel < 1 ? 1 : reqLevel;

            int panelX = abilityPanelXs[i];
            int panelY = abilityPanelYs[i];

            GuiButton upButton = new GuiButton(i, xPos + panelX + 22 + 40 + 4, yPos + panelY + 10 + 3, 16, 19, "+");
            upButton.enabled = unspent > 0 && pData.getLevel() >= reqLevel && level < ability.getMaxRank();
            upButton.drawButton(this.mc, mouseX, mouseY, partialTicks);
            this.buttonList.add(upButton);

            GuiButton downButton = new GuiButton(i + GameConstants.ACTION_BAR_SIZE, xPos + panelX + 22 + 40 + 20 + 4, yPos + panelY + 10 + 3, 16, 19, "-");
            downButton.enabled = level > 0;
            downButton.drawButton(this.mc, mouseX, mouseY, partialTicks);
            this.buttonList.add(downButton);

            String name;
            if (level > 0) {
                name = String.format("%s %d", ability.getAbilityName(), level);
            } else {
                name = ability.getAbilityName();
            }
            this.fontRenderer.drawString(name, xPos + panelX, yPos + panelY + 3, 38600);

            GlStateManager.pushMatrix();
            GlStateManager.scale(1.0f / scaleFactor, 1.0f / scaleFactor, 1.0f / scaleFactor);

            int minLevel = Math.max(1, level); // Don't show the stats for a 0-level spell
            this.fontRenderer.drawString("Mana: " + ability.getManaCost(minLevel),
                    (xPos + panelX + 22) * scaleFactor, (yPos + panelY + 16) * scaleFactor, 0);
            String cooldown = String.format("Cooldown: %.2f", (float) pData.getAbilityCooldown(ability) / (float) GameConstants.TICKS_PER_SECOND);
            this.fontRenderer.drawString(cooldown,
                    (xPos + panelX + 22) * scaleFactor, (yPos + panelY + 23) * scaleFactor, 0);
            this.fontRenderer.drawString("Req. Level: " + reqLevel,
                    (xPos + panelX + 22) * scaleFactor, (yPos + panelY + 30) * scaleFactor, 0);
            this.fontRenderer.drawSplitString(ability.getAbilityDescription(),
                    (xPos + panelX) * scaleFactor, (yPos + panelY + 36) * scaleFactor, 114 * scaleFactor, 0);
            GlStateManager.popMatrix();
        }
    }

    private void drawStatPanel(IPlayerData pData, int xPos, int yPos) {
        int statPanelX = xPos + 141;
        int statPanelY = yPos + 11;
        float statScalingFactor = 1.5f;

        GlStateManager.pushMatrix();
        GlStateManager.scale(1.0f / statScalingFactor, 1.0f / statScalingFactor, 1.0f / statScalingFactor);
        int xCoord = (int) ((statPanelX + 3) * statScalingFactor);
        int yCoord = (int) ((statPanelY + 4) * statScalingFactor);

        String regenRate = String.format("Mana Regen Rate: %.2f", pData.getManaRegenRate());
        String cdRate = String.format("Cooldown Reduction: %.2f%%", (pData.getCooldownProgressSpeed() - 1) * 100);
        String magicArmor = String.format("Magic Armor: %.2f", pData.getMagicArmor());
        String magicDamageBonus = String.format("Magic Damage Bonus: %.2f", pData.getMagicDamageBonus());

        this.fontRenderer.drawString("Level: " + pData.getLevel(), xCoord, yCoord, 38600);
        this.fontRenderer.drawString("Max Mana: " + pData.getTotalMana(), xCoord, yCoord + 11, 38600);
        this.fontRenderer.drawString(regenRate, xCoord, yCoord + 22, 38600);
        this.fontRenderer.drawString("Unspent Points: " + pData.getUnspentPoints(), xCoord, yCoord + 33, 38600);
        this.fontRenderer.drawString(magicArmor, xCoord, yCoord + 44, 38600);
        this.fontRenderer.drawString(magicDamageBonus, xCoord, yCoord + 55, 38600);
        this.fontRenderer.drawString(cdRate, xCoord, yCoord + 66, 38600);
        GlStateManager.popMatrix();
    }


    @Override
    protected void actionPerformed(GuiButton button) {
        IPlayerData pData = MKUPlayerData.get(mc.player);
        if (pData == null)
            return;
        PlayerClass playerClass = MKURegistry.getClass(pData.getClassId());
        if (playerClass == null)
            return;
        PlayerAbility ability = playerClass.getOfferedAbilityBySlot(button.id % GameConstants.ACTION_BAR_SIZE);
        if (ability == null)
            return;

        boolean raise = button.id < GameConstants.ACTION_BAR_SIZE;
        MKUltra.packetHandler.sendToServer(new LevelAbilityPacket(ability.getAbilityId(), raise));
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

}
