package com.chaosbuffalo.mkultra.abilities.nether_mage;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.AbilityContext;
import com.chaosbuffalo.mkcore.abilities.AbilityTargetSelector;
import com.chaosbuffalo.mkcore.abilities.AbilityTargeting;
import com.chaosbuffalo.mkcore.abilities.MKAbility;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.utility.MKParticleEffect;
import com.chaosbuffalo.mkcore.effects.utility.SoundEffect;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.serialization.attributes.IntAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.ResistanceEffects;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class FireArmorAbility extends MKAbility {
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "fire_armor_casting");
    public static final ResourceLocation CAST_PARTICLES = new ResourceLocation(MKUltra.MODID, "fire_armor_cast");
    protected final IntAttribute baseDuration = new IntAttribute("baseDuration", 60);
    protected final IntAttribute scaleDuration = new IntAttribute("scaleDuration", 15);
    protected final ResourceLocationAttribute cast_particles = new ResourceLocationAttribute("cast_particles", CAST_PARTICLES);

    public FireArmorAbility() {
        super();
        setCooldownSeconds(150);
        setManaCost(12);
        setCastTime(GameConstants.TICKS_PER_SECOND);
        addSkillAttribute(MKAttributes.ABJURATION);
        addAttributes(baseDuration, scaleDuration, cast_particles);
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 10.0f;
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        float level = getSkillLevel(entityData.getEntity(), MKAttributes.ABJURATION);
        float amount = ResistanceEffects.FIRE_ARMOR.getPerLevel() * (level + 1) * 100.0f;
        int duration = getBuffDuration(entityData, level, baseDuration.value(), scaleDuration.value()) / GameConstants.TICKS_PER_SECOND;
        return new TranslationTextComponent(getDescriptionTranslationKey(), amount,
                CoreDamageTypes.FireDamage.getDisplayName().mergeStyle(CoreDamageTypes.FireDamage.getFormatting()), duration);
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_buff_5;
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        float level = getSkillLevel(entity, MKAttributes.ABJURATION);
        int duration = getBuffDuration(data, level, baseDuration.value(), scaleDuration.value());

        int oldAmp = Math.round(level);
        EffectInstance fireResist = new EffectInstance(Effects.FIRE_RESISTANCE, duration, oldAmp, false, false, true, null);
        EffectInstance absorb = new EffectInstance(Effects.ABSORPTION, duration, oldAmp, false, false, true, null);

        MKEffectBuilder<?> particles = MKParticleEffect.from(entity, cast_particles.getValue(), true, new Vector3d(0.0, 1.0, 0.0))
                .ability(this);

        MKEffectBuilder<?> sound = SoundEffect.from(entity, ModSounds.spell_fire_2, entity.getSoundCategory())
                .ability(this);

        MKEffectBuilder<?> fireArmor = ResistanceEffects.FIRE_ARMOR.builder(entity)
                .ability(this)
                .timed(duration)
                .skillLevel(level);

        AreaEffectBuilder.createOnCaster(entity)
                .effect(fireResist, getTargetContext())
                .effect(absorb, getTargetContext())
                .effect(sound, getTargetContext())
                .effect(fireArmor, getTargetContext())
                .effect(particles, getTargetContext())
                .disableParticle()
                .instant()
                .color(16762905)
                .radius(getDistance(entity), true)
                .spawn();
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.FRIENDLY;
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.PBAOE;
    }
}
