package com.chaosbuffalo.mkultra.core.abilities.passives;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.PlayerPassiveAbility;
import com.chaosbuffalo.mkultra.effects.passives.PassiveAbilityPotionBase;
import com.chaosbuffalo.mkultra.effects.spells.DualWieldPotion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class DualWield extends PlayerPassiveAbility {

    public static final DualWield INSTANCE = new DualWield();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<PlayerAbility> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public DualWield() {
        super(MKUltra.MODID, "ability.dual_wield");
    }

    @Override
    public PassiveAbilityPotionBase getPassiveEffect() {
        return DualWieldPotion.INSTANCE;
    }
}
