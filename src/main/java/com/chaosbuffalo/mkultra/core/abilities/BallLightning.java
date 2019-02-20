package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.entities.projectiles.EntityBallLightningProjectile;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class BallLightning extends PlayerAbility {

    public static float PROJECTILE_SPEED = 1.0f;
    public static float PROJECTILE_INACCURACY = 0.2f;

    public BallLightning() {
        super(MKUltra.MODID, "ability.ball_lightning");
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
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public int getManaCost(int currentRank) {
        return 1 + 2 * currentRank;
    }


    @Override
    public int getRequiredLevel(int currentRank) {
        return currentRank * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {

        int level = pData.getAbilityRank(getAbilityId());
        pData.startAbility(this);
        EntityBallLightningProjectile ballP = new EntityBallLightningProjectile(theWorld, entity);
        ballP.setAmplifier(level);
        ballP.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, PROJECTILE_SPEED,
                PROJECTILE_INACCURACY);
        theWorld.spawnEntity(ballP);
    }
}


