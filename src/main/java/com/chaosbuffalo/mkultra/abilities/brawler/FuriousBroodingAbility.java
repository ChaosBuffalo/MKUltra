package com.chaosbuffalo.mkultra.abilities.brawler;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.abilities.ai.conditions.HealCondition;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.IntAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.FuriousBroodingEffect;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.Nullable;

public class FuriousBroodingAbility extends MKAbility {
    public static final ResourceLocation TICK_PARTICLES = new ResourceLocation(MKUltra.MODID, "furious_brooding_pulse");
    protected final ResourceLocationAttribute tick_particles = new ResourceLocationAttribute("cast_particles", TICK_PARTICLES);
    protected final FloatAttribute baseValue = new FloatAttribute("baseValue", 2.0f);
    protected final FloatAttribute scaleValue = new FloatAttribute("scaleValue", 1.0f);
    protected final IntAttribute baseDuration = new IntAttribute("baseDuration", 6);
    protected final IntAttribute scaleDuration = new IntAttribute("scaleDuration", 5);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);

    public FuriousBroodingAbility() {
        super();
        setCooldownSeconds(18);
        setManaCost(6);
        addAttributes(tick_particles, baseValue, baseDuration, scaleValue, scaleDuration, modifierScaling);
        addSkillAttribute(MKAttributes.PNEUMA);
        setUseCondition(new HealCondition(this, 0.8f).setSelfOnly(true));
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.SELF;
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.SELF;
    }

    @Override
    protected Component getSkillDescription(IMKEntityData casterData) {
        float level = getSkillLevel(casterData.getEntity(), MKAttributes.PNEUMA);
        Component damageStr = getHealDescription(casterData, baseValue.value(),
                scaleValue.value(), level, modifierScaling.value());
        int duration = getBuffDuration(casterData, level, baseDuration.value(), scaleDuration.value()) / GameConstants.TICKS_PER_SECOND;
        float speedReduction = -0.6f + 0.05f * level;
        return new TranslatableComponent(getDescriptionTranslationKey(), damageStr, INTEGER_FORMATTER.format(duration), PERCENT_FORMATTER.format(speedReduction));
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_negative_effect_7.get();
    }

    public MKEffectBuilder<?> createFuriousBroodingEffect(IMKEntityData casterData, float level) {
        int duration = getBuffDuration(casterData, level, baseDuration.value(), scaleDuration.value());
        return FuriousBroodingEffect.from(casterData.getEntity(), baseValue.value(), scaleValue.value(),
                modifierScaling.value(), tick_particles.getValue())
                .ability(this)
                .skillLevel(level)
                .timed(duration);
    }

    @Override
    public void endCast(LivingEntity castingEntity, IMKEntityData casterData, AbilityContext context) {
        super.endCast(castingEntity, casterData, context);
        float level = getSkillLevel(castingEntity, MKAttributes.PNEUMA);
        context.getMemory(MKAbilityMemories.ABILITY_TARGET).ifPresent(targetEntity -> {
            MKEffectBuilder<?> heal = createFuriousBroodingEffect(casterData, level).ability(this);
            MKCore.getEntityData(targetEntity).ifPresent(targetData -> targetData.getEffects().addEffect(heal));
            PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                    new Vec3(0.0, 1.0, 0.0), tick_particles.getValue(),
                    targetEntity.getId()), targetEntity);
        });
    }
}

