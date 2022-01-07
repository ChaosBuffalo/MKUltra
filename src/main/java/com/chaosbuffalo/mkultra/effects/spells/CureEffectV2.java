package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.effects.MKActiveEffect;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.effects.MKEffectState;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CureEffectV2 extends MKEffect {
    public static final CureEffectV2 INSTANCE = new CureEffectV2();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKEffect> event) {
        event.getRegistry().register(INSTANCE);
    }

    private CureEffectV2() {
        super(EffectType.BENEFICIAL);
        setRegistryName(MKUltra.MODID, "effect.cure");
    }

    @Override
    public MKEffectState makeState() {
        return new State();
    }

    public static class State extends MKEffectState {

        @Override
        public boolean performEffect(IMKEntityData targetData, MKActiveEffect activeEffect) {
            ArrayList<EffectInstance> toRemove = new ArrayList<>();
            int count = 0;
            for (EffectInstance effect : targetData.getEntity().getActivePotionEffects()) {
                if (count > activeEffect.getStackCount()) {
                    break;
                }
                if (!effect.getPotion().isBeneficial()) {
                    toRemove.add(effect);
                    count++;
                }
            }
            for (EffectInstance effect : toRemove) {
                targetData.getEntity().removePotionEffect(effect.getPotion());
            }
            return true;
        }
    }
}