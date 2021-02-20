package com.chaosbuffalo.mkultra.abilities.green_knight;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.abilities.ai.conditions.HealCondition;
import com.chaosbuffalo.mkcore.abilities.attributes.FloatAttribute;
import com.chaosbuffalo.mkcore.abilities.attributes.IntAttribute;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.SpellCast;
import com.chaosbuffalo.mkcore.fx.ParticleEffects;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.network.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.spells.NaturesRemedyEffect;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Set;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NaturesRemedyAbility extends MKAbility {
    public static final NaturesRemedyAbility INSTANCE = new NaturesRemedyAbility();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKAbility> event) {
        event.getRegistry().register(INSTANCE);
    }

    protected final FloatAttribute baseValue = new FloatAttribute("baseValue", 2.0f);
    protected final FloatAttribute scaleValue = new FloatAttribute("scaleValue", 1.0f);
    protected final IntAttribute baseDuration = new IntAttribute("baseDuration", 4);
    protected final IntAttribute scaleDuration = new IntAttribute("scaleDuration", 1);
    protected final FloatAttribute modifierScaling = new FloatAttribute("modifierScaling", 1.0f);

    private NaturesRemedyAbility() {
        super(MKUltra.MODID, "ability.natures_remedy");
        setCooldownSeconds(10);
        setManaCost(4);
        setCastTime(GameConstants.TICKS_PER_SECOND);
        addSkillAttribute(MKAttributes.RESTORATION);
        addAttributes(baseValue, scaleValue, baseDuration, scaleDuration, modifierScaling);
        setUseCondition(new HealCondition(this, .75f));
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.FRIENDLY;
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 10.0f;
    }

    @Override
    protected ITextComponent getAbilityDescription(IMKEntityData entityData) {
        int level = getSkillLevel(entityData.getEntity(), MKAttributes.RESTORATION);
        ITextComponent damageStr = getHealDescription(entityData, baseValue.getValue(),
                scaleValue.getValue(), level,
                modifierScaling.getValue());
        return new TranslationTextComponent(getDescriptionTranslationKey(), damageStr,
                getBuffDuration(entityData, level, baseDuration.getValue(), scaleDuration.getValue()));
    }

    public float getModifierScaling() {
        return modifierScaling.getValue();
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_cast_5;
    }

    public void castNaturesRemedyOnTarget(LivingEntity target, IMKEntityData casterData, int level){
        int duration = getBuffDuration(casterData, level, baseDuration.getValue(), scaleDuration.getValue()) / GameConstants.TICKS_PER_SECOND;
        SpellCast heal = NaturesRemedyEffect.Create(casterData.getEntity(), target,
                baseValue.getValue(), scaleDuration.getValue());
        target.addPotionEffect(heal.toPotionEffect(duration, level));
    }

    @Override
    public void endCast(LivingEntity entity, IMKEntityData data, AbilityContext context) {
        super.endCast(entity, data, context);
        int level = getSkillLevel(entity, MKAttributes.RESTORATION);
        context.getMemory(MKAbilityMemories.ABILITY_TARGET).ifPresent(targetEntity -> {
            castNaturesRemedyOnTarget(targetEntity, data, level);
            SoundUtils.playSoundAtEntity(targetEntity, ModSounds.spell_heal_8);
            Vector3d lookVec = entity.getLookVec();
            PacketHandler.sendToTrackingAndSelf(
                    new ParticleEffectSpawnPacket(
                            ParticleTypes.ITEM_SLIME,
                            ParticleEffects.SPHERE_MOTION, 30, 10,
                            targetEntity.getPosX(), targetEntity.getPosY() + 1.0f,
                            targetEntity.getPosZ(), 1.0, 1.0, 1.0, .5,
                            lookVec), targetEntity);
        });
    }

    @Override
    public Set<MemoryModuleType<?>> getRequiredMemories() {
        return ImmutableSet.of(MKAbilityMemories.ABILITY_TARGET);
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.SINGLE_TARGET_OR_SELF;
    }
}

