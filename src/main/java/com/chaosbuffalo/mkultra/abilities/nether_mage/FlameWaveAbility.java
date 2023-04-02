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
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.IntAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.FlameWaveEffect;
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

public class FlameWaveAbility extends MKAbility {
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "flame_wave_casting");
    public static final ResourceLocation CAST_1_PARTICLES = new ResourceLocation(MKUltra.MODID, "flame_wave_cast_1");
    public static final ResourceLocation CAST_2_PARTICLES = new ResourceLocation(MKUltra.MODID, "flame_wave_cast_2");
    protected final FloatAttribute base = new FloatAttribute("base", 6.0f);
    protected final FloatAttribute scale = new FloatAttribute("scale", 3.0f);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);
    protected final IntAttribute baseDuration = new IntAttribute("baseDuration", 3);
    protected final IntAttribute scaleDuration = new IntAttribute("scaleDuration", 1);
    protected final FloatAttribute damageBoost = new FloatAttribute("damageBoost", 1.5f);
    protected final ResourceLocationAttribute cast_1_particles = new ResourceLocationAttribute("cast_1_particles", CAST_1_PARTICLES);
    protected final ResourceLocationAttribute cast_2_particles = new ResourceLocationAttribute("cast_2_particles", CAST_2_PARTICLES);


    public FlameWaveAbility() {
        super();
        setCooldownSeconds(14);
        setManaCost(6);
        setCastTime(GameConstants.TICKS_PER_SECOND / 2);
        addAttributes(base, scale, modifierScaling, baseDuration, scaleDuration, damageBoost, cast_1_particles, cast_2_particles);
        addSkillAttribute(MKAttributes.EVOCATION);
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    @Override
    protected Component getAbilityDescription(IMKEntityData entityData) {
        float level = getSkillLevel(entityData.getEntity(), MKAttributes.EVOCATION);
        Component dmgStr = getDamageDescription(entityData, CoreDamageTypes.FireDamage, base.value(), scale.value(),
                level, modifierScaling.value());
        int dur = Math.round(baseDuration.value() + scaleDuration.value() * level);
        float mult = damageBoost.value() * 100.0f;
        return new TranslatableComponent(getDescriptionTranslationKey(), dmgStr, mult, dur);
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 5.0f + 2.0f * getSkillLevel(entity, MKAttributes.EVOCATION);
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ENEMY;
    }


    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.PBAOE;
    }

    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_fire_7.get();
    }

    @Nullable
    @Override
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.casting_fire.get();
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        float level = getSkillLevel(entity, MKAttributes.EVOCATION);

        MKEffectBuilder<?> flames = FlameWaveEffect.from(entity, base.value(), scale.value(), modifierScaling.value(),
                baseDuration.value(), scaleDuration.value(), damageBoost.value())
                .ability(this)
                .skillLevel(level);

        MKEffectBuilder<?> particles = MKParticleEffect.from(entity, cast_2_particles.getValue(), false, new Vec3(0.0, 1.0, 0.0))
                .ability(this);

        MKEffectBuilder<?> sound = SoundEffect.from(entity, ModSounds.spell_fire_1.get(), entity.getSoundSource())
                .ability(this);

        AreaEffectBuilder.createOnCaster(entity)
                .effect(flames, getTargetContext())
                .effect(particles, getTargetContext())
                .effect(sound, getTargetContext())
                .instant()
                .color(16737305)
                .radius(getDistance(entity), true)
                .disableParticle()
                .spawn();

        PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                        new Vec3(0.0, 1.0, 0.0), cast_1_particles.getValue(), entity.getId()),
                entity);
    }
}
