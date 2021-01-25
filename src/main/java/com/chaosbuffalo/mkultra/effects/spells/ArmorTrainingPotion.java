//package com.chaosbuffalo.mkultra.effects.spells;
//
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.effects.SpellCast;
//import com.chaosbuffalo.mkultra.effects.passives.PassiveAbilityPotionBase;
//import net.minecraft.entity.Entity;
//import net.minecraft.potion.Potion;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//@Mod.EventBusSubscriber(modid = MKUltra.MODID)
//public class ArmorTrainingPotion extends PassiveAbilityPotionBase {
//    public static ArmorTrainingPotion INSTANCE = new ArmorTrainingPotion();
//
//    @SubscribeEvent
//    public static void register(RegistryEvent.Register<Potion> event) {
//        event.getRegistry().register(INSTANCE.finish());
//    }
//
//    public static SpellCast Create(Entity source) {
//        return INSTANCE.newSpellCast(source);
//    }
//
//    private ArmorTrainingPotion() {
//        super();
//        setPotionName("effect.armor_training");
//    }
//
//    @Override
//    public ResourceLocation getIconTexture() {
//        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/armor_training.png");
//    }
//}
