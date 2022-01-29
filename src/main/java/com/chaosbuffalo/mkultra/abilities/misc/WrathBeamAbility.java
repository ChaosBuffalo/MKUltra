package com.chaosbuffalo.mkultra.abilities.misc;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.LineEffectBuilder;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.instant.MKAbilityDamageEffect;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.IntAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.ResistanceEffects;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

public class WrathBeamAbility extends MKAbility {
    private static final ResourceLocation PULSE_PARTICLES = new ResourceLocation(MKUltra.MODID, "wrath_beam_pulse");
    private static final ResourceLocation WAIT_PARTICLES = new ResourceLocation(MKUltra.MODID, "wrath_beam_wait");
    public static final WrathBeamAbility INSTANCE = new WrathBeamAbility();
    protected final FloatAttribute base = new FloatAttribute("base", 5.0f);
    protected final FloatAttribute scale = new FloatAttribute("scale", 5.0f);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);
    protected final ResourceLocationAttribute pulse_particles = new ResourceLocationAttribute("pulse_particles", PULSE_PARTICLES);
    protected final ResourceLocationAttribute wait_particles = new ResourceLocationAttribute("wait_particles", WAIT_PARTICLES);
    protected final IntAttribute tickRate = new IntAttribute("tickRate", GameConstants.TICKS_PER_SECOND / 2);
    protected final IntAttribute duration = new IntAttribute("duration", GameConstants.TICKS_PER_SECOND * 2);
    protected final IntAttribute breakDuration = new IntAttribute("breakDuration", GameConstants.TICKS_PER_SECOND * 2);



    private WrathBeamAbility() {
        super(MKUltra.MODID, "ability.wrath_beam");
        setCastTime(GameConstants.TICKS_PER_SECOND * 2);
        setCooldownSeconds(10);
        setManaCost(6);
        addAttributes(base, scale, modifierScaling, tickRate, pulse_particles, wait_particles, duration, breakDuration);
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData casterData) {
        float level = getSkillLevel(casterData.getEntity(), MKAttributes.EVOCATION);
        ITextComponent damageStr = getDamageDescription(casterData, CoreDamageTypes.FireDamage, base.value(), scale.value(), level, modifierScaling.value());
        return new TranslationTextComponent(getDescriptionTranslationKey(), damageStr,
                NUMBER_FORMATTER.format(convertDurationToSeconds(breakDuration.value())),
                NUMBER_FORMATTER.format(convertDurationToSeconds(tickRate.value())),
                NUMBER_FORMATTER.format(convertDurationToSeconds(duration.value())));
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ENEMY;
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 20.0f;
    }


    @Override
    public void endCast(LivingEntity castingEntity, IMKEntityData casterData, AbilityContext context) {
        super.endCast(castingEntity, casterData, context);
        context.getMemory(MKAbilityMemories.ABILITY_TARGET).ifPresent(targetEntity -> {
            Vector3d linePos = targetEntity.getPositionVec();
            float level = getSkillLevel(castingEntity, MKAttributes.EVOCATION);
            MKEffectBuilder<?> damage = MKAbilityDamageEffect.from(castingEntity, CoreDamageTypes.FireDamage,
                    base.value(), scale.value(), modifierScaling.value())
                    .ability(this)
                    .skillLevel(level);
            MKEffectBuilder<?> fireBreak = ResistanceEffects.BREAK_FIRE.builder(castingEntity)
                    .ability(this)
                    .timed(breakDuration.value())
                    .skillLevel(level);
            LineEffectBuilder lineBuilder = LineEffectBuilder.createOnEntity(castingEntity, targetEntity,
                    linePos.subtract(0.0, 0.1, 0.0),
                    linePos.add(0.0, 4.1, 0.0))
                    .effect(damage, getTargetContext())
                    .effect(fireBreak, getTargetContext())
                    .setParticles(pulse_particles.getValue())
                    .setWaitingParticles(wait_particles.getValue())
                    .duration(duration.value())
                    .waitTime(duration.value() / 2)
                    .tickRate(tickRate.value())
                    .visualTickRate(tickRate.value());
            lineBuilder.spawn();
        });
    }

    @Override
    public Set<MemoryModuleType<?>> getRequiredMemories() {
        return ImmutableSet.of(MKAbilityMemories.ABILITY_TARGET);
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.SINGLE_TARGET;
    }

    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class RegisterMe {
        @SubscribeEvent
        public static void register(RegistryEvent.Register<MKAbility> event) {
            event.getRegistry().register(INSTANCE);
        }
    }
}
