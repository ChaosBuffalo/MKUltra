package com.chaosbuffalo.mkultra.client.gui.lib;

import net.minecraft.client.Minecraft;

public class MKModal extends MKWidget {

    private boolean doBackground;
    private int backgroundColor;
    private boolean closeOnClickOutsideContent;
    private Runnable onCloseCallback;

    public MKModal() {
        super(0, 0);
        doBackground = true;
        backgroundColor = 0x7D000000;
        closeOnClickOutsideContent = true;
    }

    public MKModal setBackgroundColor(int color){
        backgroundColor = color;
        return this;
    }

    public MKModal setCloseOnClickOutside(boolean value){
        closeOnClickOutsideContent = value;
        return this;
    }

    public boolean shouldCloseOnClickOutside(){
        return closeOnClickOutsideContent;
    }

    public MKModal setOnCloseCallback(Runnable callback){
        onCloseCallback = callback;
        return this;
    }

    public Runnable getOnCloseCallback(){
        return onCloseCallback;
    }

    @Override
    public boolean onMousePressed(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
        if (getScreen() != null && shouldCloseOnClickOutside()){
            getScreen().closeModal(this);
            return true;
        }
        return super.onMousePressed(minecraft, mouseX, mouseY, mouseButton);

    }

    public int getBackgroundColor(){
        return backgroundColor;
    }

    public MKModal setDoBackground(boolean value){
        doBackground = value;
        return this;
    }

    public boolean shouldDoBackground(){
        return doBackground;
    }

    @Override
    public void preDraw(Minecraft mc, int x, int y, int width, int height, int mouseX, int mouseY, float partialTicks) {
        super.preDraw(mc, x, y, width, height, mouseX, mouseY, partialTicks);
        if (shouldDoBackground()){
            drawRect(getX(), getY(), getWidth(), getHeight(), getBackgroundColor());
        }

    }
}
