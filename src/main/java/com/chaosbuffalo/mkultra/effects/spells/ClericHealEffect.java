package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.healing.MKHealSource;
import com.chaosbuffalo.mkcore.core.healing.MKHealing;
import com.chaosbuffalo.mkcore.effects.MKActiveEffect;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.ScalingValueEffectState;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.targeting_api.TargetingContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;


@Mod.EventBusSubscriber(modid = MKUltra.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClericHealEffect extends MKEffect {

    public static final ClericHealEffect INSTANCE = new ClericHealEffect();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<MKEffect> event) {
        event.getRegistry().register(INSTANCE);
    }

    private ClericHealEffect() {
        super(EffectType.BENEFICIAL);
        setRegistryName(MKUltra.MODID, "effect.cleric_heal");
    }

    @Override
    public boolean isValidTarget(TargetingContext targetContext, IMKEntityData sourceData, IMKEntityData targetData) {
        return super.isValidTarget(targetContext, sourceData, targetData) || MKHealing.isEnemyUndead(targetData.getEntity());
    }

    @Override
    public MKEffectBuilder<State> builder(UUID sourceId) {
        return new MKEffectBuilder<>(this, sourceId, this::makeState);
    }

    @Override
    public State makeState() {
        return new State();
    }

    public static class State extends ScalingValueEffectState {
        private Entity source;

        @Override
        public boolean performEffect(IMKEntityData targetData, MKActiveEffect instance) {
            source = findEntity(source, instance.getSourceId(), targetData);

            LivingEntity target = targetData.getEntity();
            float value = getScaledValue(instance.getStackCount());
//            MKUltra.LOGGER.info("ClericHealEffect.performEffect {} on {} from {} {}", value, target, source, instance);
            MKHealSource heal = MKHealSource.getHolyHeal(instance.getAbilityId(), source, getModifierScale());
            MKHealing.healEntityFrom(target, value, heal);
            return true;
        }
    }
}