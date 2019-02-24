package com.chaosbuffalo.mkultra.core.mob_abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.entities.projectiles.EntityGraspingRootsProjectile;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;

public class GraspingRoots extends MobAbility {
    private static float PROJECTILE_SPEED = 1.5f;
    private static float PROJECTILE_INACCURACY = 10.0f;

    public GraspingRoots() {
        super(MKUltra.MODID, "mob_ability.grasping_roots");
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
        return 15.0f;
    }

    @Override
    public void execute(EntityLivingBase entity, IMobData data, EntityLivingBase target, World theWorld) {
        World world = entity.getEntityWorld();
        EntityGraspingRootsProjectile proj = new EntityGraspingRootsProjectile(world, entity);
        proj.setAmplifier(data.getMobLevel());
        double d1 = target.posX - entity.posX;
        double d2 =  target.posY - entity.posY;
        double d3 = target.posZ - entity.posZ;
        proj.shoot(d1, d2, d3, PROJECTILE_SPEED, PROJECTILE_INACCURACY);
        world.spawnEntity(proj);
    }
}