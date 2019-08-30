package com.chaosbuffalo.mkultra.effects.spells;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAttributes;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.passives.PassiveAbilityPotionBase;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class BurningSoulPotion extends PassiveAbilityPotionBase {

    public static final UUID MODIFIER_ID = UUID.fromString("1f7540cb-a9c3-4a11-866d-24547723dd06");

    public static final BurningSoulPotion INSTANCE = (BurningSoulPotion) (new BurningSoulPotion()
            .registerPotionAttributeModifier(PlayerAttributes.SPELL_CRITICAL_DAMAGE, MODIFIER_ID.toString(),
                    1.0, PlayerAttributes.OP_INCREMENT)
            .registerPotionAttributeModifier(PlayerAttributes.SPELL_CRIT, MODIFIER_ID.toString(),
                    0.1, PlayerAttributes.OP_INCREMENT)
    );


    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private BurningSoulPotion() {
        super();
        setPotionName("effect.burning_soul");
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/burning_soul.png");
    }
}