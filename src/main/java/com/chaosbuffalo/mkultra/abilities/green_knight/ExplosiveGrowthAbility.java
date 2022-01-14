package com.chaosbuffalo.mkultra.abilities.green_knight;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.abilities.AbilityContext;
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
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.Targeting;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;

public class ExplosiveGrowthAbility extends MKAbility {
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "explosive_growth_casting");
    public static final ResourceLocation CAST_PARTICLES = new ResourceLocation(MKUltra.MODID, "explosive_growth_cast");
    public static final ResourceLocation DETONATE_PARTICLES = new ResourceLocation(MKUltra.MODID, "explosive_growth_detonate");
    public static final ExplosiveGrowthAbility INSTANCE = new ExplosiveGrowthAbility();

    protected final FloatAttribute baseDamage = new FloatAttribute("baseDamage", 10.0f);
    protected final FloatAttribute scaleDamage = new FloatAttribute("scaleDamage", 5.0f);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);
    protected final ResourceLocationAttribute cast_particles = new ResourceLocationAttribute("cast_particles", CAST_PARTICLES);
    protected final ResourceLocationAttribute detonate_particles = new ResourceLocationAttribute("detonate_particles", DETONATE_PARTICLES);

    private ExplosiveGrowthAbility() {
        super(MKUltra.MODID, "ability.explosive_growth");
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
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        ITextComponent damageStr = getDamageDescription(entityData, CoreDamageTypes.MeleeDamage, baseDamage.value(),
                scaleDamage.value(),
                getSkillLevel(entityData.getEntity(), MKAttributes.PANKRATION),
                modifierScaling.value());
        return new TranslationTextComponent(getDescriptionTranslationKey(), damageStr);
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 8.0f;
    }

    @Override
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.casting_shadow;
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_earth_8;
    }

    @Override
    public void endCast(LivingEntity castingEntity, IMKEntityData casterData, AbilityContext context) {
        super.endCast(castingEntity, casterData, context);
        float restoLevel = getSkillLevel(castingEntity, MKAttributes.RESTORATION);
        float pankrationLevel = getSkillLevel(castingEntity, MKAttributes.PANKRATION);

        SoundCategory cat = castingEntity instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.HOSTILE;
        float damage = baseDamage.value() + scaleDamage.value() * pankrationLevel;

        MKEffectBuilder<?> cure = CureEffect.from(castingEntity)
                .ability(this)
                .skillLevel(restoLevel);
        MKEffectBuilder<?> remedy = NaturesRemedyAbility.INSTANCE.createNaturesRemedyEffect(casterData, restoLevel)
                .ability(this);

        Vector3d look = castingEntity.getLookVec().scale(getDistance(castingEntity));
        Vector3d from = castingEntity.getPositionVec().add(0, castingEntity.getEyeHeight(), 0);
        Vector3d to = from.add(look);
        List<LivingEntity> entityHit = TargetUtil.getTargetsInLine(castingEntity, from, to, 1.0f, this::isValidTarget);

        for (LivingEntity entHit : entityHit) {
            Targeting.TargetRelation relation = Targeting.getTargetRelation(castingEntity, entHit);
            switch (relation) {
                case FRIEND: {
                    MKCore.getEntityData(entHit).ifPresent(targetData -> {
                        targetData.getEffects().addEffect(cure);
                        targetData.getEffects().addEffect(remedy);
                    });

                    SoundUtils.serverPlaySoundAtEntity(entHit, ModSounds.spell_earth_6, cat);
                    break;
                }
                case ENEMY: {
                    entHit.attackEntityFrom(MKDamageSource.causeMeleeDamage(getAbilityId(), castingEntity, castingEntity), damage);
                    SoundUtils.serverPlaySoundAtEntity(entHit, ModSounds.spell_earth_1, cat);
                    break;
                }
            }

            PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                    new Vector3d(0.0, 1.0, 0.0), detonate_particles.getValue(),
                    entHit.getEntityId()), entHit);
        }

        RayTraceResult blockHit = RayTraceUtils.rayTraceBlocks(castingEntity, from, to, false);
        if (blockHit != null && blockHit.getType() == RayTraceResult.Type.BLOCK) {
            to = blockHit.getHitVec();
        }

        casterData.getEffects().addEffect(cure);
        casterData.getEffects().addEffect(remedy);
        castingEntity.setPositionAndUpdate(to.x, to.y, to.z);
        MKParticleEffectSpawnPacket spawn = new MKParticleEffectSpawnPacket(from, CAST_PARTICLES);
        spawn.addLoc(to);
        PacketHandler.sendToTrackingAndSelf(spawn, castingEntity);
    }

    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class RegisterMe {
        @SubscribeEvent
        public static void register(RegistryEvent.Register<MKAbility> event) {
            event.getRegistry().register(INSTANCE);
        }
    }
}
