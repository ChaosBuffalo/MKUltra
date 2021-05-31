//package com.chaosbuffalo.mkultra.abilities.passives;
//
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.core.PlayerAbility;
//import com.chaosbuffalo.mkultra.core.PlayerPassiveAbility;
//import com.chaosbuffalo.mkultra.effects.passives.PassiveAbilityPotionBase;
//import com.chaosbuffalo.mkultra.effects.spells.HolyAuraPotion;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//@Mod.EventBusSubscriber(modid = MKUltra.MODID)
//public class HolyAura extends PlayerPassiveAbility {
//
//    public static final HolyAura INSTANCE = new HolyAura();
//
//    @SubscribeEvent
//    public static void register(RegistryEvent.Register<PlayerAbility> event) {
//        event.getRegistry().register(INSTANCE.finish());
//    }
//
//    public HolyAura() {
//        super(MKUltra.MODID, "ability.holy_aura");
//    }
//
//    @Override
//    public PassiveAbilityPotionBase getPassiveEffect() {
//        return HolyAuraPotion.INSTANCE;
//    }
//}
