package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.chaosbuffalo.mkultra.effects.PassiveAbilityPotionBase;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class ExtendedDurationPotion extends PassiveAbilityPotionBase {

    public static final UUID MODIFIER_ID = UUID.fromString("def04c1a-3bc5-4018-9600-4a76a9994762");

    public static final ExtendedDurationPotion INSTANCE = (ExtendedDurationPotion) (new ExtendedDurationPotion()
            .registerPotionAttributeModifier(PlayerAttributes.BUFF_DURATION, MODIFIER_ID.toString(), 0.5, PlayerAttributes.OP_INCREMENT)
    );


    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private ExtendedDurationPotion() {
        super();
        setPotionName("effect.extended_duration");
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/extended_duration.png");
    }
}