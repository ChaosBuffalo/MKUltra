package com.chaosbuffalo.mkultra.client.gui;


import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;

/**
 * Created by Jacob on 7/29/2016.
 */
public class PartyPanel extends Gui {

    private static final int MIN_PARTY_PANEL_START = 80;
    private static final int PARTY_MEMBER_HEIGHT = 34;
    private static final int PARTY_MEMBER_WIDTH = 50;
    private static final int PARTY_MEMBER_X_OFFSET = 6;

    private Minecraft mc;

    public PartyPanel(Minecraft mc) {
        super();

        // We need this to invoke the render engine.
        this.mc = mc;
    }

    private int getPartyStartY(int partyCount) {
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int height = scaledresolution.getScaledHeight();
        int barStart = height / 2 - (partyCount * PARTY_MEMBER_HEIGHT) / 2;
        return Math.max(barStart, MIN_PARTY_PANEL_START);
    }

    private int getPartyStartX() {
        ScaledResolution scaledresolution = new ScaledResolution(this.mc);
        int width = scaledresolution.getScaledWidth();
        return width - PARTY_MEMBER_WIDTH - PARTY_MEMBER_X_OFFSET;
    }


    private void drawPartyMembers() {
        Team playerTeam = mc.player.getTeam();
        if (playerTeam == null)
            return;
        ArrayList<EntityPlayer> members = new ArrayList<>();
        int pCount = 0;
        for (String member : playerTeam.getMembershipCollection()) {
            EntityPlayer p = mc.world.getPlayerEntityByName(member);
            if (p != null && p.getUniqueID() != mc.player.getUniqueID()) {
                members.add(p);
            }
        }
        int partySize = members.size();
        int partyStartY = getPartyStartY(partySize);
        int partyStartX = getPartyStartX();

        GL11.glDisable(GL11.GL_LIGHTING);
        for (EntityPlayer member : members) {
            drawPartyMember(member, pCount, partyStartX, partyStartY);
            pCount += 1;

        }
    }

    private void drawPartyMember(EntityPlayer player, int index, int panelX, int panelY) {

        int memberY = panelY + index * PARTY_MEMBER_HEIGHT;
        IPlayerData data = MKUPlayerData.get(player);
        int manaSize;
        if (data == null || data.getTotalMana() == 0) {
            manaSize = 0;
        } else {
            manaSize = PARTY_MEMBER_WIDTH * data.getMana() / data.getTotalMana();
        }
        int healthSize = (int) (PARTY_MEMBER_WIDTH * player.getHealth() / player.getMaxHealth());
        mc.renderEngine.bindTexture(new ResourceLocation(MKUltra.MODID, "textures/gui/abilitybar.png"));
        GlStateManager.pushMatrix();
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        this.drawTexturedModalRect(panelX, memberY + 14, 26, 0, healthSize, 8);
        this.drawTexturedModalRect(panelX, memberY + 24, 26, 10, manaSize, 8);
        GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
        mc.fontRenderer.drawString(player.getName(), panelX + 2, memberY + 2, 16777215);
        GlStateManager.popMatrix();
        mc.renderEngine.bindTexture(Gui.ICONS);
    }


    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onRenderExperienceBar(RenderGameOverlayEvent event) {
        if (event.isCancelable() || event.getType() != RenderGameOverlayEvent.ElementType.EXPERIENCE) {
            return;
        }
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        drawPartyMembers();
    }
}
