package com.chaosbuffalo.mkultra.item.interfaces;

import net.minecraft.util.ResourceLocation;

public interface IClassProvider {
    ResourceLocation getIconForProvider();
    String getClassSelectionText();
    String getXpTableText();
    ResourceLocation getXpTableBackground();
    int getXpTableTextColor();
}
