package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.effects.MKEffect;
import com.chaosbuffalo.mkcore.effects.MKEffectBuilder;
import com.chaosbuffalo.mkcore.effects.MKEffectState;
import com.chaosbuffalo.mkcore.effects.MKSimplePassiveState;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;


public class YaupEffect extends MKEffect {
    private static final UUID hasteUUID = UUID.fromString("ffa941c5-ee35-48f3-a30b-56ca67af695f");
    private static final UUID dmgUUID = UUID.fromString("19e97391-a2cc-4883-9310-82784f642b9f");

    public static final YaupEffect INSTANCE = new YaupEffect();


    public static MKEffectBuilder<?> from(LivingEntity source, float skillLevel, int duration) {
        return INSTANCE.builder(source).skillLevel(skillLevel).timed(duration);
    }

    public YaupEffect() {
        super(EffectType.BENEFICIAL);
        setRegistryName(MKUltra.MODID, "effect.yaup");
        addAttribute(Attributes.ATTACK_SPEED, hasteUUID, 0.1, 0.02, AttributeModifier.Operation.MULTIPLY_TOTAL, MKAttributes.ARETE);
        addAttribute(Attributes.ATTACK_DAMAGE, dmgUUID, 0.2, 0.01, AttributeModifier.Operation.MULTIPLY_TOTAL, MKAttributes.ARETE);
    }


    @Override
    public MKEffectState makeState() {
        return MKSimplePassiveState.INSTANCE;
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
