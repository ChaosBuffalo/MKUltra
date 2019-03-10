package com.chaosbuffalo.mkultra.entities.projectiles;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.MKDamageSource;
import com.chaosbuffalo.mkultra.core.abilities.CleansingSeed;
import com.chaosbuffalo.mkultra.effects.spells.CurePotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Jacob on 7/28/2018.
 */
public class EntityCleansingSeedProjectile extends EntityBaseProjectile {
    public EntityCleansingSeedProjectile(World worldIn) {
        super(worldIn);
    }

    public EntityCleansingSeedProjectile(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);

        this.setDeathTime(40);
    }

    public EntityCleansingSeedProjectile(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    protected Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ALL;
    }

    @Override
    protected boolean shouldExcludeCaster() {
        return true;
    }

    @Override
    protected boolean onImpact(EntityLivingBase entity, RayTraceResult result, int level) {

        if (world.isRemote) {
            // No client code
            return false;
        }

        if (entity instanceof EntityPlayer && result.entityHit instanceof EntityLivingBase) {
            EntityLivingBase targetEntity = (EntityLivingBase) result.entityHit;

            if (Targeting.isValidTarget(Targeting.TargetType.FRIENDLY, getThrower(), targetEntity, false)){
                targetEntity.addPotionEffect(CurePotion.Create(entity).setTarget(targetEntity)
                        .toPotionEffect(level));
            } else if (Targeting.isValidTarget(Targeting.TargetType.ENEMY, getThrower(), targetEntity, true)){
                targetEntity.attackEntityFrom(MKDamageSource.causeIndirectMagicDamage(
                        CleansingSeed.INSTANCE.getAbilityId(), this, getThrower()),
                        CleansingSeed.BASE_DAMAGE + level * CleansingSeed.DAMAGE_SCALE);
            }
        }
        if (entity != null) {
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.SLIME.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 30, 10,
                            result.hitVec.x, result.hitVec.y + 1.0,
                            result.hitVec.z, 1.0, 1.0, 1.0, 1.0,
                            new Vec3d(0., 1.0, 0.0)),
                    entity.dimension, result.hitVec.x,
                    result.hitVec.y, result.hitVec.z, 50.0f);
        }

        return true;
    }
}
