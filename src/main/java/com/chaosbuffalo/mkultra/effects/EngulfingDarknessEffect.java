package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.MKActiveEffect;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.status.DamageTypeDotEffect;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

public class EngulfingDarknessEffect extends DamageTypeDotEffect {

    public static int DEFAULT_PERIOD = 40;
    private static final UUID modUUID = UUID.fromString("b349fa30-5995-42c0-8fff-19b5c181cc75");

    public static final EngulfingDarknessEffect INSTANCE = new EngulfingDarknessEffect();


    public EngulfingDarknessEffect() {

        setRegistryName("effect.engulfing_darkness");
        addAttribute(Attributes.MOVEMENT_SPEED, modUUID, -0.10, -0.05, AttributeModifier.Operation.MULTIPLY_TOTAL, MKAttributes.CONJURATION);
    }

    public static MKEffectBuilder<?> from(LivingEntity source, float base, float scaling, float modifierScaling,
                                          float chanceToTrigger, int ticksForTrigger,
                                          ResourceLocation castParticles) {
        return INSTANCE.builder(source)
                .state(s -> {
                    s.setEffectParticles(castParticles);
                    s.setScalingParameters(base, scaling, modifierScaling);
                    s.setTriggerChance(chanceToTrigger);
                    s.setTriggerTime(ticksForTrigger);
                })
                .periodic(DEFAULT_PERIOD);
    }

    @Override
    public EngulfingDarknessEffect.State makeState() {
        return new EngulfingDarknessEffect.State();
    }

    @Override
    public MKEffectBuilder<EngulfingDarknessEffect.State> builder(UUID sourceId) {
        return new MKEffectBuilder<>(this, sourceId, this::makeState);
    }

    @Override
    public MKEffectBuilder<EngulfingDarknessEffect.State> builder(LivingEntity sourceEntity) {
        return new MKEffectBuilder<>(this, sourceEntity, this::makeState);
    }

    public static class State extends DamageTypeDotEffect.State {
        protected float triggerChance = 0.0f;
        protected int triggerTime = GameConstants.TICKS_PER_SECOND;

        public State() {
            super();
            setDamageType(CoreDamageTypes.ShadowDamage);
        }

        public void setTriggerChance(float triggerChance) {
            this.triggerChance = triggerChance;
        }

        public float getTriggerChance() {
            return triggerChance;
        }

        public void setTriggerTime(int triggerTime) {
            this.triggerTime = triggerTime;
        }

        public int getTriggerTime() {
            return triggerTime;
        }

        @Override
        public boolean performEffect(IMKEntityData targetData, MKActiveEffect activeEffect) {
            SoundUtils.serverPlaySoundAtEntity(targetData.getEntity(), ModSounds.spell_dark_1,
                    targetData.getEntity().getSoundCategory());
            sendEffectParticles(targetData.getEntity());
            LivingEntity source = activeEffect.getSourceEntity();
            if (source != null && source.getRNG().nextFloat() <= getTriggerChance()) {
                MKCore.getEntityData(source).ifPresent(
                        x -> {
                            x.getEffects().addEffect(ShadowbringerEffect.from(source, getTriggerTime()));
                            SoundUtils.serverPlaySoundAtEntity(source, ModSounds.spell_dark_9,
                                    source.getSoundCategory());
                        });
            }
            return super.performEffect(targetData, activeEffect);
        }
    }

    @SuppressWarnings("unused")
    @Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    private static class RegisterMe {
        @SubscribeEvent
        public static void register(RegistryEvent.Register<MKEffect> event) {
            event.getRegistry().register(INSTANCE);
        }
    }
}