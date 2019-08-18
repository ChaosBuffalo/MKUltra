package com.chaosbuffalo.mkultra.client.gui.lib;

import static net.minecraft.client.gui.Gui.drawModalRectWithCustomSizedTexture;

public class DrawingUtils {

    public static void drawTexturedRect(int x, int y, int u, int v, int width, int height, int texWidth, int texHeight){
        drawModalRectWithCustomSizedTexture(x, y, u, v, width, height, texWidth, texHeight);
    }
}
