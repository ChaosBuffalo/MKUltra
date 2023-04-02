package com.chaosbuffalo.mkultra.abilities.green_knight;

import com.chaosbuffalo.mkcore.abilities.AbilityTargetSelector;
import com.chaosbuffalo.mkcore.abilities.AbilityTargeting;
import com.chaosbuffalo.mkcore.abilities.MKToggleAbility;
import com.chaosbuffalo.mkcore.abilities.ai.conditions.NeedsBuffCondition;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.SkinLikeWoodEffect;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class SkinLikeWoodAbility extends MKToggleAbility {
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "skin_like_wood_casting");
    public static final ResourceLocation CAST_PARTICLES = new ResourceLocation(MKUltra.MODID, "skin_like_wood_cast");
    protected final ResourceLocationAttribute cast_particles = new ResourceLocationAttribute("cast_particles", CAST_PARTICLES);

    public SkinLikeWoodAbility() {
        super();
        setCooldownSeconds(3);
        setManaCost(2);
        addAttributes(cast_particles);
        addSkillAttribute(MKAttributes.ABJURATION);
        setUseCondition(new NeedsBuffCondition(this, SkinLikeWoodEffect.INSTANCE).setSelfOnly(true));
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.SELF;
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.SELF;
    }

    @Override
    public MKEffect getToggleEffect() {
        return SkinLikeWoodEffect.INSTANCE;
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return null;
    }

    @Override
    public void applyEffect(LivingEntity entity, IMKEntityData entityData) {
        super.applyEffect(entity, entityData);
        float level = getSkillLevel(entity, MKAttributes.ABJURATION);
        SoundUtils.serverPlaySoundAtEntity(entity, ModSounds.spell_earth_7.get(), entity.getSoundSource());

        MKEffectBuilder<?> instance = getToggleEffect().builder(entity)
                .ability(this)
                .skillLevel(level)
                .infinite();
        entityData.getEffects().addEffect(instance);

        PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                new Vec3(0.0, 1.0, 0.0), cast_particles.getValue(),
                entity.getId()), entity);
    }
}
