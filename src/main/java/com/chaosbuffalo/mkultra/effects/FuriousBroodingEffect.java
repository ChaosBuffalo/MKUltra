package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkcore.GameConstants;
import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.core.healing.MKHealSource;
import com.chaosbuffalo.mkcore.core.healing.MKHealing;
import com.chaosbuffalo.mkcore.effects.*;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

public class FuriousBroodingEffect extends MKEffect {
    public static final int DEFAULT_PERIOD = GameConstants.TICKS_PER_SECOND;

    private static final UUID modUUID = UUID.fromString("06bbfb88-d53e-4565-964c-4642b9165c6d");
    public static final FuriousBroodingEffect INSTANCE = new FuriousBroodingEffect();

    protected FuriousBroodingEffect() {
        super(MobEffectCategory.BENEFICIAL);
        setRegistryName(MKUltra.MODID, "effect.furious_brooding");
        addAttribute(Attributes.MOVEMENT_SPEED, modUUID, -0.60, 0.05, AttributeModifier.Operation.MULTIPLY_TOTAL, MKAttributes.PNEUMA);
    }

    public static MKEffectBuilder<?> from(LivingEntity source, float baseHealing, float scaling, float modifierScaling,
                                          ResourceLocation castParticles) {
        return INSTANCE.builder(source)
                .state(s -> {
                    s.setEffectParticles(castParticles);
                    s.setScalingParameters(baseHealing, scaling, modifierScaling);
                })
                .periodic(DEFAULT_PERIOD);
    }

    @Override
    public FuriousBroodingEffect.State makeState() {
        return new FuriousBroodingEffect.State();
    }

    @Override
    public MKEffectBuilder<FuriousBroodingEffect.State> builder(UUID sourceId) {
        return new MKEffectBuilder<>(this, sourceId, this::makeState);
    }

    @Override
    public MKEffectBuilder<FuriousBroodingEffect.State> builder(LivingEntity sourceEntity) {
        return new MKEffectBuilder<>(this, sourceEntity, this::makeState);
    }

    public static class State extends ScalingValueEffectState {

        @Override
        public boolean performEffect(IMKEntityData targetData, MKActiveEffect activeEffect) {
            float healing = getScaledValue(activeEffect.getStackCount(), activeEffect.getSkillLevel());
            LivingEntity target = targetData.getEntity();
            MKHealing.healEntityFrom(target, healing, MKHealSource.getNatureHeal(activeEffect.getAbilityId(),
                    activeEffect.getDirectEntity(), activeEffect.getSourceEntity(), getModifierScale()));
            sendEffectParticles(targetData.getEntity());
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