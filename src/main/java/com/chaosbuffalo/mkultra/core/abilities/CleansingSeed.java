package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.entities.projectiles.EntityCleansingSeedProjectile;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Jacob on 7/28/2018.
 */
public class CleansingSeed extends PlayerAbility {

    public static final CleansingSeed INSTANCE = new CleansingSeed();

    public static float PROJECTILE_SPEED = 1.25f;
    public static float PROJECTILE_INACCURACY = 0.2f;
    public static float BASE_DAMAGE = 4.0f;
    public static float DAMAGE_SCALE = 4.0f;

    public CleansingSeed() {
        super(MKUltra.MODID, "ability.cleansing_seed");
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 8 - 2 * currentLevel;
    }

    @Override
    public int getType() {
        return ACTIVE_ABILITY;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ALL;
    }

    @Override
    public int getManaCost(int currentLevel) {
        return 1 + 2 * currentLevel;
    }


    @Override
    public int getRequiredLevel(int currentLevel) {
        return 4 + currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {

        int level = pData.getLevelForAbility(getAbilityId());
        pData.startAbility(this);
        EntityCleansingSeedProjectile ballP = new EntityCleansingSeedProjectile(theWorld, entity);
        ballP.setAmplifier(level);
        ballP.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, PROJECTILE_SPEED,
                PROJECTILE_INACCURACY);
        theWorld.spawnEntity(ballP);
    }
}


