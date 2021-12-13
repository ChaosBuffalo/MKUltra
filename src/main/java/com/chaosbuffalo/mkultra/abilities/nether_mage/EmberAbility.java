package com.chaosbuffalo.mkultra.abilities.nether_mage;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.SpellCast;
import com.chaosbuffalo.mkcore.effects.instant.MKAbilityDamageEffect;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.serialization.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.IntAttribute;
import com.chaosbuffalo.mkcore.serialization.attributes.ResourceLocationAttribute;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.spells.BurnEffect;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.potion.EffectInstance;
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
public class EmberAbility extends MKAbility {
    public static final ResourceLocation CASTING_PARTICLES = new ResourceLocation(MKUltra.MODID, "ember_casting");
    public static final ResourceLocation CAST_PARTICLES = new ResourceLocation(MKUltra.MODID, "ember_cast");
    public static final ResourceLocation BURN_PARTICLES = new ResourceLocation(MKUltra.MODID, "burn_tick");
    public static final EmberAbility INSTANCE = new EmberAbility();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKAbility> event) {
        event.getRegistry().register(INSTANCE);
    }

    protected final FloatAttribute base = new FloatAttribute("base", 8.0f);
    protected final FloatAttribute scale = new FloatAttribute("scale", 3.0f);
    protected final FloatAttribute baseDot = new FloatAttribute("baseBurnDamage", 2.0f);
    protected final FloatAttribute scaleDot = new FloatAttribute("scaleBurnDamage", 1.0f);
    protected final IntAttribute baseDuration = new IntAttribute("baseDuration", 6);
    protected final IntAttribute scaleDuration = new IntAttribute("scaleDuration", 1);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);
    protected final FloatAttribute dotModifierScaling = new FloatAttribute("dotModifierScaling", 0.2f);
    protected final ResourceLocationAttribute cast_particles = new ResourceLocationAttribute("cast_particles", CAST_PARTICLES);
    protected final ResourceLocationAttribute burn_cast_particles = new ResourceLocationAttribute("burn_cast_particles", BURN_PARTICLES);



    public EmberAbility() {
        super(MKUltra.MODID, "ability.ember");
        setCooldownSeconds(6);
        setManaCost(4);
        setCastTime(GameConstants.TICKS_PER_SECOND / 2);
        addAttributes(base, scale, modifierScaling, baseDuration, scaleDuration, baseDot, scaleDot, dotModifierScaling,
                cast_particles, burn_cast_particles);
        addSkillAttribute(MKAttributes.EVOCATION);
        casting_particles.setDefaultValue(CASTING_PARTICLES);
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        int level = getSkillLevel(entityData.getEntity(), MKAttributes.EVOCATION);
        ITextComponent valueStr = getDamageDescription(entityData,
                CoreDamageTypes.FireDamage, base.getValue(), scale.getValue(), level, modifierScaling.getValue());
        ITextComponent dotStr = getDamageDescription(entityData,
                CoreDamageTypes.FireDamage, baseDot.getValue(), scaleDot.getValue(), level, dotModifierScaling.getValue());
        return new TranslationTextComponent(getDescriptionTranslationKey(), valueStr,
                getBuffDuration(entityData, level, baseDuration.getValue(), scaleDuration.getValue()) / 20,
                dotStr, BurnEffect.INSTANCE.getPeriod() / 20);
    }

    public EffectInstance getBurnCast(LivingEntity caster, LivingEntity target, IMKEntityData data, int level){
        int burnTicks = getBuffDuration(data, level, baseDuration.getValue(), scaleDuration.getValue());
        SpellCast burn = BurnEffect.Create(caster, baseDot.getValue(), scaleDot.getValue(),
                dotModifierScaling.getValue(), burn_cast_particles.getValue()).setTarget(target);
        return burn.toPotionEffect(burnTicks, level);
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
        return ModSounds.casting_fire;
    }

    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_cast_7;
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        int level = getSkillLevel(entity, MKAttributes.EVOCATION);
        context.getMemory(MKAbilityMemories.ABILITY_TARGET).ifPresent(targetEntity -> {
            SpellCast damage = MKAbilityDamageEffect.Create(entity, CoreDamageTypes.FireDamage,
                    this,
                    base.getValue(),
                    scale.getValue(),
                    modifierScaling.getValue()).setTarget(targetEntity);
            targetEntity.addPotionEffect(damage.toPotionEffect(level));
            EffectInstance burn = getBurnCast(entity, targetEntity, data, level);
            targetEntity.addPotionEffect(burn);
            SoundUtils.serverPlaySoundAtEntity(targetEntity, ModSounds.spell_fire_6, targetEntity.getSoundCategory());
            PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                    new Vector3d(0.0, 1.0, 0.0), cast_particles.getValue(),
                            targetEntity.getEntityId()),
                    targetEntity);
        });
    }
}
