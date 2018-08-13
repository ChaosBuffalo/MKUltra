package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.chaosbuffalo.mkultra.effects.PassiveEffect;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

/**
 * Created by Jacob on 8/5/2018.
 */

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class HealthRegenSmokeEffect extends PassiveEffect {
    public static final UUID MODIFIER_ID = UUID.fromString("fc94013c-9a25-45f9-a3a9-e1d5404dcdc3");
    public static final HealthRegenSmokeEffect INSTANCE = (HealthRegenSmokeEffect) (new HealthRegenSmokeEffect()
            .registerPotionAttributeModifier(PlayerAttributes.HEALTH_REGEN,
                    MODIFIER_ID.toString(), 1.0, PlayerAttributes.OP_INCREMENT)
    );

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private HealthRegenSmokeEffect() {
        super(false, 1665535);
        setPotionName("effect.health_regen_smoke");
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/health_regen_smoke.png");
    }

    @Override
    public double getAttributeModifierAmount(int amplifier, AttributeModifier modifier) {
        return modifier.getAmount() * (double) (amplifier);
    }
}
