//package com.chaosbuffalo.mkultra.effects.spells;
//
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.effects.SpellCast;
//import com.chaosbuffalo.mkultra.effects.SpellPotionBase;
//import com.chaosbuffalo.mkultra.init.ModSounds;
//import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
//import com.chaosbuffalo.mkultra.utils.AbilityUtils;
//import com.chaosbuffalo.targeting_api.Targeting;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.potion.Potion;
//import net.minecraft.util.SoundCategory;
//import net.minecraft.util.SoundEvent;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//import net.minecraftforge.fml.common.registry.ForgeRegistries;
//
//
//@Mod.EventBusSubscriber(modid = MKUltra.MODID)
//public class SoundPotion extends SpellPotionBase {
//
//    public static final SoundPotion INSTANCE = new SoundPotion();
//
//
//    @SubscribeEvent
//    public static void register(RegistryEvent.Register<Potion> event) {
//        event.getRegistry().register(INSTANCE.finish());
//    }
//
//    public static SpellCast Create(Entity source, SoundEvent event, float pitch, float volume,
//                                   SoundCategory cat) {
//        SpellCast cast = INSTANCE.newSpellCast(source);
//        INSTANCE.setParameters(cast, event, pitch, volume, cat);
//        return cast;
//    }
//
//    public static SpellCast Create(Entity source, SoundEvent event, SoundCategory cat){
//        return Create(source, event, 1.0f, 1.0f, cat);
//    }
//
//
//    protected SoundPotion() {
//        super(true, 123);
//        setPotionName("effect.sound_potion");
//    }
//
//    private SoundPotion setParameters(SpellCast cast, SoundEvent event, float pitch, float volume,
//                                      SoundCategory cat) {
//
//        cast.setResourceLocation("soundEvent", event.getRegistryName());
//        cast.setFloat("volume", volume);
//        cast.setFloat("pitch", pitch);
//        cast.setInt("category", cat.ordinal());
//        return this;
//    }
//
//    @Override
//    public Targeting.TargetType getTargetType() {
//        return Targeting.TargetType.ALL;
//    }
//
//    @Override
//    public boolean canSelfCast() {
//        // Since this can be configured per-cast we return true here and then filter in doEffect where we have the
//        // SpellCast object carrying the state
//        return true;
//    }
//
//    @Override
//    public void doEffect(Entity applier, Entity caster,
//                         EntityLivingBase target, int amplifier, SpellCast cast) {
//
//        SoundEvent event = ForgeRegistries.SOUND_EVENTS.getValue(cast.getResourceLocation("soundEvent"));
//        if (event != null){
//            AbilityUtils.playSoundAtServerEntity(target, event, SoundCategory.values()[cast.getInt("category")],
//                    cast.getFloat("volume"), cast.getFloat("pitch"));
//        }
//    }
//}