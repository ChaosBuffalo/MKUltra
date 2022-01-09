package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.damage.MKDamageSource;
import com.chaosbuffalo.mkcore.effects.MKActiveEffect;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.ScalingValueEffectState;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkcore.utils.EntityUtils;
import com.chaosbuffalo.mkcore.utils.SoundUtils;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.nether_mage.WarpCurseAbility;
import com.chaosbuffalo.mkultra.init.ModSounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

public class WarpCurseEffect extends MKEffect {
    public static final int DEFAULT_PERIOD = 40;

    public static final WarpCurseEffect INSTANCE = new WarpCurseEffect();

    public static MKEffectBuilder<?> from(Entity source, float base, float scaling, float modifier, ResourceLocation castParticles) {
        return INSTANCE.builder(source.getUniqueID()).state(s -> {
                    s.particles = castParticles;
                    s.setScalingParameters(base, scaling, modifier);
                })
                .periodic(DEFAULT_PERIOD);
    }

    private WarpCurseEffect() {
        super(EffectType.HARMFUL);
        setRegistryName(MKUltra.MODID, "effect.warp_curse");
    }

    @Override
    public State makeState() {
        return new State();
    }

    @Override
    public MKEffectBuilder<State> builder(UUID sourceId) {
        return new MKEffectBuilder<>(this, sourceId, this::makeState);
    }

    public static class State extends ScalingValueEffectState {
        private Entity source;
        public ResourceLocation particles;

        @Override
        public boolean performEffect(IMKEntityData imkEntityData, MKActiveEffect activeEffect) {
            source = findEntity(source, activeEffect.getSourceId(), imkEntityData);

            LivingEntity target = imkEntityData.getEntity();
            Vector3d targetOrigin = target.getPositionVec();
            target.attackEntityFrom(MKDamageSource.causeAbilityDamage(CoreDamageTypes.ShadowDamage,
                    WarpCurseAbility.INSTANCE.getAbilityId(), source, source,
                    getModifierScale()), getScaledValue(activeEffect.getStackCount()));

            SoundUtils.serverPlaySoundAtEntity(target, ModSounds.spell_fire_5, target.getSoundCategory());
            if (EntityUtils.canTeleportEntity(target)) {
                double nextX = targetOrigin.x + (target.getRNG().nextInt(8) - target.getRNG().nextInt(8));
                double nextY = targetOrigin.y + 5.0;
                double nextZ = targetOrigin.z + (target.getRNG().nextInt(8) - target.getRNG().nextInt(8));
                EntityUtils.safeTeleportEntity(target, new Vector3d(nextX, nextY, nextZ));
            }
            PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                            new Vector3d(0.0, 1.0, 0.0),
                            particles, target.getEntityId()),
                    target);
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
