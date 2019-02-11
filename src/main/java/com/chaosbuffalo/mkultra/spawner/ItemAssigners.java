package com.chaosbuffalo.mkultra.spawner;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.utils.SpawnerUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiFunction;

public class ItemAssigners {

    public static BiFunction<EntityLivingBase, ItemChoice, Boolean> MAINHAND = (entity, choice) -> {
        entity.setHeldItem(EnumHand.MAIN_HAND, choice.item);
        return Boolean.TRUE;
    };

    public static void setup(){
        SpawnerUtils.registerItemAssigner(new ResourceLocation(MKUltra.MODID, "main_hand"), MAINHAND);
    }

}
