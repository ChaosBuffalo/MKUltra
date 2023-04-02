package com.chaosbuffalo.mkultra.abilities.necromancer;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.core.AbilityType;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.VampiricDamageEffect;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

public class LifeSpikeAbility extends MKAbility {
    protected final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "lifespike_casting");
    protected final ResourceLocation CAST_PARTICLES = new ResourceLocation(MKUltra.MODID, "lifespike_cast");
    protected final FloatAttribute base = new FloatAttribute("base", 10.0f);
    protected final FloatAttribute scale = new FloatAttribute("scale", 2.0f);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);
    protected final FloatAttribute healScaling = new FloatAttribute("healScaling", 1.0f);
    protected final FloatAttribute healModScaling = new FloatAttribute("healModScaling", 0.5f);
    protected final ResourceLocationAttribute cast_particles = new ResourceLocationAttribute("cast_particles", CAST_PARTICLES);

    public LifeSpikeAbility() {
        super();
        setCooldownSeconds(30);
        setManaCost(10);
        setCastTime(GameConstants.TICKS_PER_SECOND * 2);
        addAttributes(base, scale, modifierScaling, cast_particles, healScaling, healModScaling);
        addSkillAttribute(MKAttributes.NECROMANCY);
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    @Override
    public AbilityType getType() {
        return AbilityType.Ultimate;
    }

    @Override
    protected Component getAbilityDescription(IMKEntityData entityData) {
        float level = getSkillLevel(entityData.getEntity(), MKAttributes.NECROMANCY);
        Component valueStr = getDamageDescription(entityData,
                CoreDamageTypes.ShadowDamage, base.value(), scale.value(), level, modifierScaling.value());

        float bonus = entityData.getStats().getDamageTypeBonus(CoreDamageTypes.ShadowDamage) * modifierScaling.value();
        float healing = (base.value() + scale.value() * level + bonus) * healScaling.value();
        Component healStr = getHealDescription(entityData, healing, 0.0f, 0.0f, healModScaling.value());
        return new TranslatableComponent(getDescriptionTranslationKey(), valueStr, healStr);
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
        return ModSounds.casting_shadow.get();
    }

    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_magic_whoosh_4.get();
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        float level = getSkillLevel(entity, MKAttributes.EVOCATION);
        context.getMemory(MKAbilityMemories.ABILITY_TARGET).ifPresent(targetEntity -> {

            MKEffectBuilder<?> damage = VampiricDamageEffect.from(entity, CoreDamageTypes.ShadowDamage,
                    base.value(), scale.value(), modifierScaling.value(), healScaling.value(), healModScaling.value())
                    .ability(this)
                    .skillLevel(level);


            MKCore.getEntityData(targetEntity).ifPresent(targetData -> {
                targetData.getEffects().addEffect(damage);
            });

            SoundUtils.serverPlaySoundAtEntity(targetEntity, ModSounds.spell_shadow_6.get(), targetEntity.getSoundSource());
            PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                    new Vec3(0.0, 1.75, 0.0), cast_particles.getValue(), targetEntity.getId()), targetEntity);
        });
    }
}
