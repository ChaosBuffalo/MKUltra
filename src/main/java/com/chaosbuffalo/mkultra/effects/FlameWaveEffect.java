package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.damage.MKDamageSource;
import com.chaosbuffalo.mkcore.effects.*;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.MKUAbilityUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class FlameWaveEffect extends MKEffect {

    public static final FlameWaveEffect INSTANCE = new FlameWaveEffect();

    public static MKEffectBuilder<?> from(LivingEntity source, float baseDamage, float scaling, float modifierScaling,
                                          int witherBase, int witherScale, float damageMultiplier) {
        return INSTANCE.builder(source).state(s -> {
            s.witherDurationBase = witherBase;
            s.witherDurationScale = witherScale;
            s.damageBoost = damageMultiplier;
            s.setScalingParameters(baseDamage, scaling, modifierScaling);
        });
    }

    private FlameWaveEffect() {
        super(MobEffectCategory.HARMFUL);
        setRegistryName("effect.flame_wave");
    }

    @Override
    public State makeState() {
        return new State();
    }

    @Override
    public MKEffectBuilder<State> builder(UUID sourceId) {
        return new MKEffectBuilder<>(this, sourceId, this::makeState);
    }

    @Override
    public MKEffectBuilder<State> builder(LivingEntity sourceEntity) {
        return new MKEffectBuilder<>(this, sourceEntity, this::makeState);
    }

    public static class State extends ScalingValueEffectState {
        public int witherDurationBase;
        public int witherDurationScale;
        public float damageBoost;

        @Override
        public boolean performEffect(IMKEntityData targetData, MKActiveEffect activeEffect) {

            float damage = getScaledValue(activeEffect.getStackCount(), activeEffect.getSkillLevel());
            if (MKUAbilityUtils.isBurning(targetData)) {
                int dur = witherDurationBase + activeEffect.getStackCount() * witherDurationScale;
                MobEffectInstance witherEffect = new MobEffectInstance(MobEffects.WITHER, dur * GameConstants.TICKS_PER_SECOND, 0);
                damage *= damageBoost;
                targetData.getEntity().addEffect(witherEffect);
            }

            targetData.getEntity().hurt(MKDamageSource.causeAbilityDamage(CoreDamageTypes.FireDamage,
                    activeEffect.getAbilityId(), activeEffect.getDirectEntity(), activeEffect.getSourceEntity(), getModifierScale()), damage);
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
