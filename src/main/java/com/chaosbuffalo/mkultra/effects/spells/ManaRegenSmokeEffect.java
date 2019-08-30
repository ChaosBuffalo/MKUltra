package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.chaosbuffalo.mkultra.effects.PassiveEffect;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;


@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class ManaRegenSmokeEffect extends PassiveEffect {
    public static final UUID MODIFIER_ID = UUID.fromString("bcf4e6d6-ea41-40c6-ab3e-02b0c7204a33");
    public static final ManaRegenSmokeEffect INSTANCE = (ManaRegenSmokeEffect) (new ManaRegenSmokeEffect()
            .registerPotionAttributeModifier(PlayerAttributes.MANA_REGEN,
                    MODIFIER_ID.toString(), 1.0, PlayerAttributes.OP_INCREMENT)
    );

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private ManaRegenSmokeEffect() {
        super(false, 1665535);
        setPotionName("effect.mana_regen_smoke");
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/mana_regen_smoke.png");
    }
}