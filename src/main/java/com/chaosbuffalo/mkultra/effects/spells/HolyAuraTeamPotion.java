//package com.chaosbuffalo.mkultra.effects.spells;
//
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.core.IPlayerData;
//import com.chaosbuffalo.mkultra.effects.PassiveEffect;
//import com.chaosbuffalo.mkultra.effects.SpellCast;
//import com.chaosbuffalo.mkultra.effects.SpellTriggers;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.potion.Potion;
//import net.minecraft.util.DamageSource;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.event.entity.living.LivingHurtEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//@Mod.EventBusSubscriber(modid = MKUltra.MODID)
//public class HolyAuraTeamPotion extends PassiveEffect {
//    public static final HolyAuraTeamPotion INSTANCE = new HolyAuraTeamPotion();
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
//    private HolyAuraTeamPotion() {
//        super(false, 16762880);
//        setPotionName("effect.holy_aura_team");
//        SpellTriggers.ENTITY_HURT_PLAYER.registerPreScale(this::playerHurtPreScale);
//    }
//
//    private void playerHurtPreScale(LivingHurtEvent event, DamageSource source, EntityPlayer livingTarget,
//                                    IPlayerData targetData) {
//        if (livingTarget.isPotionActive(this)) {
//            if (source.getTrueSource() instanceof EntityLivingBase) {
//                EntityLivingBase entLiv = (EntityLivingBase) source.getTrueSource();
//                if (entLiv.isEntityUndead()) {
//                    event.setAmount(event.getAmount() * .6f);
//                }
//            }
//        }
//    }
//
//
//    @Override
//    public ResourceLocation getIconTexture() {
//        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/holy_aura.png");
//    }
//
//
//}
//
