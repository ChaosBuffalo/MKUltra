package com.chaosbuffalo.mkultra.client.gui.lib;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;

public class MKText extends MKWidget {

    public String text;
    private FontRenderer fontRenderer;
    public int color;
    public boolean isMultiline;
    public boolean isCentered;

    public MKText(FontRenderer renderer, String text) {
        super(0, 0);
        this.color = 0;
        this.fontRenderer = renderer;
        this.text = text;
        setHeight(fontRenderer.FONT_HEIGHT);
        this.isMultiline = false;
    }

    public int getFontHeight(){
        return fontRenderer.FONT_HEIGHT;
    }

    public int getColor(){
        return color;
    }

    public MKText setColor(int i){
        this.color = i;
        return this;
    }

    public void draw(Minecraft mc, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
        if (isCentered()){
            this.drawCenteredString(this.fontRenderer, this.getText(),
                    this.getX() + this.getWidth() / 2,
                    this.getY() + (this.getHeight() - 8) / 2, color);
        } else if (isMultiline()){
            fontRenderer.drawSplitString(this.getText(), this.getX(), this.getY(), this.getWidth(), this.color);
        } else {
            fontRenderer.drawString(this.getText(), this.getX(), this.getY(), this.color);
        }

    }

    public MKText setIsCentered(boolean isCentered){
        this.isCentered = true;
        return this;
    }

    public boolean isCentered(){
        return isCentered;
    }

    public MKText setText(String text){
        this.text = text;
        updateLabel();
        return this;
    }

    public String getText(){
        return text;
    }

    public MKText setMultiline(boolean multiline){
        this.isMultiline = multiline;
        updateLabel();
        return this;
    }

    public boolean isMultiline(){
        return isMultiline;
    }

    public MKWidget setWidth(int width){
        super.setWidth(width);
        updateLabel();
        return this;
    }

    private void updateLabel(){
        if (isMultiline()){
            setHeight(fontRenderer.getWordWrappedHeight(getText(), getWidth()));
        } else {
            setHeight(fontRenderer.FONT_HEIGHT);
        }
    }
}
