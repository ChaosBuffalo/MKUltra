package com.chaosbuffalo.mkultra.entities.projectiles;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.MobFireballEffectPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityMobFireballProjectile extends EntityBaseProjectile{

    public EntityMobFireballProjectile(World worldIn) {
        super(worldIn);
    }

    public EntityMobFireballProjectile(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
        this.setDeathTime(GameConstants.TICKS_PER_SECOND * 5);
    }

    public EntityMobFireballProjectile(World worldIn, EntityLivingBase throwerIn, double offset) {
        super(worldIn, throwerIn, offset);
        this.setDeathTime(GameConstants.TICKS_PER_SECOND * 5);
    }

    public EntityMobFireballProjectile(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    public float getGravityVelocity() {
        return 0.00F;
    }


    @Override
    protected boolean onImpact(EntityLivingBase entity, RayTraceResult result, int level) {

        if (world.isRemote) {
            // No client code
            return false;
        }

        if (entity != null && result.entityHit != null && result.entityHit instanceof EntityLivingBase) {
            EntityLivingBase targetEntity = (EntityLivingBase) result.entityHit;
            SpellCast projectileEffect = MobFireballEffectPotion.Create(entity, 4.0f, .75f);


            AreaEffectBuilder.Create(entity, this)
                    .spellCast(projectileEffect, level, Targeting.TargetType.ENEMY)
                    .instant()
                    .color(16737330).radius(2.0f, true)
                    .spawn();

            Vec3d lookVec = entity.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.DRIP_LAVA.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 20, 4,
                            targetEntity.posX, targetEntity.posY + 1.0,
                            targetEntity.posZ, 0.25f, 0.25f, 0.25f, 0.25f,
                            lookVec),
                    entity.dimension, targetEntity.posX,
                    targetEntity.posY, targetEntity.posZ, 50.0f);
            return true;
        } else if (entity != null) {
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.FIREWORKS_SPARK.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 15, 3,
                            result.hitVec.x, result.hitVec.y + 1.0,
                            result.hitVec.z, 0.25f, 0.25f, 0.25f, 0.25f,
                            new Vec3d(0., 1.0, 0.0)),
                    entity.dimension, result.hitVec.x,
                    result.hitVec.y, result.hitVec.z, 50.0f);
            return true;
        }
        return false;
    }

    @Override
    protected boolean isValidEntityTarget(Entity entity) {
        if (entity instanceof EntityLivingBase && getThrower() != null) {
            return Targeting.isValidTarget(Targeting.TargetType.ENEMY, getThrower(), entity, true);
        }
        return super.isValidEntityTarget(entity);
    }
}
