package com.chaosbuffalo.mkultra.core.mob_abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.entities.projectiles.EntityWhirlpoolProjectile;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Whirlpool extends MobAbility {
    private static float PROJECTILE_SPEED = 1.25f;
    private static float PROJECTILE_INACCURACY = 2.5f;

    public Whirlpool() {
        super(MKUltra.MODID, "mob_ability.whirlpool");
    }

    @Override
    public int getCooldown() {
        return 20 * GameConstants.TICKS_PER_SECOND;
    }

    @Override
    public AbilityType getAbilityType() {
        return AbilityType.ATTACK;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public float getDistance(){
        return 6.0f;
    }

    @Override
    public void execute(EntityLivingBase entity, IMobData data, EntityLivingBase target, World theWorld) {
        World world = entity.getEntityWorld();
        EntityWhirlpoolProjectile projectile = new EntityWhirlpoolProjectile(theWorld, entity,
                entity.getEyeHeight() / 2.0);
        projectile.setAmplifier(data.getMobLevel() > 5 ? 2 : 1);
        projectile.shoot(entity, entity.rotationPitch,
                entity.rotationYaw, 0.0F, PROJECTILE_SPEED, PROJECTILE_INACCURACY);
        theWorld.spawnEntity(projectile);
        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.WATER_BUBBLE.getParticleID(),
                        ParticleEffects.CIRCLE_PILLAR_MOTION, 40, 10,
                        entity.posX, entity.posY + 0.05,
                        entity.posZ, 1.0, 1.0, 1.0, 1.5,
                        lookVec),
                entity.dimension, entity.posX,
                entity.posY, entity.posZ, 50.0f);
        double d1 = target.posX - entity.posX;
        double d2 =  target.posY - entity.posY;
        double d3 = target.posZ - entity.posZ;
        projectile.shoot(d1, d2, d3, PROJECTILE_SPEED, PROJECTILE_INACCURACY);
        world.spawnEntity(projectile);
    }
}

