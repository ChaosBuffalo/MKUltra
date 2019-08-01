package com.chaosbuffalo.mkultra.client.gui.lib;


import net.minecraft.client.Minecraft;

public abstract class MKLayout extends MKWidget {
    private int paddingLeft;
    private int paddingRight;
    private int paddingTop;
    private int paddingBot;
    private int marginLeft;
    private int marginRight;
    private int marginTop;
    private int marginBot;

    public MKLayout(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void drawWidget(Minecraft mc, int mouseX, int mouseY, float partialTicks){
        setupLayoutStartState();
        draw(mc, getX(), getY(), getWidth(), getHeight(), mouseX, mouseY, partialTicks);
        int i = 0;
        for (MKWidget child : children){
            doLayout(child, i);
            if (child.isVisible()){
                child.drawWidget(mc, mouseX, mouseY, partialTicks);
            }
            i++;
        }
    }

    public void setupLayoutStartState(){
    }

    public void doLayout(MKWidget widget, int index){
    }

    public void recomputeChildren(){

    }

    public MKLayout setMarginTop(int value){
        this.marginTop = value;
        return this;
    }

    public int getMarginTop(){
        return marginTop;
    }

    public MKLayout setMarginBot(int value){
        this.marginBot = value;
        return this;
    }

    public int getMarginBot(){
        return marginBot;
    }

    public MKLayout setMarginLeft(int value){
        this.marginLeft = value;
        return this;
    }

    public int getMarginLeft(){
        return marginLeft;
    }

    public MKLayout setMarginRight(int value){
        this.marginRight = value;
        return this;
    }

    public int getMarginRight(){
        return marginRight;
    }

    public MKLayout setPaddingTop(int value){
        this.paddingTop = value;
        return this;
    }

    public int getPaddingTop(){
        return paddingTop;
    }

    public MKLayout setPaddingBot(int value){
        this.paddingBot = value;
        return this;
    }

    public int getPaddingBot(){
        return paddingBot;
    }

    public MKLayout setPaddingLeft(int value){
        this.paddingLeft = value;
        return this;
    }

    public int getPaddingLeft(){
        return paddingLeft;
    }

    public MKLayout setPaddingRight(int value){
        this.paddingRight = value;
        return this;
    }

    public int getPaddingRight(){
        return paddingRight;
    }

}
