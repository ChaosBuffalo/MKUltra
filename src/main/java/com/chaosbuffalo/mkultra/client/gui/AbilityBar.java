package com.chaosbuffalo.mkultra.client.gui;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.event.ClientKeyHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber(Side.CLIENT)
public class AbilityBar extends Gui {
    private static final int SLOT_COUNT = GameConstants.ACTION_BAR_SIZE;

    private static final ResourceLocation barTexture = new ResourceLocation(MKUltra.MODID,
            "textures/gui/abilitybar.png");
    private static final int SLOT_WIDTH = 19;
    private static final int SLOT_HEIGHT = 20;
    private static final int MANA_START_U = 21;
    private static final int MANA_START_V = 0;
    private static final int MANA_CELL_WIDTH = 3;
    private static final int MANA_CELL_HEIGHT = 8;

    private static final ResourceLocation COOLDOWN_ICON = new ResourceLocation(MKUltra.MODID,
            "textures/class/abilities/cooldown.png");

    private static final ResourceLocation TOGGLE_EFFECT = new ResourceLocation(MKUltra.MODID,
            "textures/class/abilities/ability_toggle.png");

    private static final int ABILITY_ICON_SIZE = 16;

    private static final int MIN_BAR_START_Y = 80;

    private Minecraft mc;

    public AbilityBar(Minecraft mc) {
        super();

        // We need this to invoke the render engine.
        this.mc = mc;
    }

    private int getBarStartY(int slotCount) {
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int height = scaledresolution.getScaledHeight();
        int barStart = height / 2 - (slotCount * SLOT_HEIGHT) / 2;
        return Math.max(barStart, MIN_BAR_START_Y);
    }

    private void drawMana(IPlayerData data) {
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int height = scaledresolution.getScaledHeight();

        this.mc.renderEngine.bindTexture(barTexture);
        GL11.glDisable(GL11.GL_LIGHTING);

        final int maxManaPerRow = 20;
        final int manaCellWidth = 4;
        final int manaCellRowSize = 9;

        int manaStartY = height - 24 - 10;
        int manaStartX = 24;

        for (int i = 0; i < data.getMana(); i++) {
            int manaX = manaCellWidth * (i % maxManaPerRow);
            int manaY = (i / maxManaPerRow) * manaCellRowSize;
            this.drawTexturedModalRect(manaStartX + manaX,
                    manaStartY + manaY,
                    MANA_START_U, MANA_START_V,
                    MANA_CELL_WIDTH, MANA_CELL_HEIGHT);
        }

    }

    private void drawCastBar(IPlayerData data){
        if (!data.isCasting()){
            return;
        }
        PlayerAbilityInfo info = data.getAbilityInfo(data.getCastingAbility());
        PlayerAbility ability = MKURegistry.getAbility(data.getCastingAbility());
        if (info == null || ability == null){
            return;
        }
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int height = scaledresolution.getScaledHeight();
        int castStartY = height / 2 + 8;
        int width = 50;
        int barSize = (int) (width * data.getCastTicks() / ability.getCastTime(info.getRank()));
        int castStartX = scaledresolution.getScaledWidth() / 2 - barSize / 2;
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawTexturedModalRect(castStartX, castStartY, 26,
                21, barSize, 3);
        GlStateManager.popMatrix();

    }

    private void drawBarSlots(int slotCount) {
        this.mc.renderEngine.bindTexture(barTexture);
        GL11.glDisable(GL11.GL_LIGHTING);

        int xOffset = 0;
        int yOffset = getBarStartY(slotCount);
        for (int i = 0; i < slotCount; i++) {
            this.drawTexturedModalRect(xOffset, yOffset + i * SLOT_HEIGHT,
                    0, 0, SLOT_WIDTH, SLOT_HEIGHT);
        }
    }

    private void drawAbilities(IPlayerData data, int slotCount, float partialTicks) {
        GL11.glDisable(GL11.GL_LIGHTING);

        final int slotAbilityOffsetX = 1;
        final int slotAbilityOffsetY = 2;

        int barStartY = getBarStartY(slotCount);

        float globalCooldown = ClientKeyHandler.getGlobalCooldown();

        for (int i = 0; i < slotCount; i++) {
            ResourceLocation abilityId = data.getAbilityInSlot(i);
            if (abilityId.equals(MKURegistry.INVALID_ABILITY))
                continue;

            PlayerAbility ability = MKURegistry.getAbility(abilityId);
            if (ability == null)
                continue;
            float manaCost = PlayerFormulas.applyManaCostReduction(data, ability.getManaCost(
                    data.getAbilityRank(ability.getAbilityId())));
            if (!data.isCasting() && data.getMana() >= manaCost){
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            } else {
                GlStateManager.color(0.5f, 0.5f, 0.5f, 1.0F);
            }
            mc.getTextureManager().bindTexture(ability.getAbilityIcon());
            Gui.drawModalRectWithCustomSizedTexture(slotAbilityOffsetX,
                    barStartY + slotAbilityOffsetY + (i * SLOT_HEIGHT),
                    0, 0,
                    ABILITY_ICON_SIZE, ABILITY_ICON_SIZE, ABILITY_ICON_SIZE, ABILITY_ICON_SIZE);

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            float cooldownFactor = data.getCooldownPercent(ability, partialTicks);
            if (globalCooldown > 0.0f && cooldownFactor == 0) {
                cooldownFactor = globalCooldown / ClientKeyHandler.getTotalGlobalCooldown();
            }

            // TODO: introduce min cooldown time so there is always a visual indicator that it's on cooldown

            if (cooldownFactor > 0) {
                int coolDownHeight = (int) (cooldownFactor * ABILITY_ICON_SIZE);
                if (coolDownHeight < 1) {
                    coolDownHeight = 1;
                }
                mc.getTextureManager().bindTexture(COOLDOWN_ICON);
                Gui.drawModalRectWithCustomSizedTexture(slotAbilityOffsetX,
                        barStartY + slotAbilityOffsetY + (i * SLOT_HEIGHT),
                        0, 0,
                        ABILITY_ICON_SIZE, coolDownHeight, ABILITY_ICON_SIZE, coolDownHeight);
            }

            if (ability instanceof PlayerToggleAbility) {
                if (mc.player.isPotionActive(((PlayerToggleAbility) ability).getToggleEffect())) {
                    mc.getTextureManager().bindTexture(TOGGLE_EFFECT);
                    Gui.drawModalRectWithCustomSizedTexture(slotAbilityOffsetX,
                            barStartY + slotAbilityOffsetY + (i * SLOT_HEIGHT),
                            0, 0,
                            ABILITY_ICON_SIZE, ABILITY_ICON_SIZE, ABILITY_ICON_SIZE, ABILITY_ICON_SIZE);
                }
            }
        }
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onRenderExperienceBar(RenderGameOverlayEvent event) {
        if (event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            return;
        }

        IPlayerData data = MKUPlayerData.get(mc.player);
        if (data == null || !data.hasChosenClass())
            return;

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        ResourceLocation loc = data.getAbilityInSlot(GameConstants.ACTION_BAR_SIZE -1);
        int slotCount = data.getActionBarSize();
        drawMana(data);
        drawCastBar(data);
        drawBarSlots(slotCount);
        drawAbilities(data, slotCount, event.getPartialTicks());
    }
}
