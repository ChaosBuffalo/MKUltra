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
//@Mod.EventBusSubscriber(modid = MKUltra.MODID)
//
//public class MobFireballEffectPotion extends SpellPotionBase {
//
//    public static final MobFireballEffectPotion INSTANCE = new MobFireballEffectPotion();
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
//    private MobFireballEffectPotion() {
//        super(true, 123);
//        setPotionName("effect.mob_fireball");
//    }
//
//    @Override
//    public Targeting.TargetType getTargetType() {
//        return Targeting.TargetType.ENEMY;
//    }
//
//    @Override
//    public void doEffect(Entity applier, Entity caster, EntityLivingBase target, int amplifier, SpellCast cast) {
//
//        float damage = cast.getScaledValue(amplifier);
//        if (target.isBurning()) {
//            damage = damage * 2.0f;
//        }
//        target.setFire(1);
//        target.attackEntityFrom(MKDamageSource.causeIndirectMagicDamage(
//                new ResourceLocation(MKUltra.MODID, "mob_fireball"), applier, caster), damage);
//    }
//}
