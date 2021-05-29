package com.chaosbuffalo.mkultra.abilities.green_knight;

import com.chaosbuffalo.mkcore.abilities.*;
import com.chaosbuffalo.mkcore.abilities.ai.conditions.NeedsBuffCondition;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.PassiveEffect;
import com.chaosbuffalo.mkcore.fx.ParticleEffects;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.network.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.spells.SkinLikeWoodEffect;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.TargetingContext;
import com.chaosbuffalo.targeting_api.TargetingContexts;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.brain.memory.MemoryModuleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.Set;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SkinLikeWoodAbility extends MKToggleAbility {

    public static final SkinLikeWoodAbility INSTANCE = new SkinLikeWoodAbility();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKAbility> event) {
        event.getRegistry().register(INSTANCE);
    }

    private SkinLikeWoodAbility() {
        super(new ResourceLocation(MKUltra.MODID, "ability.skin_like_wood"));
        setCooldownSeconds(3);
        setManaCost(2);
        addSkillAttribute(MKAttributes.ABJURATION);
        setUseCondition(new NeedsBuffCondition(this, SkinLikeWoodEffect.INSTANCE).setSelfOnly(true));
    }

    @Override
    public TargetingContext getTargetContext() {
        return TargetingContexts.SELF;
    }

    @Override
    public float getDistance(LivingEntity entity) {
        return 1.0f;
    }

    @Override
    public AbilityTargetSelector getTargetSelector() {
        return AbilityTargeting.SELF;
    }


    @Override
    public Set<MemoryModuleType<?>> getRequiredMemories() {
        return ImmutableSet.of(MKAbilityMemories.ABILITY_TARGET);
    }


    @Override
    public PassiveEffect getToggleEffect() {
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
        int amplifier = getSkillLevel(entity, MKAttributes.ABJURATION);
        SoundUtils.playSoundAtEntity(entity, ModSounds.spell_earth_7);
        // What to do for each target hit
        entity.addPotionEffect(getToggleEffect().createSelfCastEffectInstance(entity, amplifier));

        PacketHandler.sendToTrackingAndSelf(
                new ParticleEffectSpawnPacket(
                        ParticleTypes.ITEM_SLIME,
                        ParticleEffects.CIRCLE_MOTION, 30, 0,
                        entity.getPosX(), entity.getPosY() + .5,
                        entity.getPosZ(), 1.0, 1.0, 1.0, 1.0f,
                        entity.getLookVec()), entity);
    }
}
