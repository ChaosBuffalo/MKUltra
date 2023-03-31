package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.healing.MKHealSource;
import com.chaosbuffalo.mkcore.core.healing.MKHealing;
import com.chaosbuffalo.mkcore.effects.MKActiveEffect;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.ScalingValueEffectState;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.targeting_api.TargetingContext;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

public class ClericHealEffect extends MKEffect {

    public static final ClericHealEffect INSTANCE = new ClericHealEffect();

    private ClericHealEffect() {
        super(MobEffectCategory.BENEFICIAL);
        setRegistryName(MKUltra.MODID, "effect.cleric_heal");
    }

    public static MKEffectBuilder<?> from(LivingEntity source, float base, float scale, float modScale) {
        return INSTANCE.builder(source).state(s -> s.setScalingParameters(base, scale, modScale));
    }

    @Override
    public boolean isValidTarget(TargetingContext targetContext, IMKEntityData sourceData, IMKEntityData targetData) {
        return super.isValidTarget(targetContext, sourceData, targetData) ||
                MKHealing.wouldHealHurtUndead(sourceData.getEntity(), targetData.getEntity());
    }

    @Override
    public MKEffectBuilder<State> builder(UUID sourceId) {
        return new MKEffectBuilder<>(this, sourceId, this::makeState);
    }

    @Override
    public MKEffectBuilder<State> builder(LivingEntity sourceEntity) {
        return new MKEffectBuilder<>(this, sourceEntity, this::makeState);
    }

    @Override
    public State makeState() {
        return new State();
    }

    public static class State extends ScalingValueEffectState {

        @Override
        public boolean performEffect(IMKEntityData targetData, MKActiveEffect activeEffect) {
            LivingEntity target = targetData.getEntity();
            float value = getScaledValue(activeEffect.getStackCount(), activeEffect.getSkillLevel());
//            MKUltra.LOGGER.info("ClericHealEffect.performEffect {} on {} from {} {}", value, target, source, instance);
            MKHealSource heal = MKHealSource.getHolyHeal(activeEffect.getAbilityId(),
                    activeEffect.getDirectEntity(), activeEffect.getSourceEntity(), getModifierScale());
            heal.setDamageUndead(activeEffect.hasSourceEntity() && !activeEffect.getSourceEntity().isInvertedHealAndHarm());
            MKHealing.healEntityFrom(target, value, heal);
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
