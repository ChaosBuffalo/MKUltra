//package com.chaosbuffalo.mkultra.abilities.ultimates;
//
//import com.chaosbuffalo.mkultra.GameConstants;
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.abilities.cast_states.CastState;
//import com.chaosbuffalo.mkultra.core.IPlayerData;
//import com.chaosbuffalo.mkultra.core.PlayerAbility;
//import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
//import com.chaosbuffalo.mkultra.effects.SpellCast;
//import com.chaosbuffalo.mkultra.effects.spells.ClericHealPotion;
//import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
//import com.chaosbuffalo.mkultra.fx.ParticleEffects;
//import com.chaosbuffalo.mkultra.init.ModSounds;
//import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
//import com.chaosbuffalo.targeting_api.Targeting;
//import net.minecraft.entity.player.EntityPlayer;
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
//
//@Mod.EventBusSubscriber
//public class HealingRain extends PlayerAbility {
//
//    public static final HealingRain INSTANCE = new HealingRain();
//
//    public static float BASE_AMOUNT= 2.0f;
//    public static float AMOUNT_SCALE = 1.0f;
//
//    public HealingRain() {
//        super(MKUltra.MODID, "ability.healing_rain");
//    }
//
//    @SubscribeEvent
//    public static void register(RegistryEvent.Register<PlayerAbility> event) {
//        INSTANCE.setRegistryName(INSTANCE.getAbilityId());
//        event.getRegistry().register(INSTANCE);
//    }
//
//    public AbilityType getType() {
//        return AbilityType.Ultimate;
//    }
//
//    @Override
//    public int getCooldown(int currentRank) {
//        return 25 - 5 * currentRank;
//    }
//
//    @Override
//    public Targeting.TargetType getTargetType() {
//        return Targeting.TargetType.FRIENDLY;
//    }
//
//    @Override
//    public float getManaCost(int currentRank) {
//        return 8 + 2 * currentRank;
//    }
//
//    @Override
//    public float getDistance(int currentRank) {
//        return 5.0f + currentRank * 1.0f;
//    }
//
//    @Override
//    public int getRequiredLevel(int currentRank) {
//        return 1;
//    }
//
//    @Override
//    public SoundEvent getCastingSoundEvent() {
//        return ModSounds.casting_rain;
//    }
//
//    @Nullable
//    @Override
//    public SoundEvent getSpellCompleteSoundEvent() {
//        return null;
//    }
//
//    @Override
//    public int getCastTime(int currentRank) {
//        return currentRank * GameConstants.TICKS_PER_SECOND * 2;
//    }
//
//    @Override
//    public void continueCast(EntityPlayer entity, IPlayerData data, World theWorld, int castTimeLeft, CastState state) {
//        super.continueCast(entity, data, theWorld, castTimeLeft, state);
//        int tickSpeed = 5;
//        if (castTimeLeft % tickSpeed == 0){
//            int level = data.getAbilityRank(getAbilityId());
//            SpellCast heal = ClericHealPotion.Create(entity, BASE_AMOUNT, AMOUNT_SCALE);
//            SpellCast particlePotion = ParticlePotion.Create(entity,
//                    EnumParticleTypes.WATER_BUBBLE.getParticleID(),
//                    ParticleEffects.CIRCLE_MOTION, false,
//                    new Vec3d(1.0, 1.0, 1.0),
//                    new Vec3d(0.0, 1.0, 0.0),
//                    10, 0, 1.0);
//
//            float dist = getDistance(level);
//            AreaEffectBuilder.Create(entity, entity)
//                    .spellCast(heal, level, getTargetType())
//                    .spellCast(particlePotion, level, getTargetType())
//                    .instant()
//                    .color(16409620).radius(getDistance(level), true)
//                    .disableParticle()
//                    .spawn();
//
//            Vec3d lookVec = entity.getLookVec();
//            MKUltra.packetHandler.sendToAllAround(
//                    new ParticleEffectSpawnPacket(
//                            EnumParticleTypes.WATER_DROP.getParticleID(),
//                            ParticleEffects.RAIN_EFFECT, 30, 4,
//                            entity.posX, entity.posY + 3.0,
//                            entity.posZ, dist, 0.5, dist, 1.0,
//                            lookVec),
//                    entity, 50.0f);
//        }
//    }
//
//    @Override
//    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
//        pData.startAbility(this);
//    }
//}