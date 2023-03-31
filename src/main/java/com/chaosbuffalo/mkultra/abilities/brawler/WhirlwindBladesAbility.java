package com.chaosbuffalo.mkultra.abilities.brawler;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.AbilityContext;
import com.chaosbuffalo.mkcore.abilities.AbilityTargetSelector;
import com.chaosbuffalo.mkcore.abilities.AbilityTargeting;
import com.chaosbuffalo.mkcore.abilities.MKAbility;
import com.chaosbuffalo.mkcore.abilities.ai.conditions.MeleeUseCondition;
import com.chaosbuffalo.mkcore.core.AbilityType;
import com.chaosbuffalo.mkcore.core.CastInterruptReason;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.instant.MKAbilityDamageEffect;
import com.chaosbuffalo.mkcore.effects.utility.MKParticleEffect;
import com.chaosbuffalo.mkcore.effects.utility.SoundEffect;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkultra.MKUltra;
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


public class WhirlwindBladesAbility extends MKAbility {
    public static final ResourceLocation CAST_PARTICLES = new ResourceLocation(MKUltra.MODID, "whirlwind_blades_pulse");
    protected final ResourceLocationAttribute cast_particles = new ResourceLocationAttribute("cast_particles", CAST_PARTICLES);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);
    protected final FloatAttribute base = new FloatAttribute("base", 2.0f);
    protected final FloatAttribute scale = new FloatAttribute("scale", 1.0f);
    protected final FloatAttribute perTick = new FloatAttribute("perTick", 0.15f);

    public WhirlwindBladesAbility() {
        super();
        addAttributes(cast_particles, base, scale, modifierScaling, perTick);
        setCastTime(GameConstants.TICKS_PER_SECOND * 3);
        setCooldownSeconds(20);
        setManaCost(6);
        addSkillAttribute(MKAttributes.PANKRATION);
        setUseCondition(new MeleeUseCondition(this));
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
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.PBAOE;
    }

    @Override
    protected Component getAbilityDescription(IMKEntityData entityData) {
        float level = getSkillLevel(entityData.getEntity(), MKAttributes.PANKRATION);
        Component baseDamage = getDamageDescription(entityData,
                CoreDamageTypes.MeleeDamage, base.value(), scale.value(), level, 0.0f);
        float periodSeconds = 6.0f / GameConstants.TICKS_PER_SECOND;
        int castSeconds = getCastTime(entityData) / GameConstants.TICKS_PER_SECOND;
        int numberOfCasts = Math.round(castSeconds / periodSeconds);
        Component maxDamage = getDamageDescription(entityData,
                CoreDamageTypes.MeleeDamage, base.value(), scale.value(), level,
                modifierScaling.value() * numberOfCasts * perTick.value());
        return new TranslatableComponent(getDescriptionTranslationKey(), NUMBER_FORMATTER.format(periodSeconds), INTEGER_FORMATTER.format(castSeconds),
                PERCENT_FORMATTER.format(perTick.value()), baseDamage, maxDamage);
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ENEMY;
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return getMeleeReach(entity);
    }

    @Override
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.spell_whirlwind_1;
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return null;
    }

    @Override
    public void continueCast(LivingEntity castingEntity, IMKEntityData casterData, int castTimeLeft, AbilityContext context) {
        super.continueCast(castingEntity, casterData, castTimeLeft, context);
        int tickSpeed = 6;
        if (castTimeLeft % tickSpeed == 0) {
            float level = getSkillLevel(castingEntity, MKAttributes.PANKRATION);
            int totalDuration = getCastTime(casterData);
            int count = (totalDuration - castTimeLeft) / tickSpeed;
            float baseAmount = perTick.value();
            float scaling = count * baseAmount;

            MKEffectBuilder<?> damage = MKAbilityDamageEffect.from(castingEntity, CoreDamageTypes.MeleeDamage,
                    base.value(), scale.value(), modifierScaling.value() * scaling)
                    .ability(this)
                    .skillLevel(level);
            MKEffectBuilder<?> particles = MKParticleEffect.from(castingEntity,
                    cast_particles.getValue(), true, new Vec3(0.0, 1.0, 0.0))
                    .ability(this);
            MKEffectBuilder<?> sound = SoundEffect.from(castingEntity, ModSounds.spell_shadow_2, castingEntity.getSoundSource())
                    .ability(this);

            AreaEffectBuilder.createOnCaster(castingEntity)
                    .effect(damage, getTargetContext())
                    .effect(particles, TargetingContexts.SELF)
                    .effect(sound, getTargetContext())
                    .instant()
                    .color(16409620)
                    .radius(getDistance(castingEntity), true)
                    .disableParticle()
                    .spawn();
        }
    }
}