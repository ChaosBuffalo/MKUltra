package com.chaosbuffalo.mkultra.entities.projectiles;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.effects.spells.LightningDamagePotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Jacob on 7/21/2016.
 */
public class EntityBallLightningProjectile extends EntityBaseProjectile {
    public EntityBallLightningProjectile(World worldIn)
    {
        super(worldIn);
    }

    public EntityBallLightningProjectile(World worldIn, EntityLivingBase throwerIn)
    {
        super(worldIn, throwerIn);
        this.setDoAirProc(true);
        this.setAirProcTime(7);
        this.setDeathTime(30);
    }

    public EntityBallLightningProjectile(World worldIn, double x, double y, double z)
    {
        super(worldIn, x, y, z);
    }

    @Override
    public float getGravityVelocity()
    {
        return 0.00F;
    }


    @Override
    protected boolean onImpact(EntityLivingBase entity, RayTraceResult result, int level) {

        if (world.isRemote) {
            // No client code
            return false;
        }

        if (result.entityHit != null && entity instanceof EntityPlayer && result.entityHit instanceof EntityLivingBase){
            EntityLivingBase targetEntity = (EntityLivingBase)result.entityHit;
            SpellCast ballLightning = LightningDamagePotion.Create(entity, 5.0f, 5.0f);

            AreaEffectBuilder.Create(entity, this)
                    .spellCast(ballLightning, level, Targeting.TargetType.ENEMY)
                    .instant()
                    .color(16769280).radius(4.0f, true)
                    .spawn();

            Vec3d lookVec = entity.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.FIREWORKS_SPARK.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 20, 6,
                            targetEntity.posX, targetEntity.posY + 1.0,
                            targetEntity.posZ, 0.5f, 0.5f, 0.5f, 0.5f,
                            lookVec),
                    entity.dimension, targetEntity.posX,
                    targetEntity.posY, targetEntity.posZ, 50.0f);
        } else if (entity != null){
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.FIREWORKS_SPARK.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 20, 6,
                            result.hitVec.x, result.hitVec.y + 1.0,
                            result.hitVec.z, 0.5f, 0.5f, 0.5f, 0.5f,
                            new Vec3d(0., 1.0, 0.0)),
                    entity.dimension, result.hitVec.x,
                    result.hitVec.y, result.hitVec.z, 50.0f);
        }

        return true;
    }

    @Override
    protected boolean isValidEntityTarget(Entity entity) {
        if (entity instanceof EntityLivingBase && getThrower() != null) {
            return Targeting.isValidTarget(Targeting.TargetType.ENEMY, getThrower(), (EntityLivingBase) entity, false);
        }
        return super.isValidEntityTarget(entity);
    }


    @Override
    protected void onAirProc(EntityLivingBase caster, int amplifier) {
        if (caster != null){
            SpellCast ballLightning = LightningDamagePotion.Create(caster, 4.0f, 4.0f);
            this.setTicksInAir(0);
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.FIREWORKS_SPARK.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 15, 5,
                            this.posX, this.posY, this.posZ, 0.25f, 0.25f, 0.25f, 0.25f,
                            new Vec3d(0., 1.0, 0.0)),
                    caster.dimension, this.posX, this.posY, this.posZ, 50.0f);
            AreaEffectBuilder.Create(caster, this)
                    .spellCast(ballLightning, amplifier, Targeting.TargetType.ENEMY)
                    .instant()
                    .color(16769280).radius(3.0f, true)
                    .spawn();

        }

    }
}
