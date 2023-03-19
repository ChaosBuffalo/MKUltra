package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.damage.MKDamageSource;
import com.chaosbuffalo.mkcore.core.damage.MKDamageType;
import com.chaosbuffalo.mkcore.core.healing.MKHealSource;
import com.chaosbuffalo.mkcore.core.healing.MKHealing;
import com.chaosbuffalo.mkcore.effects.MKActiveEffect;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.ScalingValueEffectState;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

public class VampiricDamageEffect extends MKEffect {

    public static final VampiricDamageEffect INSTANCE = new VampiricDamageEffect();

    public VampiricDamageEffect() {
        super(EffectType.HARMFUL);
        this.setRegistryName(MKUltra.MODID, "effect.vampiric_damage");
    }

    @Override
    public State makeState() {
        return new VampiricDamageEffect.State();
    }

    @Override
    public MKEffectBuilder<State> builder(UUID sourceId) {
        return new MKEffectBuilder(this, sourceId, this::makeState);
    }

    @Override
    public MKEffectBuilder<State> builder(LivingEntity sourceEntity) {
        return new MKEffectBuilder(this, sourceEntity, this::makeState);
    }

    public static MKEffectBuilder<State> from(LivingEntity source, MKDamageType damageType, float baseDamage,
                                              float scaling, float modifierScaling, float healthScaling, float healModScaling) {
        return INSTANCE.builder(source).state((s) -> {
            s.setDamageType(damageType);
            s.setScalingParameters(baseDamage, scaling, modifierScaling);
            s.setHealthScaling(healthScaling);
            s.setHealModScaling(healModScaling);
        });
    }

    public static class State extends ScalingValueEffectState {
        protected float healthScaling = 1.0f;
        protected float healModScaling = 1.0f;

        public State() {
        }

        public boolean validateOnLoad(MKActiveEffect activeEffect) {
            return this.damageType != null;
        }

        public boolean validateOnApply(IMKEntityData targetData, MKActiveEffect activeEffect) {
            return this.damageType != null;
        }

        public void setHealModScaling(float healModScaling) {
            this.healModScaling = healModScaling;
        }

        public float getHealModScaling() {
            return healModScaling;
        }

        public float getHealthScaling() {
            return healthScaling;
        }

        public void setHealthScaling(float healthScaling) {
            this.healthScaling = healthScaling;
        }

        @Override
        public void serializeStorage(CompoundNBT stateTag) {
            super.serializeStorage(stateTag);
            stateTag.putFloat("healthScaling", healthScaling);
            stateTag.putFloat("healthModScaling", healModScaling);
        }

        @Override
        public void deserializeStorage(CompoundNBT stateTag) {
            super.deserializeStorage(stateTag);
            healthScaling = stateTag.getFloat("healthScaling");
            healModScaling = stateTag.getFloat("healthModScaling");
        }

        public boolean performEffect(IMKEntityData targetData, MKActiveEffect activeEffect) {
            DamageSource damage = MKDamageSource.causeAbilityDamage(this.damageType, activeEffect.getAbilityId(),
                    activeEffect.getDirectEntity(), activeEffect.getSourceEntity(), this.getModifierScale());
            float value = this.getScaledValue(activeEffect.getStackCount(), activeEffect.getSkillLevel());
            targetData.getEntity().attackEntityFrom(damage, value);
            LivingEntity source = activeEffect.getSourceEntity();
            if (source != null) {
                MKHealSource healSource = MKHealSource.getShadowHeal(activeEffect.getAbilityId(),
                        activeEffect.getDirectEntity(), activeEffect.getSourceEntity(), getHealModScaling());
                MKHealing.healEntityFrom(source, value * getHealthScaling(), healSource);
            }
            return true;
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
