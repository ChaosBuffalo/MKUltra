package com.chaosbuffalo.mkultra.abilities.green_knight;

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
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.NaturesRemedyEffect;
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

public class NaturesRemedyAbility extends MKAbility {
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "natures_remedy_casting");
    public static final ResourceLocation CAST_PARTICLES = new ResourceLocation(MKUltra.MODID, "natures_remedy_cast");
    public static final ResourceLocation TICK_PARTICLES = new ResourceLocation(MKUltra.MODID, "natures_remedy_tick");
    protected final FloatAttribute baseValue = new FloatAttribute("baseValue", 2.0f);
    protected final FloatAttribute scaleValue = new FloatAttribute("scaleValue", 1.0f);
    protected final IntAttribute baseDuration = new IntAttribute("baseDuration", 4);
    protected final IntAttribute scaleDuration = new IntAttribute("scaleDuration", 1);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);
    protected final ResourceLocationAttribute cast_particles = new ResourceLocationAttribute("cast_particles", CAST_PARTICLES);
    protected final ResourceLocationAttribute tick_particles = new ResourceLocationAttribute("tick_particles", TICK_PARTICLES);

    public NaturesRemedyAbility() {
        super();
        setCooldownSeconds(10);
        setManaCost(4);
        setCastTime(GameConstants.TICKS_PER_SECOND / 2);
        addSkillAttribute(MKAttributes.RESTORATION);
        addAttributes(baseValue, scaleValue, baseDuration, scaleDuration, modifierScaling, cast_particles, tick_particles);
        setUseCondition(new HealCondition(this, .75f));
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.FRIENDLY;
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 10.0f;
    }

    @Override
    protected Component getAbilityDescription(IMKEntityData entityData) {
        float level = getSkillLevel(entityData.getEntity(), MKAttributes.RESTORATION);
        Component damageStr = getHealDescription(entityData, baseValue.value(),
                scaleValue.value(), level, modifierScaling.value());
        int duration = getBuffDuration(entityData, level, baseDuration.value(), scaleDuration.value()) / GameConstants.TICKS_PER_SECOND;
        return new TranslatableComponent(getDescriptionTranslationKey(), damageStr, duration);
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_cast_5.get();
    }

    public MKEffectBuilder<?> createNaturesRemedyEffect(IMKEntityData casterData, float level) {
        int duration = getBuffDuration(casterData, level, baseDuration.value(), scaleDuration.value());
        return NaturesRemedyEffect.from(casterData.getEntity(), baseValue.value(), scaleValue.value(),
                modifierScaling.value(), tick_particles.getValue())
                .ability(this)
                .skillLevel(level)
                .timed(duration);
    }

    @Override
    public void endCast(LivingEntity castingEntity, IMKEntityData casterData, AbilityContext context) {
        super.endCast(castingEntity, casterData, context);
        float level = getSkillLevel(castingEntity, MKAttributes.RESTORATION);
        context.getMemory(MKAbilityMemories.ABILITY_TARGET).ifPresent(targetEntity -> {
            MKEffectBuilder<?> heal = createNaturesRemedyEffect(casterData, level).ability(this);

            MKCore.getEntityData(targetEntity).ifPresent(targetData -> targetData.getEffects().addEffect(heal));

            SoundUtils.serverPlaySoundAtEntity(targetEntity, ModSounds.spell_heal_8.get(), targetEntity.getSoundSource());
            PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                    new Vec3(0.0, 1.0, 0.0), cast_particles.getValue(),
                    targetEntity.getId()), targetEntity);
        });
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.SINGLE_TARGET_OR_SELF;
    }
}
