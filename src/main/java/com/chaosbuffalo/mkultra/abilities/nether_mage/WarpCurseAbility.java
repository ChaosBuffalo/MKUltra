package com.chaosbuffalo.mkultra.abilities.nether_mage;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.SpellCast;
import com.chaosbuffalo.mkcore.fx.ParticleEffects;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.network.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.IntAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.spells.WarpCurseEffect;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;


@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WarpCurseAbility extends MKAbility {
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "warp_curse_casting");
    public static final ResourceLocation CAST_PARTICLES = new ResourceLocation(MKUltra.MODID, "warp_curse_cast");
    public static final WarpCurseAbility INSTANCE = new WarpCurseAbility();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKAbility> event) {
        event.getRegistry().register(INSTANCE);
    }

    protected final FloatAttribute base = new FloatAttribute("base", 4.0f);
    protected final FloatAttribute scale = new FloatAttribute("scale", 2.0f);
    protected final IntAttribute baseDuration = new IntAttribute("baseDuration", 4);
    protected final IntAttribute scaleDuration = new IntAttribute("scaleDuration", 2);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 0.25f);
    protected final ResourceLocationAttribute cast_particles = new ResourceLocationAttribute("cast_particles", CAST_PARTICLES);

    public WarpCurseAbility() {
        super(MKUltra.MODID, "ability.warp_curse");
        setCooldownSeconds(16);
        setManaCost(6);
        setCastTime(GameConstants.TICKS_PER_SECOND + 10);
        addAttributes(base, scale, modifierScaling, baseDuration, scaleDuration, cast_particles);
        addSkillAttribute(MKAttributes.ALTERATON);
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        int level = getSkillLevel(entityData.getEntity(), MKAttributes.ALTERATON);
        ITextComponent valueStr = getDamageDescription(entityData,
                CoreDamageTypes.ShadowDamage, base.getValue(), scale.getValue(), level, modifierScaling.getValue());
        return new TranslationTextComponent(getDescriptionTranslationKey(), valueStr,
                WarpCurseEffect.INSTANCE.getPeriod() / 20,
                getBuffDuration(entityData, level, baseDuration.getValue(), scaleDuration.getValue()) / 20);
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
    public Set<MemoryModuleType<?>> getRequiredMemories() {
        return ImmutableSet.of(MKAbilityMemories.ABILITY_TARGET);
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
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        int level = getSkillLevel(entity, MKAttributes.ALTERATON);
        context.getMemory(MKAbilityMemories.ABILITY_TARGET).ifPresent(targetEntity -> {
            int dur = getBuffDuration(data, level, baseDuration.getValue(), scaleDuration.getValue());
            SpellCast warpCast = WarpCurseEffect.Create(entity, base.getValue(), scale.getValue(),
                    modifierScaling.getValue(), cast_particles.getValue()).setTarget(targetEntity);
            targetEntity.addPotionEffect(warpCast.toPotionEffect(dur, level));
            targetEntity.addPotionEffect(new EffectInstance(Effects.SLOWNESS, dur, level,  false, false, true, null));
            SoundUtils.serverPlaySoundAtEntity(targetEntity, ModSounds.spell_fire_5, targetEntity.getSoundCategory());
            PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                            new Vector3d(0.0, 1.0, 0.0), cast_particles.getValue(), entity.getEntityId()),
                    entity);
        });
    }
}
