package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.healing.MKHealSource;
import com.chaosbuffalo.mkcore.core.healing.MKHealing;
import com.chaosbuffalo.mkcore.effects.MKActiveEffect;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.ScalingValueEffectState;
import com.chaosbuffalo.mkcore.network.MKParticleEffectSpawnPacket;
import com.chaosbuffalo.mkcore.network.PacketHandler;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;


@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NaturesRemedyEffect extends MKEffect {

    public static final int DEFAULT_PERIOD = 20;

    public static final NaturesRemedyEffect INSTANCE = new NaturesRemedyEffect();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKEffect> event) {
        event.getRegistry().register(INSTANCE);
    }

    private NaturesRemedyEffect() {
        super(EffectType.BENEFICIAL);
        setRegistryName(MKUltra.MODID, "effect.natures_remedy");
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
        public boolean validateOnApply(IMKEntityData targetData, MKActiveEffect activeEffect) {
            return particles != null;
        }

        @Override
        public boolean performEffect(IMKEntityData targetData, MKActiveEffect activeEffect) {
            source = findEntity(source, activeEffect.getSourceId(), targetData);

            LivingEntity target = targetData.getEntity();

            float value = getScaledValue(activeEffect.getStackCount());
            MKHealSource heal = MKHealSource.getNatureHeal(activeEffect.getAbilityId(), source, source, getModifierScale());
            MKHealing.healEntityFrom(target, value, heal);
            PacketHandler.sendToTrackingAndSelf(new MKParticleEffectSpawnPacket(
                    new Vector3d(0.0, 1.0, 0.0), particles, target.getEntityId()), target);
            return true;
        }
    }
}
