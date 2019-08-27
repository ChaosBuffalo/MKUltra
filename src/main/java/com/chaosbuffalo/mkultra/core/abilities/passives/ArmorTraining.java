package com.chaosbuffalo.mkultra.core.abilities.passives;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.PlayerPassiveAbility;
import com.chaosbuffalo.mkultra.effects.passives.PassiveAbilityPotionBase;
import com.chaosbuffalo.mkultra.effects.spells.ArmorTrainingPotion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MKUltra.MODID)
public class ArmorTraining extends PlayerPassiveAbility {

    public static final ArmorTraining INSTANCE = new ArmorTraining();

    @SubscribeEvent
    public static void register(RegistryEvent.Register<PlayerAbility> event) {
        event.getRegistry().register(INSTANCE.finish());
    }

    public ArmorTraining() {
        super(MKUltra.MODID, "ability.armor_training");
    }

    @Override
    public PassiveAbilityPotionBase getPassiveEffect() {
        return ArmorTrainingPotion.INSTANCE;
    }
}
