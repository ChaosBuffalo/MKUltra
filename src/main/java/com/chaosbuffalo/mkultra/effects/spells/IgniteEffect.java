package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkcore.MKCore;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.damage.MKDamageSource;
import com.chaosbuffalo.mkcore.effects.*;
import com.chaosbuffalo.mkcore.init.CoreDamageTypes;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.abilities.nether_mage.EmberAbility;
import net.minecraft.entity.Entity;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class IgniteEffect extends MKEffect {

    public static final IgniteEffect INSTANCE = new IgniteEffect();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKEffect> event) {
        event.getRegistry().register(INSTANCE);
    }

    public static MKEffectBuilder<?> from(Entity source, float baseDamage, float scaling, float modifierScaling) {
        return INSTANCE.builder(source.getUniqueID())
                .state(s -> s.setScalingParameters(baseDamage, scaling, modifierScaling));
    }

    private IgniteEffect() {
        super(EffectType.HARMFUL);
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

    public static class State extends ScalingValueEffectState {
        private Entity source;

        @Override
        public boolean performEffect(IMKEntityData targetData, MKActiveEffect activeEffect) {
            source = findEntity(source, activeEffect.getSourceId(), targetData);

            float damage = getScaledValue(activeEffect.getStackCount());
            float scaling = getModifierScale();
            targetData.getEntity().attackEntityFrom(MKDamageSource.causeAbilityDamage(CoreDamageTypes.FireDamage,
                    activeEffect.getAbilityId(), source, source, scaling), damage);

            MKCore.getEntityData(source).ifPresent(casterData -> {
                MKEffectBuilder<?> burn = EmberAbility.INSTANCE.getBurnCast(casterData.getEntity(), casterData, activeEffect.getStackCount())
                        .ability(activeEffect.getAbilityId());
                targetData.getEffects().addEffect(burn);
            });

            return true;
        }
    }
}
