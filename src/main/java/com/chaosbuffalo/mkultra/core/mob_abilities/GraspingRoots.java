package com.chaosbuffalo.mkultra.core.mob_abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.entities.projectiles.EntityGraspingRootsProjectile;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.mkultra.utils.EntityUtils;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class GraspingRoots extends MobAbility {
    private static float PROJECTILE_SPEED = 1.5f;
    private static float PROJECTILE_INACCURACY = 1.0f;

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
    public float getDistance() {
        return 15.0f;
    }

    @Nullable
    @Override
    public SoundEvent getCastingCompleteEvent() {
        return ModSounds.spell_earth_8;
    }

    @Override
    public void execute(EntityLivingBase entity, IMobData data, EntityLivingBase target, World theWorld) {
        World world = entity.getEntityWorld();
        EntityGraspingRootsProjectile proj = new EntityGraspingRootsProjectile(world, entity,
                entity.getEyeHeight() / 2.0);
        proj.setAmplifier(data.getMobLevel());
        boolean result = EntityUtils.shootProjectileAtTarget(proj, target, PROJECTILE_SPEED, PROJECTILE_INACCURACY);
        world.spawnEntity(proj);
        if (result){
            world.spawnEntity(proj);
        } else {
            proj.setDead();
        }
    }
}