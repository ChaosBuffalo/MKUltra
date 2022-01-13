package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.healing.MKHealSource;
import com.chaosbuffalo.mkcore.core.healing.MKHealing;
import com.chaosbuffalo.mkcore.effects.MKActiveEffect;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.ScalingValueEffectState;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

public class NaturesRemedyEffect extends MKEffect {

    public static final int DEFAULT_PERIOD = 20;

    public static final NaturesRemedyEffect INSTANCE = new NaturesRemedyEffect();

    private NaturesRemedyEffect() {
        super(EffectType.BENEFICIAL);
        setRegistryName(MKUltra.MODID, "effect.natures_remedy");
    }

    public static MKEffectBuilder<?> from(LivingEntity source, float base, float scale, float modScale,
                                          ResourceLocation castParticles) {
        return INSTANCE.builder(source).state(s -> {
            s.setEffectParticles(castParticles);
            s.setScalingParameters(base, scale, modScale);
        });
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

        @Override
        public boolean validateOnApply(IMKEntityData targetData, MKActiveEffect activeEffect) {
            return particles != null;
        }

        @Override
        public boolean performEffect(IMKEntityData targetData, MKActiveEffect activeEffect) {
            LivingEntity target = targetData.getEntity();
            float value = getScaledValue(activeEffect.getStackCount(), activeEffect.getSkillLevel());
            MKHealSource heal = MKHealSource.getNatureHeal(activeEffect.getAbilityId(),
                    activeEffect.getDirectEntity(),
                    activeEffect.getSourceEntity(),
                    getModifierScale());
            MKHealing.healEntityFrom(target, value, heal);
            sendEffectParticles(targetData.getEntity());
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
