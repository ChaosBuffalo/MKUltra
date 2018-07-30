package com.chaosbuffalo.mkultra.entities.projectiles;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.FlameBladeEffectPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Jacob on 4/21/2018.
 */
public class EntityFlameBladeProjectile extends EntityBaseProjectile{

    public EntityFlameBladeProjectile(World worldIn) {
        super(worldIn);
    }

    public EntityFlameBladeProjectile(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
        this.setDeathTime(10);
    }

    public EntityFlameBladeProjectile(World worldIn, double x, double y, double z) {
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

        if (result.entityHit != null && entity instanceof EntityPlayer && result.entityHit instanceof EntityLivingBase) {
            EntityLivingBase targetEntity = (EntityLivingBase) result.entityHit;
            SpellCast flameblade_projectile = FlameBladeEffectPotion.Create(entity, 1.0f, 1.0f);


            AreaEffectBuilder.Create(entity, this)
                    .spellCast(flameblade_projectile, level, Targeting.TargetType.ENEMY)
                    .instant()
                    .color(16737330).radius(.5f, true)
                    .spawn();

            Vec3d lookVec = entity.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.DRIP_LAVA.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 4, 4,
                            targetEntity.posX, targetEntity.posY + 1.0,
                            targetEntity.posZ, 0.25f, 0.25f, 0.25f, 0.25f,
                            lookVec),
                    entity.dimension, targetEntity.posX,
                    targetEntity.posY, targetEntity.posZ, 50.0f);
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
        }

        return false;
    }

    @Override
    protected boolean isValidEntityTarget(Entity entity) {
        if (entity instanceof EntityLivingBase && getThrower() != null) {
            return Targeting.isValidTarget(Targeting.TargetType.ENEMY, getThrower(), entity, false);
        }
        return super.isValidEntityTarget(entity);
    }
}
