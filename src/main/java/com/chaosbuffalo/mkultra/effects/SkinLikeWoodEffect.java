package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkcore.core.IMKEntityData;
import com.chaosbuffalo.mkcore.core.MKAttributes;
import com.chaosbuffalo.mkcore.core.MKPlayerData;
import com.chaosbuffalo.mkcore.effects.*;
import com.chaosbuffalo.mkultra.MKUltra;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectType;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

public class SkinLikeWoodEffect extends MKEffect {
    public static final SkinLikeWoodEffect INSTANCE = new SkinLikeWoodEffect();

    public final UUID MODIFIER_ID = UUID.fromString("60f31ee6-4a8e-4c35-8746-6c5950187e77");

    private SkinLikeWoodEffect() {
        super(EffectType.BENEFICIAL);
        setRegistryName("effect.skin_like_wood");
        addAttribute(Attributes.ARMOR, MODIFIER_ID, 4, 1, AttributeModifier.Operation.ADDITION,
                MKAttributes.ABJURATION);
        SpellTriggers.ENTITY_HURT_LIVING.registerPreScale(this::playerHurtPreScale);
    }

    @Override
    public MKEffectState makeState() {
        return MKSimplePassiveState.INSTANCE;
    }

    private void playerHurtPreScale(LivingHurtEvent event, DamageSource source, LivingEntity livingTarget,
                                    IMKEntityData targetData) {
        if (targetData.getEffects().isEffectActive(INSTANCE)) {
            if (targetData instanceof MKPlayerData) {
                if (!((MKPlayerData) targetData).getStats().consumeMana(1)) {
                    targetData.getEffects().removeEffect(INSTANCE);
                }
            }
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

