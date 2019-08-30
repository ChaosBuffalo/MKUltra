package com.chaosbuffalo.mkultra.core.mob_abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MKDamageSource;
import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.utils.RayTraceUtils;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WarpDash extends MobAbility {

    float BASE_DAMAGE = .5f;
    float DAMAGE_SCALE = .5f;

    public WarpDash() {
        super(MKUltra.MODID, "mob_ability.warp_dash");
    }

    @Override
    public int getCooldown() {
        return 10 * GameConstants.TICKS_PER_SECOND;
    }

    @Override
    public MobAbility.AbilityType getAbilityType() {
        return MobAbility.AbilityType.ATTACK;
    }

    @Override
    public float getDistance() {
        return 5.0f;
    }

    @Override
    public int getCastTime() {
        return GameConstants.TICKS_PER_SECOND;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public void execute(EntityLivingBase entity, IMobData data, EntityLivingBase target, World theWorld) {
        if (target != null) {
            int level = data.getMobLevel();
            target.attackEntityFrom(MKDamageSource.fromMeleeSkill(getAbilityId(), entity, entity),
                    BASE_DAMAGE + DAMAGE_SCALE * level);
            Vec3d lookVec = entity.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.SMOKE_LARGE.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 40, 4,
                            entity.posX, entity.posY + 1.0,
                            entity.posZ, 1.0, 1.0, 1.0, .25,
                            lookVec),
                    entity.dimension, target.posX,
                    target.posY, target.posZ, 50.0f);
            Vec3d position = entity.getPositionVector();
            Vec3d movementVec = target.getPositionVector().subtract(position).normalize().scale(5.0f);
            Vec3d teleLoc = target.getPositionVector().add(new Vec3d(movementVec.x, 0.0, movementVec.z));
            RayTraceResult colTrace = RayTraceUtils.rayTraceBlocks(theWorld, target.getPositionVector(),
                    teleLoc, false);
            if (colTrace != null && colTrace.typeOfHit == RayTraceResult.Type.BLOCK) {
                teleLoc = colTrace.hitVec;
            }
            Vec3d lookDelta = teleLoc.subtract(target.getPositionVector().add(new Vec3d(0.0f, .5f, 0.0f))).normalize();
            double pitch = Math.asin(lookDelta.y);
            double yaw = Math.atan2(lookDelta.z, lookDelta.x);
            pitch = pitch * 180.0 / Math.PI;
            yaw = yaw * 180.0 / Math.PI;
            yaw += 90f;
            // Unfortunately setRotation is protected so we have to set these directly.
            entity.rotationYaw = (float) yaw;
            entity.rotationPitch = (float) pitch;
            entity.setPositionAndUpdate(teleLoc.x, teleLoc.y, teleLoc.z);
            entity.addPotionEffect(new PotionEffect(MobEffects.SPEED, 2 * GameConstants.TICKS_PER_SECOND, 2));
        }
    }
}

