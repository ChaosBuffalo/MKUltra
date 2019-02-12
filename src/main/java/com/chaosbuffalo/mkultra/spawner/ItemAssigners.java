package com.chaosbuffalo.mkultra.spawner;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import java.util.function.BiConsumer;

public class ItemAssigners {

    public static BiConsumer<EntityLivingBase, ItemChoice> MAINHAND = (entity, choice) -> {
        entity.setHeldItem(EnumHand.MAIN_HAND, choice.item);
    };
}
