package com.chaosbuffalo.mkultra.core;

import net.minecraft.util.ResourceLocation;

public interface IClassClientData {
    ResourceLocation getIcon();

    String getXpTableText();

    ResourceLocation getXpTableBackground();

    int getXpTableTextColor();
}
