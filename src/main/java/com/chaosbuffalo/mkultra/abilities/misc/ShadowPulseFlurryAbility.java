package com.chaosbuffalo.mkultra.abilities.misc;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.ai.conditions.MeleeUseCondition;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.MKUAbilities;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import javax.annotation.Nullable;

public class ShadowPulseFlurryAbility extends PositionFlurryAbility{
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "shadow_bolt_casting");

    public ShadowPulseFlurryAbility() {
        super(MKUAbilities.SHADOW_PULSE);
        setCastTime(GameConstants.TICKS_PER_SECOND * 3);
        //FIXME: this should be simplified in attribute rework
        tickRate.setValue(GameConstants.TICKS_PER_SECOND);
        tickRate.setDefaultValue(GameConstants.TICKS_PER_SECOND);
        setCooldownSeconds(5);
        setManaCost(10);
        addSkillAttribute(MKAttributes.EVOCATION);
        setUseCondition(new MeleeUseCondition(this));
        casting_particles.setDefaultValue(CASTING_PARTICLES);
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
