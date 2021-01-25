//package com.chaosbuffalo.mkultra.effects.spells;
//
//import com.chaosbuffalo.mkultra.GameConstants;
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.core.IMobData;
//import com.chaosbuffalo.mkultra.core.MKUMobData;
//import com.chaosbuffalo.mkultra.effects.SpellCast;
//import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
//import com.chaosbuffalo.targeting_api.Targeting;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraft.init.MobEffects;
//import net.minecraft.network.play.server.SPacketEntityVelocity;
//import net.minecraft.potion.Potion;
//import net.minecraft.potion.PotionEffect;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
///**
// * Created by Jacob on 3/25/2018.
// */
//@Mod.EventBusSubscriber(modid = MKUltra.MODID)
//public class HeavingSeasPotion extends SpellPotionBase {
//
//    public static final HeavingSeasPotion INSTANCE = new HeavingSeasPotion();
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
//    private HeavingSeasPotion() {
//        // boolean isBadEffectIn, int liquidColorIn
//        super(true, 1665535);
//        setPotionName("effect.heavingseas");
//    }
//
//    @Override
//    public Targeting.TargetType getTargetType() {
//        return Targeting.TargetType.ENEMY;
//    }
//
//    @Override
//    public boolean canSelfCast() {
//        return true;
//    }
//
//    @Override
//    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {
//
//        int duration = GameConstants.TICKS_PER_SECOND * amplifier;
//        target.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, duration, amplifier, false, true));
//        target.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, duration, amplifier, false, true));
//        IMobData mobData = MKUMobData.get(target);
//        if (mobData != null) {
//            if (!mobData.isBoss()) {
//                target.addVelocity(0.0, amplifier * 1.25f, 0.0);
//            }
//        } else {
//            target.addVelocity(0.0, amplifier * 1.25f, 0.0);
//        }
//        if (target instanceof EntityPlayerMP && !caster.world.isRemote) {
//            ((EntityPlayerMP) target).connection.sendPacket(new SPacketEntityVelocity(target));
//        }
//    }
//}