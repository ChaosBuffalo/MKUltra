package com.chaosbuffalo.mkultra.abilities.cleric;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.abilities.attributes.IntAttribute;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.fx.ParticleEffects;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.network.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.spells.WarpTargetEffect;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.Targeting;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Set;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PowerWordSummonAbility extends MKAbility {
    public static final PowerWordSummonAbility INSTANCE = new PowerWordSummonAbility();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKAbility> event) {
        event.getRegistry().register(INSTANCE);
    }

    protected final IntAttribute base = new IntAttribute("baseDuration", 4);
    protected final IntAttribute scale = new IntAttribute("scaleDuration", 1);

    public PowerWordSummonAbility(){
        super(MKUltra.MODID, "ability.power_word_summon");
        setCooldownSeconds(16);
        setCastTime(GameConstants.TICKS_PER_SECOND);
        setManaCost(6);
        addAttributes(base, scale);
        addSkillAttribute(MKAttributes.CONJURATION);
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 30.0f;
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.ALL;
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        int level = getSkillLevel(entityData.getEntity(), MKAttributes.CONJURATION);
        int duration = getBuffDuration(entityData, level, base.getValue(), scale.getValue()) / GameConstants.TICKS_PER_SECOND;
        return new TranslationTextComponent(getDescriptionTranslationKey(), duration);
    }

    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_magic_whoosh_3;
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.SINGLE_TARGET;
    }

    @Override
    public Set<MemoryModuleType<?>> getRequiredMemories() {
        return ImmutableSet.of(MKAbilityMemories.ABILITY_TARGET);
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        int level = getSkillLevel(entity, MKAttributes.CONJURATION);
        context.getMemory(MKAbilityMemories.ABILITY_TARGET).ifPresent(targetEntity -> {
            targetEntity.addPotionEffect(WarpTargetEffect.Create(entity).setTarget(targetEntity).toPotionEffect(level));
            if (Targeting.isValidEnemy(entity, targetEntity)){
                int duration = getBuffDuration(data, level, base.getValue(), scale.getValue());
                targetEntity.addPotionEffect(
                    new EffectInstance(Effects.SLOWNESS, duration, 100, false, true));
            }
            SoundUtils.playSoundAtEntity(targetEntity, ModSounds.spell_magic_whoosh_4);
            Vector3d lookVec = entity.getLookVec();
            PacketHandler.sendToTrackingAndSelf(
                    new ParticleEffectSpawnPacket(
                            ParticleTypes.ENTITY_EFFECT,
                            ParticleEffects.CIRCLE_PILLAR_MOTION, 60, 10,
                            targetEntity.getPosX(), targetEntity.getPosY() + 0.5f,
                            targetEntity.getPosZ(), 1.0, 1.0, 1.0, 1.0,
                            lookVec), targetEntity);
        });
    }
}
