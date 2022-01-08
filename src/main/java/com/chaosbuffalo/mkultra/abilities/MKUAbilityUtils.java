package com.chaosbuffalo.mkultra.abilities;

import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkultra.effects.spells.BurnEffect;
import net.minecraft.entity.LivingEntity;

public class MKUAbilityUtils {

    public static boolean isBurning(LivingEntity entity) {
        return MKCore.getEntityData(entity)
                .map(entityData -> entityData.getEffects().isEffectActive(BurnEffect.INSTANCE))
                .orElse(false);
    }
}
