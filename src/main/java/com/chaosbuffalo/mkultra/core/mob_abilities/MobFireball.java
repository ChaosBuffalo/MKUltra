package com.chaosbuffalo.mkultra.core.mob_abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.BaseMobAbility;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.entities.projectiles.EntityMobFireballProjectile;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class MobFireball extends BaseMobAbility {
    private static float PROJECTILE_SPEED = 1.0f;
    private static float PROJECTILE_INACCURACY = .05f;

    public MobFireball() {
        super(MKUltra.MODID, "mob_ability.fireball");
    }

    @Override
    public int getCooldown() {
        return 3 * GameConstants.TICKS_PER_SECOND;
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
        return 20.0f;
    }

    @Override
    public int getCastTime(){
        return GameConstants.TICKS_PER_SECOND;
    }

    @Override
    public void execute(EntityLivingBase entity, IMobData data, EntityLivingBase target, World theWorld) {
        World world = entity.getEntityWorld();
        EntityMobFireballProjectile flamep = new EntityMobFireballProjectile(world, entity);
        flamep.setAmplifier(data.getMobLevel());
        flamep.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0.0f, PROJECTILE_SPEED,
                PROJECTILE_INACCURACY);
        world.spawnEntity(flamep);
    }
}
