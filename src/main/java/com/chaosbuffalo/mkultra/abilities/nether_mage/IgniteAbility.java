package com.chaosbuffalo.mkultra.abilities.nether_mage;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.abilities.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkcore.effects.ParticleEffect;
import com.chaosbuffalo.mkcore.effects.SpellCast;
import com.chaosbuffalo.mkcore.effects.instant.MKAbilityDamageEffect;
import com.chaosbuffalo.mkcore.effects.instant.SoundEffect;
import com.chaosbuffalo.mkcore.fx.ParticleEffects;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.network.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.MKUAbilityUtils;
import com.chaosbuffalo.mkultra.effects.spells.IgniteEffect;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;


@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IgniteAbility extends MKAbility {

    public static final IgniteAbility INSTANCE = new IgniteAbility();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKAbility> event) {
        event.getRegistry().register(INSTANCE);
    }

    protected final FloatAttribute base = new FloatAttribute("base", 8.0f);
    protected final FloatAttribute scale = new FloatAttribute("scale", 1.0f);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);
    protected final FloatAttribute igniteDistance = new FloatAttribute("igniteDistance", 5.0f);

    public IgniteAbility() {
        super(MKUltra.MODID, "ability.ignite");
        setCooldownSeconds(16);
        setManaCost(8);
        setCastTime(GameConstants.TICKS_PER_SECOND / 4);
        addAttributes(base, scale, modifierScaling);
        addSkillAttribute(MKAttributes.EVOCATION);
    }

    @Override
    public AbilityType getType() {
        return AbilityType.PooledUltimate;
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        int level = getSkillLevel(entityData.getEntity(), MKAttributes.EVOCATION);
        ITextComponent valueStr = getDamageDescription(entityData,
                CoreDamageTypes.FireDamage, base.getValue(), scale.getValue(), level, modifierScaling.getValue());
        return new TranslationTextComponent(getDescriptionTranslationKey(), valueStr, igniteDistance.getValue());
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
        return ModSounds.spell_dark_13;
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
            SoundUtils.playSoundAtEntity(targetEntity, ModSounds.spell_fire_4);
            if (MKUAbilityUtils.isBurning(targetEntity)){
                SpellCast ignite = IgniteEffect.Create(entity, base.getValue(), scale.getValue(),
                        modifierScaling.getValue());
                SpellCast particle = ParticleEffect.Create(entity,
                        ParticleTypes.FLAME,
                        ParticleEffects.SPHERE_MOTION, false, new Vector3d(1.0, 1.0, 1.0),
                        new Vector3d(0.0, 1.0, 0.0), 40, 5, 0.4);
                AreaEffectBuilder.Create(entity, targetEntity)
                        .spellCast(particle, level, getTargetContext())
                        .spellCast(ignite, level, getTargetContext())
                        .spellCast(SoundEffect.Create(entity, ModSounds.spell_fire_8, entity.getSoundCategory()),
                                1, getTargetContext())
                        .instant().color(16737305).radius(igniteDistance.getValue(), true)
                        .particle(ParticleTypes.FLAME)
                        .spawn();

            } else {
                EffectInstance burn = EmberAbility.INSTANCE.getBurnCast(entity, targetEntity, data, level);
                targetEntity.addPotionEffect(burn);
            }
            Vector3d lookVec = entity.getLookVec();
            PacketHandler.sendToTrackingAndSelf(
                    new ParticleEffectSpawnPacket(
                            ParticleTypes.FLAME,
                            ParticleEffects.CIRCLE_MOTION, 40, 10,
                            targetEntity.getPosX(), targetEntity.getPosY() + 1.0f,
                            targetEntity.getPosZ(), 1.0, 1.0, 1.0, 0.25,
                            lookVec), targetEntity);
        });
    }
}
