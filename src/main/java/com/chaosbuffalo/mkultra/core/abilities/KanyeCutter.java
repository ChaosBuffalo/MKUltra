package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKDamageSource;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.utils.RayTraceUtils;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class KanyeCutter extends PlayerAbility {

    public static float BASE_DAMAGE = 6.0f;
    public static float DAMAGE_SCALE = 2.0f;

    public KanyeCutter() {
        super(MKUltra.MODID, "ability.kanye_cutter");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 8 - currentRank;
    }

    @Override
    public AbilityType getType() {
        return AbilityType.Active;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public int getManaCost(int currentRank) {
        return 3 + currentRank * 2;
    }

    @Override
    public float getDistance(int currentRank) {
        return 25.0f;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return currentRank * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        int level = pData.getAbilityRank(getAbilityId());

        EntityLivingBase targetEntity = getSingleLivingTarget(entity, getDistance(level), false);
        if (targetEntity != null) {
            pData.startAbility(this);

            if (isValidTarget(entity, targetEntity)) {
                targetEntity.addPotionEffect(new PotionEffect(MobEffects.WITHER, GameConstants.TICKS_PER_SECOND * 3, level));
                targetEntity.attackEntityFrom(MKDamageSource.fromMeleeSkill(getAbilityId(), entity, entity),
                        BASE_DAMAGE + DAMAGE_SCALE * level);
            }

            Vec3d position = entity.getPositionVector();
            Vec3d movementVec = targetEntity.getPositionVector().subtract(position).normalize().scale(5.0f);
            Vec3d teleLoc = targetEntity.getPositionVector().add(new Vec3d(movementVec.x, 0.0, movementVec.z));
            RayTraceResult colTrace = RayTraceUtils.rayTraceBlocks(theWorld, targetEntity.getPositionVector(),
                    teleLoc, false);
            if (colTrace != null && colTrace.typeOfHit == RayTraceResult.Type.BLOCK){
                teleLoc = colTrace.hitVec;
            }
            Vec3d lookDelta = teleLoc.subtract(targetEntity.getPositionVector().add(new Vec3d(0.0f, .5f, 0.0f))).normalize();
            double pitch = Math.asin(lookDelta.y);
            double yaw = Math.atan2(lookDelta.z, lookDelta.x);
            pitch = pitch * 180.0 / Math.PI;
            yaw = yaw * 180.0 / Math.PI;
            yaw += 90f;
            // Unfortunately setRotation is protected so we have to set these directly.
            entity.rotationYaw = (float)yaw;
            entity.rotationPitch = (float)pitch;
            entity.setPositionAndUpdate(teleLoc.x, teleLoc.y, teleLoc.z);
            entity.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 3 * GameConstants.TICKS_PER_SECOND, 5));

            Vec3d lookVec = entity.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.END_ROD.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 60, 10,
                            targetEntity.posX, targetEntity.posY + 1.0,
                            targetEntity.posZ, 1.0, 1.0, 1.0, 2.0,
                            lookVec),
                    entity.dimension, targetEntity.posX,
                    targetEntity.posY, targetEntity.posZ, 50.0f);
        }
    }
}

