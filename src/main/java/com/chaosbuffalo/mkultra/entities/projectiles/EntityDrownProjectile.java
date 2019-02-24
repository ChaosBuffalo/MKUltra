package com.chaosbuffalo.mkultra.entities.projectiles;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.spells.DrownPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.utils.EnvironmentUtils;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

/**
 * Created by Jacob on 7/14/2016.
 */
public class EntityDrownProjectile extends EntityBaseProjectile {
    public EntityDrownProjectile(World worldIn) {
        super(worldIn);
    }

    public EntityDrownProjectile(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);

        this.setDeathTime(40);
    }

    public EntityDrownProjectile(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    protected Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    protected boolean shouldExcludeCaster() {
        return false;
    }


    @Override
    protected boolean onImpact(EntityLivingBase entity, RayTraceResult result, int level) {

        if (world.isRemote) {
            // No client code
            return false;
        }

        if (entity instanceof EntityPlayer && result.entityHit instanceof EntityLivingBase) {
            EntityLivingBase targetEntity = (EntityLivingBase) result.entityHit;
            targetEntity.addPotionEffect(DrownPotion.Create(entity).setTarget(targetEntity)
                    .toPotionEffect(GameConstants.TICKS_PER_SECOND * 3, level));

        }
        if (entity != null) {
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.WATER_BUBBLE.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 30, 10,
                            result.hitVec.x, result.hitVec.y + 1.0,
                            result.hitVec.z, 1.0, 1.0, 1.0, 1.0,
                            new Vec3d(0., 1.0, 0.0)),
                    entity.dimension, result.hitVec.x,
                    result.hitVec.y, result.hitVec.z, 50.0f);
            switch (result.typeOfHit){
                case BLOCK:
                    EnvironmentUtils.putOutFires(entity.getEntityWorld(), result.getBlockPos(), new Vec3i(10, 10, 10));
                    break;
                case ENTITY:
                    EnvironmentUtils.putOutFires(entity.getEntityWorld(), result.entityHit.getPosition(),
                            new Vec3i(10, 10, 10));
                    break;
                case MISS:
                    break;
            }
        }

        return true;
    }
}
