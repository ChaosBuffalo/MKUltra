//package com.chaosbuffalo.mkultra.effects.spells;
//
//import com.chaosbuffalo.mkultra.GameConstants;
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.core.IPlayerData;
//import com.chaosbuffalo.mkultra.core.PlayerFormulas;
//import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
//import com.chaosbuffalo.mkultra.effects.SpellCast;
//import com.chaosbuffalo.mkultra.effects.songs.SongEffect;
//import com.chaosbuffalo.mkultra.init.ModSounds;
//import com.chaosbuffalo.mkultra.utils.AbilityUtils;
//import com.chaosbuffalo.targeting_api.Targeting;
//import net.minecraft.entity.Entity;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.init.MobEffects;
//import net.minecraft.potion.Potion;
//import net.minecraft.potion.PotionEffect;
//import net.minecraft.util.EnumParticleTypes;
//import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.SoundCategory;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
///**
// * Created by Jacob on 4/22/2018.
// */
//@Mod.EventBusSubscriber(modid = MKUltra.MODID)
//public class MileysInspiringBangerzPotion extends SongEffect {
//    public static final MileysInspiringBangerzPotion INSTANCE = new MileysInspiringBangerzPotion();
//    public static final int PERIOD = 6 * GameConstants.TICKS_PER_SECOND;
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
//    @Override
//    public AreaEffectBuilder prepareAreaEffect(EntityPlayer source, IPlayerData playerData, int level, AreaEffectBuilder builder) {
//        int duration = PlayerFormulas.applyBuffDurationBonus(playerData, PERIOD);
////        AbilityUtils.playSoundAtServerEntity(source, ModSounds.spell_buff_shield_4, SoundCategory.PLAYERS);
//        builder.spellCast(ShieldingPotion.Create(source), duration, level, Targeting.TargetType.FRIENDLY);
//        builder.effect(new PotionEffect(MobEffects.RESISTANCE, duration, level), Targeting.TargetType.FRIENDLY);
//        builder.effect(new PotionEffect(MobEffects.REGENERATION, duration, level), Targeting.TargetType.FRIENDLY);
//        return builder;
//    }
//
//    private MileysInspiringBangerzPotion() {
//        super(PERIOD, false, 16762880);
//        setPotionName("effect.mileys_bangerz");
//    }
//
//    @Override
//    public EnumParticleTypes getSongParticle() {
//        return EnumParticleTypes.SPELL;
//    }
//
//    @Override
//    public ResourceLocation getIconTexture() {
//        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/mileys_bangerz.png");
//    }
//
//
//    @Override
//    public float getSongDistance(int level) {
//        return 2.0f + level * 4.0f;
//    }
//}
//
