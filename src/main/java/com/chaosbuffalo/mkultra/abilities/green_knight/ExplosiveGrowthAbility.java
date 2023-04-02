package com.chaosbuffalo.mkultra.abilities.green_knight;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.abilities.AbilityContext;
import com.chaosbuffalo.mkcore.abilities.AbilityTargetSelector;
import com.chaosbuffalo.mkcore.abilities.AbilityTargeting;
import com.chaosbuffalo.mkcore.abilities.MKAbility;
import com.chaosbuffalo.mkcore.core.AbilityType;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.core.damage.MKDamageSource;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkcore.utils.RayTraceUtils;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkcore.utils.TargetUtil;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.CureEffect;
import com.chaosbuffalo.mkultra.init.MKUAbilities;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.Targeting;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.Nullable;
import java.util.List;

public class ExplosiveGrowthAbility extends MKAbility {
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "explosive_growth_casting");
    public static final ResourceLocation CAST_PARTICLES = new ResourceLocation(MKUltra.MODID, "explosive_growth_cast");
    public static final ResourceLocation DETONATE_PARTICLES = new ResourceLocation(MKUltra.MODID, "explosive_growth_detonate");
    protected final FloatAttribute baseDamage = new FloatAttribute("baseDamage", 10.0f);
    protected final FloatAttribute scaleDamage = new FloatAttribute("scaleDamage", 5.0f);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);
    protected final ResourceLocationAttribute cast_particles = new ResourceLocationAttribute("cast_particles", CAST_PARTICLES);
    protected final ResourceLocationAttribute detonate_particles = new ResourceLocationAttribute("detonate_particles", DETONATE_PARTICLES);

    public ExplosiveGrowthAbility() {
        super();
        setCooldownSeconds(35);
        setManaCost(6);
        setCastTime(GameConstants.TICKS_PER_SECOND / 4);
        addAttributes(baseDamage, scaleDamage, cast_particles, detonate_particles);
        addSkillAttribute(MKAttributes.RESTORATION);
        addSkillAttribute(MKAttributes.PANKRATION);
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    @Override
    public AbilityType getType() {
        return AbilityType.Ultimate;
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ALL;
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.LINE;
    }

    @Override
    protected Component getAbilityDescription(IMKEntityData entityData) {
        Component damageStr = getDamageDescription(entityData, CoreDamageTypes.MeleeDamage, baseDamage.value(),
                scaleDamage.value(),
                getSkillLevel(entityData.getEntity(), MKAttributes.PANKRATION),
                modifierScaling.value());
        return new TranslatableComponent(getDescriptionTranslationKey(), damageStr);
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 8.0f;
    }

    @Override
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.casting_shadow.get();
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_earth_8.get();
    }

    @Override
    public void endCast(LivingEntity castingEntity, IMKEntityData casterData, AbilityContext context) {
        super.endCast(castingEntity, casterData, context);
        float restoLevel = getSkillLevel(castingEntity, MKAttributes.RESTORATION);
        float pankrationLevel = getSkillLevel(castingEntity, MKAttributes.PANKRATION);

        SoundSource cat = castingEntity instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
        float damage = baseDamage.value() + scaleDamage.value() * pankrationLevel;

        MKEffectBuilder<?> cure = CureEffect.from(castingEntity)
                .ability(this)
                .skillLevel(restoLevel);
        MKEffectBuilder<?> remedy = MKUAbilities.NATURES_REMEDY.get().createNaturesRemedyEffect(casterData, restoLevel)
                .ability(this);

        Vec3 look = castingEntity.getLookAngle().scale(getDistance(castingEntity));
        Vec3 from = castingEntity.position().add(0, castingEntity.getEyeHeight(), 0);
        Vec3 to = from.add(look);
        List<LivingEntity> entityHit = TargetUtil.getTargetsInLine(castingEntity, from, to, 1.0f, this::isValidTarget);

        for (LivingEntity entHit : entityHit) {
            Targeting.TargetRelation relation = Targeting.getTargetRelation(castingEntity, entHit);
            switch (relation) {
                case FRIEND: {
                    MKCore.getEntityData(entHit).ifPresent(targetData -> {
                        targetData.getEffects().addEffect(cure);
                        targetData.getEffects().addEffect(remedy);
                    });

                    SoundUtils.serverPlaySoundAtEntity(entHit, ModSounds.spell_earth_6.get(), cat);
                    break;
                }
                case ENEMY: {
                    entHit.hurt(MKDamageSource.causeMeleeDamage(getAbilityId(), castingEntity, castingEntity), damage);
                    SoundUtils.serverPlaySoundAtEntity(entHit, ModSounds.spell_earth_1.get(), cat);
                    break;
                }
            }

            PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                    new Vec3(0.0, 1.0, 0.0), detonate_particles.getValue(),
                    entHit.getId()), entHit);
        }

        HitResult blockHit = RayTraceUtils.rayTraceBlocks(castingEntity, from, to, false);
        if (blockHit != null && blockHit.getType() == HitResult.Type.BLOCK) {
            to = blockHit.getLocation();
        }

        casterData.getEffects().addEffect(cure);
        casterData.getEffects().addEffect(remedy);
        castingEntity.teleportTo(to.x, to.y, to.z);
        MKParticleEffectSpawnPacket spawn = new MKParticleEffectSpawnPacket(from, CAST_PARTICLES);
        spawn.addLoc(to);
        PacketHandler.sendToTrackingAndSelf(spawn, castingEntity);
    }

}
