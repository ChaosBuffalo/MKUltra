package com.chaosbuffalo.mkultra.abilities.cleric;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.AbilityContext;
import com.chaosbuffalo.mkcore.abilities.AbilityTargetSelector;
import com.chaosbuffalo.mkcore.abilities.AbilityTargeting;
import com.chaosbuffalo.mkcore.abilities.MKAbility;
import com.chaosbuffalo.mkcore.core.AbilityType;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.utility.MKParticleEffect;
import com.chaosbuffalo.mkcore.effects.utility.SoundEffect;
import com.chaosbuffalo.mkcore.serialization.attributes.IntAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkultra.MKUltra;
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

public class InspireAbility extends MKAbility {
    protected final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "inspire_casting");
    protected final ResourceLocation CAST_PARTICLES = new ResourceLocation(MKUltra.MODID, "inspire_cast");
    protected final IntAttribute base = new IntAttribute("baseDuration", 8);
    protected final IntAttribute scale = new IntAttribute("scaleDuration", 2);
    protected final ResourceLocationAttribute cast_particles = new ResourceLocationAttribute("cast_particles", CAST_PARTICLES);

    public InspireAbility() {
        super();
        setCooldownSeconds(35);
        setManaCost(8);
        setCastTime(GameConstants.TICKS_PER_SECOND * 2);
        addAttributes(base, scale, cast_particles);
        addSkillAttribute(MKAttributes.ALTERATON);
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        float level = getSkillLevel(entityData.getEntity(), MKAttributes.ALTERATON);
        int duration = getBuffDuration(entityData, level, base.value(), scale.value()) / GameConstants.TICKS_PER_SECOND;
        return new TranslationTextComponent(getDescriptionTranslationKey(), duration);
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 20.0f;
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.FRIENDLY;
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.PBAOE;
    }

    @Override
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.casting_holy;
    }

    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_cast_12;
    }

    @Override
    public AbilityType getType() {
        return AbilityType.Ultimate;
    }

    @Override
    public void endCast(LivingEntity castingEntity, IMKEntityData casterData, AbilityContext context) {
        super.endCast(castingEntity, casterData, context);
        float level = getSkillLevel(castingEntity, MKAttributes.ALTERATON);
        int duration = getBuffDuration(casterData, level, base.value(), scale.value());
        int oldAmp = Math.round(level);

        EffectInstance hasteEffect = new EffectInstance(Effects.HASTE, duration, oldAmp, false, false);
        EffectInstance regenEffect = new EffectInstance(Effects.REGENERATION, duration, oldAmp, false, false);
        MKEffectBuilder<?> sound = SoundEffect.from(castingEntity, ModSounds.spell_holy_8, castingEntity.getSoundCategory())
                .ability(this);
        MKEffectBuilder<?> particles = MKParticleEffect.from(castingEntity, cast_particles.getValue(), true, new Vector3d(0.0, 1.0, 0.0))
                .ability(this);

        AreaEffectBuilder.createOnCaster(castingEntity)
                .effect(hasteEffect, getTargetContext())
                .effect(regenEffect, getTargetContext())
                .effect(sound, getTargetContext())
                .effect(particles, getTargetContext())
                .instant()
                .color(1034415)
                .radius(getDistance(castingEntity), true)
                .disableParticle()
                .spawn();
    }
}
