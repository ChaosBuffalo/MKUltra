package com.chaosbuffalo.mkultra.abilities.misc;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.AbilityContext;
import com.chaosbuffalo.mkcore.abilities.AbilityTargetSelector;
import com.chaosbuffalo.mkcore.abilities.AbilityTargeting;
import com.chaosbuffalo.mkcore.abilities.MKAbility;
import com.chaosbuffalo.mkcore.core.AbilityType;
import com.chaosbuffalo.mkcore.core.CastInterruptReason;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.serialization.attributes.IntAttribute;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.fml.RegistryObject;

import java.util.List;

public abstract class PositionFlurryAbility extends MKAbility {
    protected final IntAttribute tickRate = new IntAttribute("tickRate", GameConstants.TICKS_PER_SECOND / 2);
    protected final RegistryObject<? extends PositionTargetingAbility> abilityToCast;

    public PositionFlurryAbility(RegistryObject<? extends PositionTargetingAbility> abilityToCast) {
        super();
        addAttributes(tickRate);
        this.abilityToCast = abilityToCast;
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.PBAOE;
    }

    @Override
    public AbilityType getType() {
        return AbilityType.Ultimate;
    }

    @Override
    public boolean canApplyCastingSpeedModifier() {
        return false;
    }

    @Override
    public boolean isInterruptedBy(IMKEntityData targetData, CastInterruptReason reason) {
        return false;
    }

    @Override
    public void continueCast(LivingEntity castingEntity, IMKEntityData casterData, int castTimeLeft, AbilityContext context) {
        super.continueCast(castingEntity, casterData, castTimeLeft, context);
        if (castTimeLeft % tickRate.value() == 0) {
            float dist = getDistance(castingEntity);
            Vector3d minBound = castingEntity.getPositionVec().subtract(dist, 1.0, dist);
            Vector3d maxBound = castingEntity.getPositionVec().add(dist, 4.0, dist);
            List<LivingEntity> entities = castingEntity.getEntityWorld().getEntitiesWithinAABB(LivingEntity.class,
                    new AxisAlignedBB(minBound, maxBound));
            abilityToCast.ifPresent(ab -> {
                for (LivingEntity ent : entities) {
                    if (Targeting.isValidTarget(getTargetContext(), castingEntity, ent)) {
                        ab.castAtPosition(castingEntity, ent.getPositionVec());
                    }
                }
            });
        }
    }
}
