//package com.chaosbuffalo.mkultra.effects.spells;
//
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.core.MKDamageSource;
//import com.chaosbuffalo.mkultra.effects.SpellCast;
//import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
//import com.chaosbuffalo.targeting_api.Targeting;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.potion.Potion;
//import net.minecraft.util.ResourceLocation;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//
//@Mod.EventBusSubscriber(modid = MKUltra.MODID)
//public class HolyDamagePotion extends SpellPotionBase {
//
//    public static ResourceLocation HOLY_DMG_ABILITY_ID = new ResourceLocation(
//            MKUltra.MODID, "ability.holy_damage");
//
//    public static final HolyDamagePotion INSTANCE = new HolyDamagePotion();
//
//    @SubscribeEvent
//    public static void register(RegistryEvent.Register<Potion> event) {
//        event.getRegistry().register(INSTANCE.finish());
//    }
//
//    public static SpellCast Create(Entity source, float baseDamage, float scaling) {
//        return INSTANCE.newSpellCast(source).setScalingParameters(baseDamage, scaling);
//    }
//
//    private HolyDamagePotion() {
//        super(true, 123);
//        setPotionName("effect.holy_damage");
//    }
//
//    @Override
//    public Targeting.TargetType getTargetType() {
//        return Targeting.TargetType.ENEMY;
//    }
//
//    @Override
//    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {
//        if (target.isEntityUndead()) {
//            float damage = cast.getScaledValue(amplifier);
//            target.attackEntityFrom(MKDamageSource.causeIndirectMagicDamage(HOLY_DMG_ABILITY_ID,
//                    applier, caster), damage);
//        }
//    }
//}
