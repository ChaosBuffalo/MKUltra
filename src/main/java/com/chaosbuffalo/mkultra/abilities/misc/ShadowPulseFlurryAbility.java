package com.chaosbuffalo.mkultra.abilities.misc;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.ai.conditions.MeleeUseCondition;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.MKUAbilities;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class ShadowPulseFlurryAbility extends PositionFlurryAbility{
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "shadow_bolt_casting");

    public ShadowPulseFlurryAbility() {
        super(MKUAbilities.WRATH_BEAM);
        setCastTime(GameConstants.TICKS_PER_SECOND * 3);
        tickRate.setValue(GameConstants.TICKS_PER_SECOND);
        setCooldownSeconds(5);
        setManaCost(10);
        addSkillAttribute(MKAttributes.EVOCATION);
        setUseCondition(new MeleeUseCondition(this));
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {

        return new TranslationTextComponent(getDescriptionTranslationKey(),
                NUMBER_FORMATTER.format(getDistance(entityData.getEntity())),
                NUMBER_FORMATTER.format(convertDurationToSeconds(tickRate.value())));
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ENEMY;
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 10.0f;
    }

    @Nullable
    @Override
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.hostile_casting_fire;
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return null;
    }
}
