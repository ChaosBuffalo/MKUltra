package com.chaosbuffalo.mkultra.core.abilities.passives;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.PlayerPassiveAbility;
import com.chaosbuffalo.mkultra.effects.passives.PassiveAbilityPotionBase;
import com.chaosbuffalo.mkultra.effects.spells.TwoHandedStylePotion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class TwoHandedStyle extends PlayerPassiveAbility {

    public static final TwoHandedStyle INSTANCE = new TwoHandedStyle();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<PlayerAbility> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public TwoHandedStyle() {
        super(MKUltra.MODID, "ability.two_handed_style");
    }

    @Override
    public PassiveAbilityPotionBase getPassiveEffect() {
        return TwoHandedStylePotion.INSTANCE;
    }
}
