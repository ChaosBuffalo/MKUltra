package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.damage.MKDamageSource;
import com.chaosbuffalo.mkcore.effects.*;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.nether_mage.EmberAbility;
import com.chaosbuffalo.mkultra.init.MKUAbilities;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

public class IgniteEffect extends MKEffect {

    public static final IgniteEffect INSTANCE = new IgniteEffect();

    public static MKEffectBuilder<?> from(LivingEntity source, float baseDamage, float scaling, float modifierScaling) {
        return INSTANCE.builder(source)
                .state(s -> s.setScalingParameters(baseDamage, scaling, modifierScaling));
    }

    private IgniteEffect() {
        super(MobEffectCategory.HARMFUL);
        setRegistryName("effect.ignite");
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
        public boolean performEffect(IMKEntityData targetData, MKActiveEffect activeEffect) {

            float damage = getScaledValue(activeEffect.getStackCount(), activeEffect.getSkillLevel());
            float scaling = getModifierScale();
            targetData.getEntity().hurt(MKDamageSource.causeAbilityDamage(CoreDamageTypes.FireDamage,
                    activeEffect.getAbilityId(), activeEffect.getDirectEntity(), activeEffect.getSourceEntity(), scaling), damage);

            MKCore.getEntityData(activeEffect.getSourceEntity()).ifPresent(casterData -> {
                MKEffectBuilder<?> burn = MKUAbilities.EMBER.get().getBurnCast(casterData, activeEffect.getStackCount())
                        .ability(activeEffect.getAbilityId());
                targetData.getEffects().addEffect(burn);
            });

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
