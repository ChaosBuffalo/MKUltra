package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.healing.MKHealSource;
import com.chaosbuffalo.mkcore.core.healing.MKHealing;
import com.chaosbuffalo.mkcore.effects.*;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

public class FuriousBroodingEffect extends MKEffect {
    public static final int DEFAULT_PERIOD = GameConstants.TICKS_PER_SECOND;

    private static final UUID modUUID = UUID.fromString("06bbfb88-d53e-4565-964c-4642b9165c6d");
    public static final FuriousBroodingEffect INSTANCE = new FuriousBroodingEffect();

    protected FuriousBroodingEffect() {
        super(EffectType.BENEFICIAL);
        setRegistryName(MKUltra.MODID, "effect.furious_brooding");
        addAttribute(Attributes.MOVEMENT_SPEED, modUUID, -0.06, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    public static MKEffectBuilder<?> from(LivingEntity source, float baseHealing, float scaling, float modifierScaling,
                                          ResourceLocation castParticles, float healingSkill) {
        return INSTANCE.builder(source)
                .state(s -> {
                    s.setEffectParticles(castParticles);
                    s.setScalingParameters(baseHealing, scaling, modifierScaling);
                    s.setHealingSkill(healingSkill);
                })
                .periodic(DEFAULT_PERIOD);
    }

    @Override
    public FuriousBroodingEffect.State makeState() {
        return new FuriousBroodingEffect.State();
    }

    @Override
    public MKEffectBuilder<FuriousBroodingEffect.State> builder(UUID sourceId) {
        return new MKEffectBuilder<>(this, sourceId, this::makeState);
    }

    @Override
    public MKEffectBuilder<FuriousBroodingEffect.State> builder(LivingEntity sourceEntity) {
        return new MKEffectBuilder<>(this, sourceEntity, this::makeState);
    }

    public static class State extends ScalingValueEffectState {
        protected float healingSkill = 0.0f;

        @Override
        public boolean performEffect(IMKEntityData targetData, MKActiveEffect activeEffect) {
            float healing = getScaledValue(activeEffect.getStackCount(), getHealingSkill());
            LivingEntity target = targetData.getEntity();
            MKHealing.healEntityFrom(target, healing, MKHealSource.getNatureHeal(activeEffect.getAbilityId(),
                    activeEffect.getDirectEntity(), activeEffect.getSourceEntity(), getModifierScale()));
            sendEffectParticles(targetData.getEntity());
            return true;
        }

        public void setHealingSkill(float healingSkill) {
            this.healingSkill = healingSkill;
        }

        public float getHealingSkill() {
            return healingSkill;
        }

        @Override
        public void deserializeStorage(CompoundNBT stateTag) {
            super.deserializeStorage(stateTag);
            healingSkill = stateTag.getFloat("healSkill");
        }

        @Override
        public void serializeStorage(CompoundNBT stateTag) {
            super.serializeStorage(stateTag);
            stateTag.putFloat("healSkill", healingSkill);
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