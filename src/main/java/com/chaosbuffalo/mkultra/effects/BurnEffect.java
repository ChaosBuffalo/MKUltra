package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.effects.*;
import com.chaosbuffalo.mkcore.effects.status.DamageTypeDotEffect;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.ModSounds;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

public class BurnEffect extends DamageTypeDotEffect {

    public static int DEFAULT_PERIOD = 40;

    public static final BurnEffect INSTANCE = new BurnEffect();


    public BurnEffect() {
        setRegistryName("effect.burn");
    }

    public static MKEffectBuilder<?> from(LivingEntity source, float base, float scaling, float modifierScaling,
                                          ResourceLocation castParticles) {
        return INSTANCE.builder(source)
                .state(s -> {
                    s.setEffectParticles(castParticles);
                    s.setScalingParameters(base, scaling, modifierScaling);
                })
                .periodic(DEFAULT_PERIOD);
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

    public static class State extends DamageTypeDotEffect.State {

        public State() {
            super();
            setDamageType(CoreDamageTypes.FireDamage);
        }

        @Override
        public boolean performEffect(IMKEntityData targetData, MKActiveEffect activeEffect) {
            SoundUtils.serverPlaySoundAtEntity(targetData.getEntity(), ModSounds.spell_fire_6.get(), targetData.getEntity().getSoundSource());
            sendEffectParticles(targetData.getEntity());
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
