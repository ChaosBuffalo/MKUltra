package com.chaosbuffalo.mkultra.abilities.nether_mage;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.instant.MKAbilityDamageEffect;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.IntAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.BurnEffect;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class EmberAbility extends MKAbility {
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "ember_casting");
    public static final ResourceLocation CAST_PARTICLES = new ResourceLocation(MKUltra.MODID, "ember_cast");
    public static final ResourceLocation BURN_PARTICLES = new ResourceLocation(MKUltra.MODID, "burn_tick");
    protected final FloatAttribute base = new FloatAttribute("base", 8.0f);
    protected final FloatAttribute scale = new FloatAttribute("scale", 3.0f);
    protected final FloatAttribute baseDot = new FloatAttribute("baseBurnDamage", 2.0f);
    protected final FloatAttribute scaleDot = new FloatAttribute("scaleBurnDamage", 1.0f);
    protected final IntAttribute baseDuration = new IntAttribute("baseDuration", 6);
    protected final IntAttribute scaleDuration = new IntAttribute("scaleDuration", 1);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);
    protected final FloatAttribute dotModifierScaling = new FloatAttribute("dotModifierScaling", 0.2f);
    protected final ResourceLocationAttribute cast_particles = new ResourceLocationAttribute("cast_particles", CAST_PARTICLES);
    protected final ResourceLocationAttribute burn_cast_particles = new ResourceLocationAttribute("burn_cast_particles", BURN_PARTICLES);


    public EmberAbility() {
        super();
        setCooldownSeconds(6);
        setManaCost(4);
        setCastTime(GameConstants.TICKS_PER_SECOND / 2);
        addAttributes(base, scale, modifierScaling, baseDuration, scaleDuration, baseDot, scaleDot, dotModifierScaling,
                cast_particles, burn_cast_particles);
        addSkillAttribute(MKAttributes.EVOCATION);
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    @Override
    protected Component getAbilityDescription(IMKEntityData entityData) {
        float level = getSkillLevel(entityData.getEntity(), MKAttributes.EVOCATION);
        Component valueStr = getDamageDescription(entityData,
                CoreDamageTypes.FireDamage, base.value(), scale.value(), level, modifierScaling.value());
        Component dotStr = getDamageDescription(entityData,
                CoreDamageTypes.FireDamage, baseDot.value(), scaleDot.value(), level, dotModifierScaling.value());
        return new TranslatableComponent(getDescriptionTranslationKey(), valueStr,
                NUMBER_FORMATTER.format(convertDurationToSeconds(getBuffDuration(entityData, level, baseDuration.value(), scaleDuration.value()))),
                dotStr, NUMBER_FORMATTER.format(convertDurationToSeconds(BurnEffect.DEFAULT_PERIOD)));
    }

    public MKEffectBuilder<?> getBurnCast(IMKEntityData casterData, float level) {
        int burnTicks = getBuffDuration(casterData, level, baseDuration.value(), scaleDuration.value());
        return BurnEffect.from(casterData.getEntity(), baseDot.value(), scaleDot.value(), dotModifierScaling.value(), burn_cast_particles.getValue())
                .ability(this)
                .skillLevel(level)
                .timed(burnTicks);
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 25.0f;
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ENEMY;
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.SINGLE_TARGET;
    }

    @Override
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.casting_fire.get();
    }

    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_cast_7.get();
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        float level = getSkillLevel(entity, MKAttributes.EVOCATION);
        context.getMemory(MKAbilityMemories.ABILITY_TARGET).ifPresent(targetEntity -> {
            MKEffectBuilder<?> damage = MKAbilityDamageEffect.from(entity, CoreDamageTypes.FireDamage,
                    base.value(), scale.value(), modifierScaling.value())
                    .ability(this)
                    .skillLevel(level);
            MKEffectBuilder<?> burn = getBurnCast(data, level)
                    .ability(this)
                    .skillLevel(level);

            MKCore.getEntityData(targetEntity).ifPresent(targetData -> {
                targetData.getEffects().addEffect(damage);
                targetData.getEffects().addEffect(burn);
            });


            SoundUtils.serverPlaySoundAtEntity(targetEntity, ModSounds.spell_fire_6.get(), targetEntity.getSoundSource());
            PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                            new Vec3(0.0, 1.0, 0.0), cast_particles.getValue(),
                            targetEntity.getId()),
                    targetEntity);
        });
    }
}
