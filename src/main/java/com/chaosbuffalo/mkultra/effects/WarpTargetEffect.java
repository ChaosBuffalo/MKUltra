package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.effects.MKActiveEffect;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.MKEffectState;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.entity.Entity;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class WarpTargetEffect extends MKEffect {

    public static final WarpTargetEffect INSTANCE = new WarpTargetEffect();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKEffect> event) {
        event.getRegistry().register(INSTANCE);
    }

    private WarpTargetEffect() {
        super(EffectType.HARMFUL);
        setRegistryName(MKUltra.MODID, "effect.warp_target");
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
        private Entity source;

        @Override
        public boolean performEffect(IMKEntityData targetData, MKActiveEffect activeEffect) {
            source = findEntity(source, activeEffect.getSourceId(), targetData);
            if (source == null) {
                return false;
            }
            Vector3d playerOrigin = source.getPositionVec();
            Vector3d heading = source.getLookVec();
            targetData.getEntity().setPositionAndUpdate(
                    playerOrigin.x + heading.x,
                    playerOrigin.y + heading.y + 1.0,
                    playerOrigin.z + heading.z);
            return true;
        }
    }
}