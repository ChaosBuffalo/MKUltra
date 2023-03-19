package com.chaosbuffalo.mkultra.abilities;

import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkultra.effects.BurnEffect;
import net.minecraft.entity.LivingEntity;

public class MKUAbilityUtils {

    public static boolean isBurning(LivingEntity entity) {
        return MKCore.getEntityData(entity).map(MKUAbilityUtils::isBurning).orElse(false);
    }

    public static boolean isBurning(IMKEntityData entityData) {
        return entityData.getEffects().isEffectActive(BurnEffect.INSTANCE);
    }
}
