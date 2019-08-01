package com.chaosbuffalo.mkultra.client.gui;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.client.gui.lib.MKButton;
import com.chaosbuffalo.mkultra.core.talents.TalentRecord;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class TalentButton extends MKButton {

    private static final ResourceLocation TALENT_SLOT_GRAPHIC = new ResourceLocation(MKUltra.MODID,
            "textures/talents/talent_slot.png");
    private static final ResourceLocation TALENT_SLOT_OVERLAY = new ResourceLocation(MKUltra.MODID,
            "textures/talents/talent_slot_complete.png");
    private static final int SLOT_WIDTH = 16;
    private static final int SLOT_HEIGHT = 16;
    private static final int OVERLAY_WIDTH = 2;
    private static final int OVERLAY_HEIGHT = 2;
    public static final int HEIGHT = 30;
    public static final int WIDTH = 70;
    public static final int SLOT_Y_OFFSET = 4;
    public static final int TEXT_OFFSET = 4;
    public static final int SLOT_X_OFFSET = 7;

    public final int index;
    public final String line;
    public final TalentRecord record;

    public TalentButton(int index, String line, TalentRecord record,
                        int x, int y){
        super(x, y, WIDTH, HEIGHT, record.getNode().getTalent().getTalentName());
        this.index = index;
        this.line = line;
        this.record = record;
    }

    @Override
    public void draw(Minecraft minecraft, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks){
        if (this.isVisible()) {
            FontRenderer fontrenderer = minecraft.fontRenderer;
            minecraft.getTextureManager().bindTexture(TALENT_SLOT_GRAPHIC);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = isInBounds(mouseX, mouseY);
            int hoverState = getHoverState(this.hovered);
            GlStateManager.enableBlend();
//            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
//            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            Gui.drawModalRectWithCustomSizedTexture(this.getX() + SLOT_X_OFFSET ,
                    this.getY() + SLOT_Y_OFFSET,
                    0, 0,
                    SLOT_WIDTH, SLOT_HEIGHT, SLOT_WIDTH, SLOT_HEIGHT);
            ResourceLocation icon;
            if (record.getRank() > 0){
                icon = record.getNode().getTalent().getFilledIcon();
            } else {
                icon = record.getNode().getTalent().getIcon();
            }
            minecraft.getTextureManager().bindTexture(icon);
            Gui.drawModalRectWithCustomSizedTexture(this.getX() + SLOT_X_OFFSET ,
                    this.getY() + SLOT_Y_OFFSET,
                    0, 0,
                    SLOT_WIDTH, SLOT_HEIGHT, SLOT_WIDTH, SLOT_HEIGHT);
            if (record.getRank() == record.getNode().getMaxRanks()){
                minecraft.getTextureManager().bindTexture(TALENT_SLOT_OVERLAY);
                Gui.drawModalRectWithCustomSizedTexture(
                        this.getX() + SLOT_X_OFFSET - OVERLAY_WIDTH/2,
                        this.getY() + SLOT_Y_OFFSET - OVERLAY_HEIGHT/2,
                        0, 0,
                        SLOT_WIDTH + OVERLAY_WIDTH,
                        SLOT_HEIGHT + OVERLAY_HEIGHT,
                        SLOT_WIDTH + OVERLAY_WIDTH,
                        SLOT_HEIGHT + OVERLAY_HEIGHT);
            }
            int textColor = 14737632;
            if (!this.isEnabled()) {
                textColor = 10526880;
            } else if (this.hovered) {
                textColor = 16777120;
            }
            this.drawCenteredString(fontrenderer, this.buttonText, this.getX() + this.getWidth() / 2,
                    this.getY() + SLOT_Y_OFFSET + SLOT_HEIGHT + OVERLAY_HEIGHT + TEXT_OFFSET, textColor);
        }
    }
}