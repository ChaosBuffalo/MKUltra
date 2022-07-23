package com.chaosbuffalo.mkultra.abilities.necromancer;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.abilities.description.AbilityDescriptions;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.IntAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.BurnEffect;
import com.chaosbuffalo.mkultra.effects.EngulfingDarknessEffect;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;

public class EngulfingDarknessAbility extends MKAbility {
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "shadow_bolt_casting");
    public static final ResourceLocation CAST_PARTICLES = new ResourceLocation(MKUltra.MODID, "engulfing_darkness_cast");
    public static final ResourceLocation TICK_PARTICLES = new ResourceLocation(MKUltra.MODID, "engulfing_darkness_tick");
    public static final EngulfingDarknessAbility INSTANCE = new EngulfingDarknessAbility();

    protected final FloatAttribute baseDot = new FloatAttribute("base_dot_damage", 2.0f);
    protected final FloatAttribute scaleDot = new FloatAttribute("scale_dot_damage", 2.0f);
    protected final IntAttribute baseDuration = new IntAttribute("base_duration", 10);
    protected final IntAttribute scaleDuration = new IntAttribute("scale_duration", 1);
    protected final FloatAttribute dotModifierScaling = new FloatAttribute("dot_modifier_scaling", 0.2f);
    protected final ResourceLocationAttribute castParticles = new ResourceLocationAttribute("cast_particles", CAST_PARTICLES);
    protected final ResourceLocationAttribute dotCastParticles = new ResourceLocationAttribute("dot_cast_particles", TICK_PARTICLES);
    protected final IntAttribute shadowbringerDuration = new IntAttribute("shadowbringer_duration", GameConstants.TICKS_PER_SECOND * 10);
    protected final FloatAttribute shadowbringerChance = new FloatAttribute("shadowbringer_chance", 0.02f);


    public EngulfingDarknessAbility() {
        super(MKUltra.MODID, "ability.engulfing_darkness");
        setCooldownSeconds(4);
        setManaCost(5);
        setCastTime((GameConstants.TICKS_PER_SECOND  * 3) / 2);
        addAttributes(baseDuration, scaleDuration, baseDot, scaleDot, dotModifierScaling,
                castParticles, dotCastParticles, shadowbringerChance, shadowbringerDuration);
        addSkillAttribute(MKAttributes.CONJURATION);
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    protected float getShadowbringerChance(IMKEntityData entityData) {
        float skill = getSkillLevel(entityData.getEntity(), MKAttributes.CONJURATION);
        return shadowbringerChance.value() + shadowbringerChance.value() * skill;

    }

    @Override
    public void buildDescription(IMKEntityData casterData, Consumer<ITextComponent> consumer) {
        super.buildDescription(casterData, consumer);
        AbilityDescriptions.getEffectModifiers(EngulfingDarknessEffect.INSTANCE, casterData, false).forEach(consumer);
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        float level = getSkillLevel(entityData.getEntity(), MKAttributes.CONJURATION);
        ITextComponent dotStr = getDamageDescription(entityData,
                CoreDamageTypes.ShadowDamage, baseDot.value(), scaleDot.value(), level, dotModifierScaling.value());
        float dotDur = convertDurationToSeconds(getBuffDuration(entityData, level, baseDuration.value(), scaleDuration.value()));
        float shadowbringerDur = convertDurationToSeconds(shadowbringerDuration.value());
        return new TranslationTextComponent(getDescriptionTranslationKey(),
                dotStr, NUMBER_FORMATTER.format(convertDurationToSeconds(EngulfingDarknessEffect.DEFAULT_PERIOD)),
                NUMBER_FORMATTER.format(dotDur), PERCENT_FORMATTER.format(getShadowbringerChance(entityData)), NUMBER_FORMATTER.format(shadowbringerDur));
    }

    public MKEffectBuilder<?> getDotCast(IMKEntityData casterData, float level) {
        int dur = getBuffDuration(casterData, level, baseDuration.value(), scaleDuration.value());
        return EngulfingDarknessEffect.from(casterData.getEntity(), baseDot.value(), scaleDot.value(),
                dotModifierScaling.value(), getShadowbringerChance(casterData), shadowbringerDuration.value(),
                dotCastParticles.getValue())
                .ability(this)
                .skillLevel(level)
                .timed(dur);
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
        return ModSounds.hostile_casting_shadow;
    }

    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_dark_9;
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        float level = getSkillLevel(entity, MKAttributes.CONJURATION);
        context.getMemory(MKAbilityMemories.ABILITY_TARGET).ifPresent(targetEntity -> {
            MKEffectBuilder<?> dot = getDotCast(data, level)
                    .ability(this)
                    .skillLevel(level);
            MKCore.getEntityData(targetEntity).ifPresent(targetData -> {
                targetData.getEffects().addEffect(dot);
            });

            SoundUtils.serverPlaySoundAtEntity(targetEntity, ModSounds.spell_dark_7, targetEntity.getSoundCategory());
            PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                            new Vector3d(0.0, 1.0, 0.0), castParticles.getValue(),
                            targetEntity.getEntityId()),
                    targetEntity);
        });
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
