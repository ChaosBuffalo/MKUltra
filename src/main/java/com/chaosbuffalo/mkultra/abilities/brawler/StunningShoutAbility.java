package com.chaosbuffalo.mkultra.abilities.brawler;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.abilities.AbilityContext;
import com.chaosbuffalo.mkcore.abilities.AbilityTargetSelector;
import com.chaosbuffalo.mkcore.abilities.AbilityTargeting;
import com.chaosbuffalo.mkcore.abilities.MKAbility;
import com.chaosbuffalo.mkcore.core.AbilityType;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.instant.MKAbilityDamageEffect;
import com.chaosbuffalo.mkcore.effects.status.StunEffect;
import com.chaosbuffalo.mkcore.effects.utility.MKParticleEffect;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.IntAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkcore.utils.TargetUtil;
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
import java.util.List;

public class StunningShoutAbility extends MKAbility {
    public static final ResourceLocation TICK_PARTICLES = new ResourceLocation(MKUltra.MODID, "stunning_shout_tick");
    public static final ResourceLocation CAST_PARTICLES = new ResourceLocation(MKUltra.MODID, "stunning_shout_cast");
    protected final FloatAttribute baseDamage = new FloatAttribute("baseDamage", 4.0f);
    protected final FloatAttribute scaleDamage = new FloatAttribute("scaleDamage", 2.0f);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);
    protected final ResourceLocationAttribute cast_particles = new ResourceLocationAttribute("cast_particles", CAST_PARTICLES);
    protected final ResourceLocationAttribute tick_particles = new ResourceLocationAttribute("tick_particles", TICK_PARTICLES);
    protected final IntAttribute baseDuration = new IntAttribute("baseDuration", 1);
    protected final IntAttribute scaleDuration = new IntAttribute("scaleDuration", 1);

    public StunningShoutAbility() {
        super();
        setCooldownSeconds(12);
        setManaCost(4);
        addAttributes(baseDamage, scaleDamage, baseDuration, scaleDuration, cast_particles, tick_particles);
        addSkillAttribute(MKAttributes.PNEUMA);
    }

    @Override
    public AbilityType getType() {
        return AbilityType.Basic;
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ENEMY;
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.LINE;
    }

    @Override
    protected Component getAbilityDescription(IMKEntityData entityData) {
        float level = getSkillLevel(entityData.getEntity(), MKAttributes.PNEUMA);
        Component damageStr = getDamageDescription(entityData, CoreDamageTypes.BleedDamage, baseDamage.value(),
                scaleDamage.value(), level, modifierScaling.value());
        int dur = getBuffDuration(entityData, level, baseDuration.value(), scaleDuration.value()) / GameConstants.TICKS_PER_SECOND;
        return new TranslatableComponent(getDescriptionTranslationKey(), INTEGER_FORMATTER.format(dur), damageStr);
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 10.0f;
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_shout_1.get();
    }

    @Override
    public void endCast(LivingEntity castingEntity, IMKEntityData casterData, AbilityContext context) {
        super.endCast(castingEntity, casterData, context);
        float level = getSkillLevel(castingEntity, MKAttributes.PNEUMA);


        MKEffectBuilder<?> damage = MKAbilityDamageEffect.from(castingEntity, CoreDamageTypes.BleedDamage,
                baseDamage.value(), scaleDamage.value(), modifierScaling.value())
                .skillLevel(level)
                .ability(this);
        MKEffectBuilder<?> stun = StunEffect.from(castingEntity).ability(this).skillLevel(level).timed(
                getBuffDuration(casterData, level, baseDuration.value(), scaleDuration.value()));
        MKEffectBuilder<?> particles = MKParticleEffect.from(castingEntity, tick_particles.getValue(),
                false, new Vec3(0.0, 1.5, 0.0))
                .ability(this);

        Vec3 look = castingEntity.getLookAngle().scale(getDistance(castingEntity));
        Vec3 from = castingEntity.position().add(0, castingEntity.getEyeHeight(), 0);
        Vec3 to = from.add(look);
        List<LivingEntity> entityHit = TargetUtil.getTargetsInLine(castingEntity, from, to, 1.0f, this::isValidTarget);

        for (LivingEntity entHit : entityHit) {
            MKCore.getEntityData(entHit).ifPresent(targetData -> {
                targetData.getEffects().addEffect(damage);
                targetData.getEffects().addEffect(stun);
                targetData.getEffects().addEffect(particles);
            });
        }

        MKParticleEffectSpawnPacket spawn = new MKParticleEffectSpawnPacket(from, CAST_PARTICLES);
        spawn.addLoc(to);
        PacketHandler.sendToTrackingAndSelf(spawn, castingEntity);
    }
}

