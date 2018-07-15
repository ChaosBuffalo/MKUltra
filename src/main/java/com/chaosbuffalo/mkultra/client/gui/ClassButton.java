package com.chaosbuffalo.mkultra.client.gui;

import net.minecraft.client.gui.GuiButton;

/**
 * Created by Jacob on 7/14/2018.
 */
public class ClassButton extends GuiButton {

    public final int classInteger;

    public ClassButton(int classIntegerIn, int buttonId, int x, int y, int widthIn, int heightIn, String buttonText){
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        classInteger = classIntegerIn;
    }
}
