package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.effects.*;
import com.chaosbuffalo.mkcore.effects.status.DamageTypeDotEffect;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.init.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
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

    public static MKEffectBuilder<?> from(Entity source, float base, float scaling, float modifierScaling,
                                          ResourceLocation castParticles) {
        return INSTANCE.builder(source.getUniqueID())
                .state(s -> {
                    s.particles = castParticles;
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

    public static class State extends DamageTypeDotEffect.State {
        public ResourceLocation particles;

        public State() {
            super();
            damageType = CoreDamageTypes.FireDamage;
        }

        @Override
        public boolean performEffect(IMKEntityData imkEntityData, MKActiveEffect mkActiveEffect) {
            SoundUtils.serverPlaySoundAtEntity(imkEntityData.getEntity(), ModSounds.spell_fire_6, imkEntityData.getEntity().getSoundCategory());
            PacketHandler.sendToTrackingAndSelf(
                    new MKParticleEffectSpawnPacket(new Vector3d(0.0, 1.0, 0.0), particles, imkEntityData.getEntity().getEntityId()), imkEntityData.getEntity());
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
