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
public class BlademasterPotion extends PassiveAbilityPotionBase {

    public static final UUID MODIFIER_ID = UUID.fromString("ed2eedd2-8796-4171-aff8-834a812bd437");

    public static final BlademasterPotion INSTANCE = (BlademasterPotion) (new BlademasterPotion()
            .registerPotionAttributeModifier(PlayerAttributes.MELEE_CRITICAL_DAMAGE, MODIFIER_ID.toString(),
                    0.50, PlayerAttributes.OP_INCREMENT)
            .registerPotionAttributeModifier(PlayerAttributes.MELEE_CRIT, MODIFIER_ID.toString(),
                    0.05, PlayerAttributes.OP_INCREMENT)
    );


    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private BlademasterPotion() {
        super();
        setPotionName("effect.blademaster");
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/blademaster.png");
    }
}