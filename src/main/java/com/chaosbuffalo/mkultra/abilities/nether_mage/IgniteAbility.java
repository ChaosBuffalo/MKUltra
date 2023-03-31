package com.chaosbuffalo.mkultra.abilities.nether_mage;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.core.AbilityType;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.instant.MKAbilityDamageEffect;
import com.chaosbuffalo.mkcore.effects.utility.MKParticleEffect;
import com.chaosbuffalo.mkcore.effects.utility.SoundEffect;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.MKUAbilityUtils;
import com.chaosbuffalo.mkultra.effects.IgniteEffect;
import com.chaosbuffalo.mkultra.init.MKUAbilities;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class IgniteAbility extends MKAbility {
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "ignite_casting");
    public static final ResourceLocation CAST_1_PARTICLES = new ResourceLocation(MKUltra.MODID, "ignite_cast_1");
    public static final ResourceLocation CAST_2_PARTICLES = new ResourceLocation(MKUltra.MODID, "ignite_cast_2");
    protected final FloatAttribute base = new FloatAttribute("base", 8.0f);
    protected final FloatAttribute scale = new FloatAttribute("scale", 1.0f);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);
    protected final FloatAttribute igniteDistance = new FloatAttribute("igniteDistance", 5.0f);
    protected final ResourceLocationAttribute cast_1_particles = new ResourceLocationAttribute("cast_1_particles", CAST_1_PARTICLES);
    protected final ResourceLocationAttribute cast_2_particles = new ResourceLocationAttribute("cast_2_particles", CAST_2_PARTICLES);

    public IgniteAbility() {
        super();
        setCooldownSeconds(12);
        setManaCost(6);
        setCastTime(GameConstants.TICKS_PER_SECOND / 4);
        addAttributes(base, scale, modifierScaling, cast_1_particles, cast_2_particles);
        addSkillAttribute(MKAttributes.EVOCATION);
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    @Override
    public AbilityType getType() {
        return AbilityType.Ultimate;
    }

    @Override
    protected Component getAbilityDescription(IMKEntityData entityData) {
        float level = getSkillLevel(entityData.getEntity(), MKAttributes.EVOCATION);
        Component valueStr = getDamageDescription(entityData,
                CoreDamageTypes.FireDamage, base.value(), scale.value(), level, modifierScaling.value());
        return new TranslatableComponent(getDescriptionTranslationKey(), valueStr, igniteDistance.value());
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 15.0f;
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
        return ModSounds.casting_fire;
    }

    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_dark_13;
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        float level = getSkillLevel(entity, MKAttributes.EVOCATION);
        context.getMemory(MKAbilityMemories.ABILITY_TARGET).ifPresent(targetEntity -> {
            MKEffectBuilder<?> damage = MKAbilityDamageEffect.from(entity, CoreDamageTypes.FireDamage,
                    base.value(),
                    scale.value(),
                    modifierScaling.value())
                    .ability(this)
                    .skillLevel(level);

            MKCore.getEntityData(targetEntity).ifPresent(targetData -> {
                targetData.getEffects().addEffect(damage);

                SoundUtils.serverPlaySoundAtEntity(targetEntity, ModSounds.spell_fire_4, targetEntity.getSoundSource());

                if (MKUAbilityUtils.isBurning(targetData)) {
                    MKEffectBuilder<?> ignite = IgniteEffect.from(entity, base.value(), scale.value(), modifierScaling.value())
                            .ability(this)
                            .skillLevel(level);
                    MKEffectBuilder<?> particle = MKParticleEffect.from(entity, cast_2_particles.getValue(),
                            true, new Vec3(0.0, 1.0, 0.0))
                            .ability(this);
                    MKEffectBuilder<?> sound = SoundEffect.from(entity, ModSounds.spell_fire_8, entity.getSoundSource())
                            .ability(this);

                    AreaEffectBuilder.createOnEntity(entity, targetEntity)
                            .effect(particle, getTargetContext())
                            .effect(ignite, getTargetContext())
                            .effect(sound, getTargetContext())
                            .instant()
                            .color(16737305)
                            .radius(igniteDistance.value(), true)
                            .disableParticle()
                            .spawn();

                } else {
                    MKEffectBuilder<?> burn = MKUAbilities.EMBER.get().getBurnCast(data, level)
                            .ability(this);
                    targetData.getEffects().addEffect(burn);

                    PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(new Vec3(0.0, 1.0, 0.0),
                            cast_1_particles.getValue(), targetEntity.getId()), targetEntity);
                }
            });
        });
    }
}
