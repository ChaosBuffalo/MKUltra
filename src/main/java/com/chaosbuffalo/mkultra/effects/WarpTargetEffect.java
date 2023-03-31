package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkcore.core.CastInterruptReason;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.effects.MKActiveEffect;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.MKEffectState;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

public class WarpTargetEffect extends MKEffect {

    public static final WarpTargetEffect INSTANCE = new WarpTargetEffect();

    private WarpTargetEffect() {
        super(MobEffectCategory.HARMFUL);
        setRegistryName(MKUltra.MODID, "effect.warp_target");
    }

    public static MKEffectBuilder<?> from(LivingEntity source) {
        return INSTANCE.builder(source);
    }

    @Override
    public State makeState() {
        return new State();
    }

    @Override
    public MKEffectBuilder<State> builder(UUID sourceId) {
        return new MKEffectBuilder<>(this, sourceId, this::makeState);
    }

    public static class State extends MKEffectState {

        @Override
        public boolean performEffect(IMKEntityData targetData, MKActiveEffect activeEffect) {
            // We definitely need the source for this effect so make an attempt to recover the casting entity
            if (!activeEffect.hasSourceEntity()) {
                activeEffect.recoverState(targetData);
            }
            LivingEntity source = activeEffect.getSourceEntity();
            if (source == null) {
                return false;
            }
            Vec3 playerOrigin = source.position();
            Vec3 heading = source.getLookAngle();
            targetData.getAbilityExecutor().interruptCast(CastInterruptReason.Teleport);
            targetData.getEntity().teleportTo(
                    playerOrigin.x + heading.x,
                    playerOrigin.y + heading.y + 1.0,
                    playerOrigin.z + heading.z);
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
