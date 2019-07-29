package com.chaosbuffalo.mkultra.core;

import net.minecraft.util.ResourceLocation;

public interface IClassProvider {
    ResourceLocation getIdentity();
    String getClassSelectionText();

    default boolean isSameAs(IClassProvider other) {
        return getIdentity().compareTo(other.getIdentity()) == 0;
    }

    default boolean teachesClass(PlayerClass playerClass) {
        // Temporary. Will be refactored when classes no longer declare their providers
        return playerClass.getClassProvider().isSameAs(this);
    }
}
