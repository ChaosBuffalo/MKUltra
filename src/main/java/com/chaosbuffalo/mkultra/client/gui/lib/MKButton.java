package com.chaosbuffalo.mkultra.client.gui.lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiFunction;
import java.util.function.Function;

public class MKButton extends MKWidget {
    protected static final ResourceLocation BUTTON_TEXTURES = new ResourceLocation("textures/gui/widgets.png");
    public String buttonText;
    public BiFunction<MKButton, Integer, Boolean> pressedCallback;
    public static final int DEFAULT_HEIGHT = 20;
    public static final int DEFAULT_WIDTH = 200;

    public MKButton(int x, int y, String buttonText) {
        this(x, y, DEFAULT_WIDTH, DEFAULT_HEIGHT, buttonText);
    }

    public MKButton(String buttonText, int width, int height){
        this(0, 0, width, height, buttonText);
    }

    public MKButton(String buttonText){
        this(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT, buttonText);
    }

    public MKButton(String buttonText, int height){
        this(0, 0, DEFAULT_WIDTH, height, buttonText);
    }

    public MKButton(int width, String buttonText){
        this(0, 0, width, DEFAULT_HEIGHT, buttonText);
    }

    public MKButton(int x, int y, int width, int height, String buttonText) {
        super(x, y, width, height);
        this.buttonText = buttonText;
    }

    public MKButton setPressedCallback(BiFunction<MKButton, Integer, Boolean> callback){
        this.pressedCallback = callback;
        return this;
    }

    @Override
    public boolean onMousePressed(Minecraft minecraft, int mouseX, int mouseY, int mouseButton){
        if (pressedCallback != null){
            if (pressedCallback.apply(this, mouseButton)){
                playPressSound(minecraft.getSoundHandler());
                return true;
            }
        }
        return false;
    }

    public int getHoverState(boolean isHovering) {
        int i = 1;
        if (!this.isEnabled()) {
            i = 0;
        } else if (isHovering) {
            i = 2;
        }
        return i;
    }

    @Override
    public void draw(Minecraft mc, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
        FontRenderer fontrenderer = mc.fontRenderer;
        mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.hovered = isInBounds(mouseX, mouseY);
        int i = getHoverState(this.hovered);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE,
                GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA,
                GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.drawTexturedModalRect(
                this.getX(),
                this.getY(),
                0,
                46 + i * 20,
                this.getWidth() / 2, this.getHeight());
        this.drawTexturedModalRect(
                this.getX() + this.getWidth() / 2,
                this.getY(),
                200 - this.getWidth() / 2,
                46 + i * 20,
                this.getWidth() / 2, this.getHeight());
        int j = 14737632;
        if (!this.isEnabled()) {
            j = 10526880;
        } else if (this.hovered) {
            j = 16777120;
        }
        this.drawCenteredString(fontrenderer, this.buttonText,
                this.getX() + this.getWidth() / 2,
                this.getY() + (this.getHeight() - 8) / 2, j);
    }

    public void playPressSound(SoundHandler soundHandler) {
        soundHandler.playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }
}
