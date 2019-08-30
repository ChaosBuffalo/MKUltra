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

/**
 * Created by Jacob on 7/26/2018.
 */
@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class CriticalChancePotion extends PassiveEffect {
    public static final UUID MODIFIER_ID = UUID.fromString("0dcba474-eabd-4d13-86ef-22d9abc2cc8f");
    public static final CriticalChancePotion INSTANCE = (CriticalChancePotion) (new CriticalChancePotion()
            .registerPotionAttributeModifier(PlayerAttributes.MELEE_CRIT, MODIFIER_ID.toString(), .05, PlayerAttributes.OP_INCREMENT)
    );

    @SubscribeEvent
    public static void register(RegistryEvent.Register<Potion> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public static SpellCast Create(Entity source) {
        return INSTANCE.newSpellCast(source);
    }

    private CriticalChancePotion() {
        super(false, 4117247);
        setPotionName("effect.critical_chance");
    }

    @Override
    public ResourceLocation getIconTexture() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/crit_chance.png");
    }
}
