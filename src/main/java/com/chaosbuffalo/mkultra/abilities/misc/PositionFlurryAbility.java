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
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.RegistryObject;

import java.util.List;
import java.util.function.Consumer;

public abstract class PositionFlurryAbility extends MKAbility {
    protected final IntAttribute tickRate = new IntAttribute("tickRate", GameConstants.TICKS_PER_SECOND / 2);
    protected final RegistryObject<? extends PositionTargetingAbility> abilityToCast;

    public PositionFlurryAbility(RegistryObject<? extends PositionTargetingAbility> abilityToCast) {
        super();
        addAttributes(tickRate);
        this.abilityToCast = abilityToCast;
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {

        return new TranslationTextComponent("mkultra.ability.flurry.description",
                abilityToCast.get().getAbilityName(),
                NUMBER_FORMATTER.format(getDistance(entityData.getEntity())),
                NUMBER_FORMATTER.format(convertDurationToSeconds(tickRate.value())));
    }

    @Override
    public void buildDescription(IMKEntityData casterData, Consumer<ITextComponent> consumer) {
        super.buildDescription(casterData, consumer);
        abilityToCast.ifPresent(x -> {
            consumer.accept(x.getAbilityName().copyRaw().mergeStyle(TextFormatting.UNDERLINE).mergeStyle(TextFormatting.GRAY));
            consumer.accept(x.exposeAbilityDescription(casterData).copyRaw().mergeStyle(TextFormatting.GRAY));
        });
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
