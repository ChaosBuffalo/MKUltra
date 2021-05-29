package com.chaosbuffalo.mkultra.abilities;

import com.chaosbuffalo.mkultra.effects.spells.BurnEffect;
import net.minecraft.entity.LivingEntity;

public class MKUAbilityUtils {

    public static boolean isBurning(LivingEntity entity){
        return entity.getActivePotionEffect(BurnEffect.INSTANCE) != null;
    }
}
