package com.chaosbuffalo.mkultra.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.function.Function;

public class MKButton extends MKWidget {
    protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");
    public String buttonText;
    public Function<MKButton, Boolean> pressedCallback;

    public MKButton(int x, int y, String buttonText) {
        this(x, y, 200, 20, buttonText);
    }

    public MKButton(String buttonText, int width, int height){
        this(0, 0, width, height, buttonText);
    }

    public MKButton(int x, int y, int width, int height, String buttonText) {
        super(x, y, width, height);
        this.buttonText = buttonText;
    }

    public void setPressedCallback(Function<MKButton, Boolean> callback){
        this.pressedCallback = callback;
    }

    @Override
    public boolean onMousePressed(Minecraft minecraft, int mouseX, int mouseY, int mouseButton){
        if (pressedCallback != null){
            if (pressedCallback.apply(this)){
                return true;
            }
        }
        return false;
    }

    protected int getHoverState(boolean isHovering) {
        int i = 1;
        if (!this.enabled) {
            i = 0;
        } else if (isHovering) {
            i = 2;
        }
        return i;
    }

    public void draw(Minecraft mc, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
        FontRenderer fontrenderer = mc.fontRenderer;
        mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
        int i = this.getHoverState(this.hovered);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.drawTexturedModalRect(
                this.x,
                this.y,
                0,
                46 + i * 20,
                this.width / 2, this.height);
        this.drawTexturedModalRect(
                this.x + this.width / 2,
                this.y,
                200 - this.width / 2,
                46 + i * 20,
                this.width / 2, this.height);
        int j = 14737632;
        if (!this.enabled) {
            j = 10526880;
        } else if (this.hovered) {
            j = 16777120;
        }
        this.drawCenteredString(fontrenderer, this.buttonText,
                this.x + this.width / 2,
                this.y + (this.height - 8) / 2, j);
    }
}
