package com.chaosbuffalo.mkultra.abilities.misc;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.EntityEffectBuilder;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.instant.MKAbilityDamageEffect;
import com.chaosbuffalo.mkcore.effects.utility.SoundEffect;
import com.chaosbuffalo.mkcore.entities.BaseEffectEntity;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.IntAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkcore.utils.TargetUtil;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.ResistanceEffects;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Set;

public class WrathBeamAbility extends MKAbility {
    private static final ResourceLocation PULSE_PARTICLES = new ResourceLocation(MKUltra.MODID, "wrath_beam_pulse");
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "flame_wave_casting");
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
        setCastTime(GameConstants.TICKS_PER_SECOND);
        setCooldownSeconds(5);
        setManaCost(6);
        addSkillAttribute(MKAttributes.EVOCATION);
        casting_particles.setDefaultValue(CASTING_PARTICLES);
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

    @Nullable
    @Override
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.hostile_casting_fire;
    }

    public void castWrathBeam(LivingEntity castingEntity, Vector3d position){
        float level = getSkillLevel(castingEntity, MKAttributes.EVOCATION);
        MKEffectBuilder<?> damage = MKAbilityDamageEffect.from(castingEntity, CoreDamageTypes.FireDamage,
                base.value(), scale.value(), modifierScaling.value())
                .ability(this)
                .skillLevel(level);
        MKEffectBuilder<?> fireBreak = ResistanceEffects.BREAK_FIRE.builder(castingEntity)
                .ability(this)
                .timed(breakDuration.value())
                .skillLevel(level);
        MKEffectBuilder<?> sound = SoundEffect.from(castingEntity, ModSounds.spell_fire_7, castingEntity.getSoundCategory())
                .ability(this);
        EntityEffectBuilder.LineEffectBuilder lineBuilder = EntityEffectBuilder.createLineEffect(castingEntity,
                position.subtract(0.0, 0.1, 0.0),
                position.add(0.0, 4.1, 0.0));
        lineBuilder.effect(damage, getTargetContext())
                .effect(fireBreak, getTargetContext())
                .effect(sound, getTargetContext())
                .setParticles(new BaseEffectEntity.ParticleDisplay(pulse_particles.getValue(), tickRate.value(), BaseEffectEntity.ParticleDisplay.DisplayType.CONTINUOUS))
                .setWaitingParticles(new BaseEffectEntity.ParticleDisplay(wait_particles.getValue(), tickRate.value(), BaseEffectEntity.ParticleDisplay.DisplayType.CONTINUOUS))
                .duration(duration.value())
                .waitTime(duration.value() / 2)
                .tickRate(tickRate.value());
        SoundUtils.serverPlaySoundFromEntity(position.getX(), position.getY(), position.getZ(),
                ModSounds.spell_dark_13, castingEntity.getSoundCategory(), 1.0f, 1.0f, castingEntity);
        lineBuilder.spawn();
    }

    @Override
    public void endCast(LivingEntity castingEntity, IMKEntityData casterData, AbilityContext context) {
        super.endCast(castingEntity, casterData, context);
        context.getMemory(MKAbilityMemories.ABILITY_POSITION_TARGET)
                .flatMap(TargetUtil.LivingOrPosition::getPosition).ifPresent(x -> castWrathBeam(castingEntity, x));
    }

    @Override
    public Set<MemoryModuleType<?>> getRequiredMemories() {
        return ImmutableSet.of(MKAbilityMemories.ABILITY_POSITION_TARGET);
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.POSITION_INCLUDE_ENTITIES;
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
