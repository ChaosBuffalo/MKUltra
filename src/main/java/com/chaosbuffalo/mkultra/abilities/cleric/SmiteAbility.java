package com.chaosbuffalo.mkultra.abilities.cleric;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.instant.MKAbilityDamageEffect;
import com.chaosbuffalo.mkcore.effects.status.StunEffect;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SmiteAbility extends MKAbility {
    protected final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "smite_casting");
    protected final ResourceLocation CAST_PARTICLES = new ResourceLocation(MKUltra.MODID, "smite_cast");
    protected final FloatAttribute base = new FloatAttribute("base", 5.0f);
    protected final FloatAttribute scale = new FloatAttribute("scale", 5.0f);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);
    protected final ResourceLocationAttribute cast_particles = new ResourceLocationAttribute("cast_particles", CAST_PARTICLES);

    public SmiteAbility() {
        super();
        setCooldownSeconds(6);
        setManaCost(5);
        setCastTime(GameConstants.TICKS_PER_SECOND);
        addAttributes(base, scale, modifierScaling, cast_particles);
        addSkillAttribute(MKAttributes.EVOCATION);
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        float level = getSkillLevel(entityData.getEntity(), MKAttributes.EVOCATION);
        ITextComponent valueStr = getDamageDescription(entityData,
                CoreDamageTypes.HolyDamage, base.value(), scale.value(), level, modifierScaling.value());
        return new TranslationTextComponent(getDescriptionTranslationKey(), valueStr,
                getBuffDuration(entityData, level, 0, 1) / 20);
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
        return ModSounds.casting_shadow;
    }

    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_magic_whoosh_2;
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        float level = getSkillLevel(entity, MKAttributes.EVOCATION);
        context.getMemory(MKAbilityMemories.ABILITY_TARGET).ifPresent(targetEntity -> {

            MKEffectBuilder<?> damage = MKAbilityDamageEffect.from(entity, CoreDamageTypes.HolyDamage,
                    base.value(), scale.value(), modifierScaling.value())
                    .ability(this)
                    .skillLevel(level);

            MKEffectBuilder<?> stun = StunEffect.from(entity)
                    .ability(this)
                    .timed(Math.round(GameConstants.TICKS_PER_SECOND * (level + 1.0f)))
                    .skillLevel(level);

            MKCore.getEntityData(targetEntity).ifPresent(targetData -> {
                targetData.getEffects().addEffect(damage);
                targetData.getEffects().addEffect(stun);
            });
            SoundUtils.serverPlaySoundAtEntity(targetEntity, ModSounds.spell_holy_2, targetEntity.getSoundCategory());
            PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                    new Vector3d(0.0, 1.0, 0.0), cast_particles.getValue(), targetEntity.getEntityId()), targetEntity);
        });
    }
}
