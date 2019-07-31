package com.chaosbuffalo.mkultra.client.gui;


import net.minecraft.client.Minecraft;

public abstract class MKLayout extends MKWidget {



    public MKLayout(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public void drawWidget(Minecraft mc, int mouseX, int mouseY, float partialTicks){
        setupLayoutStartState();
        draw(mc, calcX(), calcY(), calcWidth(), calcHeight(), mouseX, mouseY, partialTicks);
        for (MKWidget child : children){
            if (child.visible){
                doLayout(child);
                child.drawWidget(mc, mouseX, mouseY, partialTicks);
            }
        }
    }

    public void setupLayoutStartState(){
    }

    public void doLayout(MKWidget widget){
    }
}
