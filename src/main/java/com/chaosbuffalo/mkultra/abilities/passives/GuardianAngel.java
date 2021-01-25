//package com.chaosbuffalo.mkultra.abilities.passives;
//
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.core.PlayerAbility;
//import com.chaosbuffalo.mkultra.core.PlayerPassiveAbility;
//import com.chaosbuffalo.mkultra.effects.passives.PassiveAbilityPotionBase;
//import com.chaosbuffalo.mkultra.effects.spells.GuardianAngelPotion;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//@Mod.EventBusSubscriber
//public class GuardianAngel extends PlayerPassiveAbility {
//
//    public static final GuardianAngel INSTANCE = new GuardianAngel();
//
//    @SubscribeEvent
//    public static void register(RegistryEvent.Register<PlayerAbility> event) {
//        event.getRegistry().register(INSTANCE.finish());
//    }
//
//    public GuardianAngel() {
//        super(MKUltra.MODID, "ability.guardian_angel");
//    }
//
//    @Override
//    public PassiveAbilityPotionBase getPassiveEffect() {
//        return GuardianAngelPotion.INSTANCE;
//    }
//}
