package com.chaosbuffalo.mkultra.item.interfaces;

import com.chaosbuffalo.mkultra.core.IClassClientData;
import net.minecraft.util.ResourceLocation;

public interface IClassProvider extends IClassClientData {
    ResourceLocation getIconForProvider();
    String getClassSelectionText();
}
