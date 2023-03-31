package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.effects.MKActiveEffect;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.MKEffectState;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

public class CureEffect extends MKEffect {
    public static final CureEffect INSTANCE = new CureEffect();

    private CureEffect() {
        super(MobEffectCategory.BENEFICIAL);
        setRegistryName(MKUltra.MODID, "effect.cure");
    }

    public static MKEffectBuilder<?> from(LivingEntity source) {
        return INSTANCE.builder(source);
    }

    @Override
    public MKEffectState makeState() {
        return new State();
    }

    public static class State extends MKEffectState {

        @Override
        public boolean performEffect(IMKEntityData targetData, MKActiveEffect activeEffect) {
            ArrayList<MobEffectInstance> toRemove = new ArrayList<>();
            int count = 0;
            for (MobEffectInstance effect : targetData.getEntity().getActiveEffects()) {
                if (count > activeEffect.getStackCount()) {
                    break;
                }
                if (!effect.getEffect().isBeneficial()) {
                    toRemove.add(effect);
                    count++;
                }
            }
            for (MobEffectInstance effect : toRemove) {
                targetData.getEntity().removeEffect(effect.getEffect());
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
