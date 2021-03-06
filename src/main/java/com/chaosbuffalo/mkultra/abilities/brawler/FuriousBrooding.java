//package com.chaosbuffalo.mkultra.abilities.brawler;
//
//import com.chaosbuffalo.mkultra.GameConstants;
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.core.IPlayerData;
//import com.chaosbuffalo.mkultra.core.PlayerAbility;
//import com.chaosbuffalo.mkultra.core.PlayerFormulas;
//import com.chaosbuffalo.mkultra.fx.ParticleEffects;
//import com.chaosbuffalo.mkultra.init.ModSounds;
//import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
//import com.chaosbuffalo.targeting_api.Targeting;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.init.MobEffects;
//import net.minecraft.potion.PotionEffect;
//import net.minecraft.util.EnumParticleTypes;
//import net.minecraft.util.SoundEvent;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.World;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//import javax.annotation.Nullable;
//
//@Mod.EventBusSubscriber(modid = MKUltra.MODID)
//public class FuriousBrooding extends PlayerAbility {
//    public static FuriousBrooding INSTANCE = new FuriousBrooding();
//
//    @SubscribeEvent
//    public static void register(RegistryEvent.Register<PlayerAbility> event) {
//        event.getRegistry().register(INSTANCE.setRegistryName(INSTANCE.getAbilityId()));
//    }
//
//    private FuriousBrooding() {
//        super(MKUltra.MODID, "ability.furious_brooding");
//    }
//
//    @Override
//    public int getCooldown(int currentRank) {
//        return 18;
//    }
//
//    @Override
//    public Targeting.TargetType getTargetType() {
//        return Targeting.TargetType.SELF;
//    }
//
//    @Override
//    public float getManaCost(int currentRank) {
//        return 4 + 2 * currentRank;
//    }
//
//    @Override
//    public float getDistance(int currentRank) {
//        return 1.0f;
//    }
//
//    @Nullable
//    @Override
//    public SoundEvent getSpellCompleteSoundEvent() {
//        return ModSounds.spell_negative_effect_7;
//    }
//
//    @Override
//    public int getRequiredLevel(int currentRank) {
//        return currentRank * 2;
//    }
//
//    @Override
//    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
//
//        int level = pData.getAbilityRank(getAbilityId());
//
//        pData.startAbility(this);
//
//        int duration = (6 + 5 * level) * GameConstants.TICKS_PER_SECOND;
//        duration = PlayerFormulas.applyBuffDurationBonus(pData, duration);
//
//        entity.addPotionEffect(
//                new PotionEffect(MobEffects.REGENERATION, duration, level + 1, false, true));
//        entity.addPotionEffect(
//                new PotionEffect(MobEffects.SLOWNESS, duration, level - 1, false, true));
//
//        Vec3d lookVec = entity.getLookVec();
//        MKUltra.packetHandler.sendToAllAround(
//                new ParticleEffectSpawnPacket(
//                        EnumParticleTypes.VILLAGER_ANGRY.getParticleID(),
//                        ParticleEffects.DIRECTED_SPOUT, 60, 1,
//                        entity.posX, entity.posY + 1.0,
//                        entity.posZ, 1.0, 1.0, 1.0, 1.0,
//                        lookVec),
//                entity.dimension, entity.posX,
//                entity.posY, entity.posZ, 50.0f);
//    }
//}
//
//
//
