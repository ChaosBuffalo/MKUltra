//package com.chaosbuffalo.mkultra.abilities.skald;
//
//import com.chaosbuffalo.mkultra.GameConstants;
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.core.*;
//import com.chaosbuffalo.mkultra.abilities.cast_states.CastState;
//import com.chaosbuffalo.mkultra.abilities.cast_states.SingleTargetCastState;
//import com.chaosbuffalo.mkultra.fx.ParticleEffects;
//import com.chaosbuffalo.mkultra.init.ModSounds;
//import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
//import com.chaosbuffalo.mkultra.utils.AbilityUtils;
//import com.chaosbuffalo.mkultra.utils.RayTraceUtils;
//import com.chaosbuffalo.targeting_api.Targeting;
//import net.minecraft.entity.EntityLivingBase;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.init.MobEffects;
//import net.minecraft.potion.PotionEffect;
//import net.minecraft.util.EnumParticleTypes;
//import net.minecraft.util.SoundCategory;
//import net.minecraft.util.SoundEvent;
//import net.minecraft.util.math.RayTraceResult;
//import net.minecraft.util.math.Vec3d;
//import net.minecraft.world.World;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//import javax.annotation.Nullable;
//
//@Mod.EventBusSubscriber(modid = MKUltra.MODID)
//public class KanyeCutter extends PlayerAbility {
//    public static final KanyeCutter INSTANCE = new KanyeCutter();
//
//    @SubscribeEvent
//    public static void register(RegistryEvent.Register<PlayerAbility> event) {
//        event.getRegistry().register(INSTANCE.setRegistryName(INSTANCE.getAbilityId()));
//    }
//
//    public static float BASE_DAMAGE = 6.0f;
//    public static float DAMAGE_SCALE = 2.0f;
//
//    private KanyeCutter() {
//        super(MKUltra.MODID, "ability.kanye_cutter");
//    }
//
//    @Override
//    public int getCooldown(int currentRank) {
//        return 8 - currentRank;
//    }
//
//    @Override
//    public Targeting.TargetType getTargetType() {
//        return Targeting.TargetType.ENEMY;
//    }
//
//    @Override
//    public float getManaCost(int currentRank) {
//        return 3 + currentRank * 2;
//    }
//
//    @Override
//    public float getDistance(int currentRank) {
//        return 25.0f;
//    }
//
//    @Override
//    public int getRequiredLevel(int currentRank) {
//        return currentRank * 2;
//    }
//
//    @Override
//    public SoundEvent getCastingSoundEvent() {
//        return ModSounds.casting_shadow;
//    }
//
//    @Nullable
//    @Override
//    public SoundEvent getSpellCompleteSoundEvent() {
//        return ModSounds.spell_shadow_10;
//    }
//
//    @Override
//    public int getCastTime(int currentRank) {
//        return GameConstants.TICKS_PER_SECOND / (2 * currentRank);
//    }
//
//    @Override
//    public void endCast(EntityPlayer entity, IPlayerData data, World theWorld, CastState state) {
//        super.endCast(entity, data, theWorld, state);
//        SingleTargetCastState singleTargetState = AbilityUtils.getCastStateAsType(state, SingleTargetCastState.class);
//        if (singleTargetState == null) {
//            return;
//        }
//
//        singleTargetState.getTarget().ifPresent(targetEntity -> {
//            int level = data.getAbilityRank(getAbilityId());
//            if (isValidTarget(entity, targetEntity)) {
//                targetEntity.addPotionEffect(new PotionEffect(MobEffects.WITHER, GameConstants.TICKS_PER_SECOND * 3, level));
//                targetEntity.attackEntityFrom(MKDamageSource.fromMeleeSkill(getAbilityId(), entity, entity),
//                        BASE_DAMAGE + DAMAGE_SCALE * level);
//                AbilityUtils.playSoundAtServerEntity(targetEntity, ModSounds.spell_shadow_8, SoundCategory.PLAYERS);
//            }
//
//            Vec3d position = entity.getPositionVector();
//            Vec3d movementVec = targetEntity.getPositionVector().subtract(position).normalize().scale(5.0f);
//            Vec3d teleLoc = targetEntity.getPositionVector().add(
//                    new Vec3d(movementVec.x, 0.0, movementVec.z));
//            RayTraceResult colTrace = RayTraceUtils.rayTraceBlocks(theWorld, targetEntity.getPositionVector(),
//                    teleLoc, false);
//            if (colTrace != null && colTrace.typeOfHit == RayTraceResult.Type.BLOCK) {
//                teleLoc = colTrace.hitVec;
//            }
//            Vec3d lookDelta = teleLoc.subtract(targetEntity.getPositionVector().add(
//                    new Vec3d(0.0f, .5f, 0.0f))).normalize();
//            double pitch = Math.asin(lookDelta.y);
//            double yaw = Math.atan2(lookDelta.z, lookDelta.x);
//            pitch = pitch * 180.0 / Math.PI;
//            yaw = yaw * 180.0 / Math.PI;
//            yaw += 90f;
//            // Unfortunately setRotation is protected so we have to set these directly.
//            entity.rotationYaw = (float) yaw;
//            entity.rotationPitch = (float) pitch;
//            entity.setPositionAndUpdate(teleLoc.x, teleLoc.y, teleLoc.z);
//            entity.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE,
//                    3 * GameConstants.TICKS_PER_SECOND, 5));
//            AbilityUtils.playSoundAtServerEntity(entity, ModSounds.spell_shadow_11, SoundCategory.PLAYERS);
//            Vec3d lookVec = entity.getLookVec();
//            MKUltra.packetHandler.sendToAllAround(
//                    new ParticleEffectSpawnPacket(
//                            EnumParticleTypes.END_ROD.getParticleID(),
//                            ParticleEffects.SPHERE_MOTION, 60, 10,
//                            targetEntity.posX, targetEntity.posY + 1.0,
//                            targetEntity.posZ, 1.0, 1.0, 1.0, 2.0,
//                            lookVec),
//                    entity.dimension, targetEntity.posX,
//                    targetEntity.posY, targetEntity.posZ, 50.0f);
//        });
//    }
//
//    @Override
//    public CastState createCastState(int castTime) {
//        return new SingleTargetCastState(castTime);
//    }
//
//    @Override
//    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
//        int level = pData.getAbilityRank(getAbilityId());
//
//        EntityLivingBase targetEntity = getSingleLivingTarget(entity, getDistance(level), false);
//        if (targetEntity != null) {
//            CastState castState = pData.startAbility(this);
//            SingleTargetCastState singleTargetState = AbilityUtils.getCastStateAsType(castState,
//                    SingleTargetCastState.class);
//            if (singleTargetState != null) {
//                singleTargetState.setTarget(targetEntity);
//            }
//        }
//    }
//}
