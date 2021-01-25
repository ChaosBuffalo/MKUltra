//package com.chaosbuffalo.mkultra.effects.spells;
//
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.core.PlayerAttributes;
//import com.chaosbuffalo.mkultra.effects.PassiveEffect;
//import com.chaosbuffalo.mkultra.effects.SpellCast;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.SharedMonsterAttributes;
//import net.minecraft.potion.Potion;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//import java.util.UUID;
//
//
//@Mod.EventBusSubscriber(modid = MKUltra.MODID)
//public class HellfireSmoke extends PassiveEffect {
//    public static final UUID MODIFIER_ID = UUID.fromString("6d949d1a-9d83-4985-867a-f82f2568d868");
//    public static final HellfireSmoke INSTANCE = (HellfireSmoke) (new HellfireSmoke()
//            .registerPotionAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED,
//                    MODIFIER_ID.toString(), .25, PlayerAttributes.OP_SCALE_MULTIPLICATIVE)
//            .registerPotionAttributeModifier(PlayerAttributes.MELEE_CRIT,
//                    MODIFIER_ID.toString(), .05, PlayerAttributes.OP_INCREMENT)
//    );
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
//    private HellfireSmoke() {
//        super(false, 1665535);
//        setPotionName("effect.hellfire_smoke");
//    }
//
//    @Override
//    public ResourceLocation getIconTexture() {
//        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/hellfire_smoke.png");
//    }
//}
