package com.chaosbuffalo.mkultra.entities.projectiles;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.DrownPotion;
import com.chaosbuffalo.mkultra.effects.spells.FairyFirePotion;
import com.chaosbuffalo.mkultra.effects.spells.LightningDamagePotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Jacob on 6/23/2018.
 */
public class EntityFairyFireProjectile extends EntityBaseProjectile {
    public EntityFairyFireProjectile(World worldIn) {
        super(worldIn);
    }

    public EntityFairyFireProjectile(World worldIn, EntityLivingBase throwerIn) {
        super(worldIn, throwerIn);
        this.setDeathTime(200);
    }

    public EntityFairyFireProjectile(World worldIn, double x, double y, double z) {
        super(worldIn, x, y, z);
    }

    @Override
    protected boolean isValidEntityTarget(Entity entity) {
        if (entity instanceof EntityLivingBase && getThrower() != null) {
            return Targeting.isValidTarget(Targeting.TargetType.ENEMY, getThrower(), entity, false);
        }
        return super.isValidEntityTarget(entity);
    }


    @Override
    protected boolean onImpact(EntityLivingBase entity, RayTraceResult result, int level) {

        if (world.isRemote) {
            // No client code
            return false;
        }

        if (entity instanceof EntityPlayer && result.entityHit instanceof EntityLivingBase) {
            EntityLivingBase targetEntity = (EntityLivingBase) result.entityHit;
            SpellCast fairyFire = FairyFirePotion.Create(entity);
            AreaEffectBuilder.Create(entity, this)
                    .spellCast(fairyFire, 10 * GameConstants.TICKS_PER_SECOND, level, Targeting.TargetType.ENEMY)
                    .effect(new PotionEffect(MobEffects.GLOWING, 10 * GameConstants.TICKS_PER_SECOND), Targeting.TargetType.ENEMY)
                    .instant()
                    .color(11540991).radius(3.0f, true)
                    .spawn();

            targetEntity.addPotionEffect(FairyFirePotion.Create(entity).setTarget(targetEntity).toPotionEffect(GameConstants.TICKS_PER_SECOND * 3, level));


            Vec3d lookVec = entity.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.SPELL_INSTANT.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 60, 10,
                            targetEntity.posX, targetEntity.posY + 1.0,
                            targetEntity.posZ, 1.0, 1.0, 1.0, 1.0,
                            lookVec),
                    entity.dimension, targetEntity.posX,
                    targetEntity.posY, targetEntity.posZ, 50.0f);
        } else if (entity != null) {
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.SPELL_INSTANT.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 60, 10,
                            result.hitVec.x, result.hitVec.y + 1.0,
                            result.hitVec.z, 1.0, 1.0, 1.0, 1.0,
                            new Vec3d(0., 1.0, 0.0)),
                    entity.dimension, result.hitVec.x,
                    result.hitVec.y, result.hitVec.z, 50.0f);
        }

        return true;
    }
}
