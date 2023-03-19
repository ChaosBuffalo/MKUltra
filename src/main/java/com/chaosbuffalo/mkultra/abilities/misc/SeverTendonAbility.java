package com.chaosbuffalo.mkultra.abilities.misc;

import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.instant.MKAbilityDamageEffect;
import com.chaosbuffalo.mkcore.fx.ParticleEffects;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.network.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.IntAttribute;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.effects.SeverTendonEffect;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.mkweapons.init.MKWeaponsParticles;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class SeverTendonAbility extends MKAbility {
    protected final FloatAttribute base = new FloatAttribute("base", 4.0f);
    protected final FloatAttribute scale = new FloatAttribute("scale", 2.0f);
    protected final FloatAttribute baseDot = new FloatAttribute("baseBleedDamage", 1.0f);
    protected final FloatAttribute scaleDot = new FloatAttribute("scaleBleedDamage", 1.0f);
    protected final IntAttribute baseDuration = new IntAttribute("baseDuration", 4);
    protected final IntAttribute scaleDuration = new IntAttribute("scaleDuration", 1);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);
    protected final FloatAttribute dotModifierScaling = new FloatAttribute("bleedModifierScaling", 0.1f);

    public SeverTendonAbility() {
        super();
        setCooldownSeconds(12);
        setManaCost(5);
        setCastTime(0);
        addAttributes(base, scale, modifierScaling, baseDuration, scaleDuration, baseDot, scaleDot, dotModifierScaling);
        addSkillAttribute(MKAttributes.PANKRATION);
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        float level = getSkillLevel(entityData.getEntity(), MKAttributes.PANKRATION);
        ITextComponent valueStr = getDamageDescription(entityData,
                CoreDamageTypes.MeleeDamage, base.value(), scale.value(), level, modifierScaling.value());
        ITextComponent dotStr = getDamageDescription(entityData,
                CoreDamageTypes.BleedDamage, baseDot.value(), scaleDot.value(), level, dotModifierScaling.value());
        int periodSeconds = SeverTendonEffect.DEFAULT_PERIOD / 20;
        return new TranslationTextComponent(getDescriptionTranslationKey(), valueStr,
                getBuffDuration(entityData, level, baseDuration.value(), scaleDuration.value()) / 20,
                dotStr, periodSeconds, (level + 1) * .05f * 100.0f);
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return getMeleeReach(entity);
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
        return null;
    }

    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_magic_whoosh_4;
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        float level = getSkillLevel(entity, MKAttributes.PANKRATION);
        context.getMemory(MKAbilityMemories.ABILITY_TARGET).ifPresent(targetEntity -> {
            MKEffectBuilder<?> damage = MKAbilityDamageEffect.from(entity, CoreDamageTypes.MeleeDamage,
                    base.value(), scale.value(), modifierScaling.value())
                    .ability(this)
                    .skillLevel(level);


            int dur = getBuffDuration(data, level, baseDuration.value(), scaleDuration.value());
            MKEffectBuilder<?> severTendon = SeverTendonEffect.from(entity, baseDot.value(), scaleDot.value(), dotModifierScaling.value())
                    .ability(this)
                    .timed(dur)
                    .skillLevel(level);

            MKCore.getEntityData(targetEntity).ifPresent(targetData -> {
                targetData.getEffects().addEffect(damage);
                targetData.getEffects().addEffect(severTendon);
            });

            SoundUtils.serverPlaySoundAtEntity(targetEntity, ModSounds.spell_punch_6, targetEntity.getSoundCategory());
            Vector3d lookVec = entity.getLookVec();
            PacketHandler.sendToTrackingAndSelf(
                    new ParticleEffectSpawnPacket(
                            MKWeaponsParticles.DRIPPING_BLOOD,
                            ParticleEffects.CIRCLE_MOTION, 25, 10,
                            targetEntity.getPosX(), targetEntity.getPosY() + 1.0f,
                            targetEntity.getPosZ(), 0.75, 0.75, 0.75, 1.0,
                            lookVec), targetEntity);
        });
    }
}
