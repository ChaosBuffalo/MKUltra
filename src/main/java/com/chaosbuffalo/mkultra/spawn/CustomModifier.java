package com.chaosbuffalo.mkultra.spawn;

import net.minecraft.entity.EntityLivingBase;

import java.util.function.BiConsumer;

public class CustomModifier {
    public int level;
    public int maxLevel;

    private final BiConsumer<EntityLivingBase, CustomModifier> applier;

    public CustomModifier(BiConsumer<EntityLivingBase, CustomModifier> applierIn) {
        this.applier = applierIn;
    }

    public void apply(EntityLivingBase entity, int level, int maxLevel) {
        this.level = level;
        this.maxLevel = maxLevel;
        this.applier.accept(entity, this);
    }
}
