package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.damage.MKDamageSource;
import com.chaosbuffalo.mkcore.effects.*;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.MKUAbilityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.potion.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class FlameWaveEffect extends MKEffect {

    public static final FlameWaveEffect INSTANCE = new FlameWaveEffect();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKEffect> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static MKEffectBuilder<?> from(Entity source, float baseDamage, float scaling, float modifierScaling,
                                          int witherBase, int witherScale, float damageMultiplier) {
        return INSTANCE.builder(source.getUniqueID()).state(s -> {
            s.witherDurationBase = witherBase;
            s.witherDurationScale = witherScale;
            s.damageBoost = damageMultiplier;
            s.setScalingParameters(baseDamage, scaling, modifierScaling);
        });
    }

    private FlameWaveEffect() {
        super(EffectType.HARMFUL);
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

    public static class State extends ScalingValueEffectState {
        private Entity source;
        public int witherDurationBase;
        public int witherDurationScale;
        public float damageBoost;

        @Override
        public boolean performEffect(IMKEntityData targetData, MKActiveEffect activeEffect) {
            source = findEntity(source, activeEffect.getSourceId(), targetData);

            float damage = getScaledValue(activeEffect.getStackCount());
            if (MKUAbilityUtils.isBurning(targetData.getEntity())) {
                int dur = witherDurationBase + activeEffect.getStackCount() * witherDurationScale;
                EffectInstance witherEffect = new EffectInstance(Effects.WITHER, dur * GameConstants.TICKS_PER_SECOND, 0);
                damage *= damageBoost;
                targetData.getEntity().addPotionEffect(witherEffect);
            }

            targetData.getEntity().attackEntityFrom(MKDamageSource.causeAbilityDamage(CoreDamageTypes.FireDamage,
                    activeEffect.getAbilityId(), source, source, getModifierScale()), damage);
            return true;
        }
    }
}
