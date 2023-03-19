package com.chaosbuffalo.mkultra.abilities.nether_mage;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.abilities.*;
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
import com.chaosbuffalo.mkultra.effects.WarpCurseEffect;
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

public class WarpCurseAbility extends MKAbility {
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "warp_curse_casting");
    public static final ResourceLocation CAST_PARTICLES = new ResourceLocation(MKUltra.MODID, "warp_curse_cast");
    protected final FloatAttribute base = new FloatAttribute("base", 4.0f);
    protected final FloatAttribute scale = new FloatAttribute("scale", 2.0f);
    protected final IntAttribute baseDuration = new IntAttribute("baseDuration", 4);
    protected final IntAttribute scaleDuration = new IntAttribute("scaleDuration", 2);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 0.25f);
    protected final ResourceLocationAttribute cast_particles = new ResourceLocationAttribute("cast_particles", CAST_PARTICLES);

    public WarpCurseAbility() {
        super();
        setCooldownSeconds(16);
        setManaCost(8);
        setCastTime(GameConstants.TICKS_PER_SECOND + 10);
        addAttributes(base, scale, modifierScaling, baseDuration, scaleDuration, cast_particles);
        addSkillAttribute(MKAttributes.ALTERATON);
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        float level = getSkillLevel(entityData.getEntity(), MKAttributes.ALTERATON);
        int duration = getBuffDuration(entityData, level, baseDuration.value(), scaleDuration.value());
        ITextComponent valueStr = getDamageDescription(entityData,
                CoreDamageTypes.ShadowDamage, base.value(), scale.value(), level, modifierScaling.value());
        return new TranslationTextComponent(getDescriptionTranslationKey(), valueStr,
                WarpCurseEffect.DEFAULT_PERIOD / 20,
                duration / 20);
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
        return ModSounds.casting_shadow;
    }

    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_dark_15;
    }

    @Override
    public void endCast(LivingEntity castingEntity, IMKEntityData casterData, AbilityContext context) {
        super.endCast(castingEntity, casterData, context);
        context.getMemory(MKAbilityMemories.ABILITY_TARGET).ifPresent(targetEntity -> {
            float level = getSkillLevel(castingEntity, MKAttributes.ALTERATON);
            int duration = getBuffDuration(casterData, level, baseDuration.value(), scaleDuration.value());
            MKEffectBuilder<?> warpCast = WarpCurseEffect.from(castingEntity, base.value(), scale.value(),
                    modifierScaling.value(), cast_particles.getValue())
                    .ability(this)
                    .timed(duration)
                    .skillLevel(level);

            int oldAmp = Math.round(level);
            MKCore.getEntityData(targetEntity).ifPresent(targetData -> targetData.getEffects().addEffect(warpCast));
            targetEntity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, duration, oldAmp, false, false, true, null));

            SoundUtils.serverPlaySoundAtEntity(targetEntity, ModSounds.spell_fire_5, targetEntity.getSoundCategory());
            PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                    new Vector3d(0.0, 1.0, 0.0), cast_particles.getValue(), castingEntity.getEntityId()), castingEntity);
        });
    }
}
