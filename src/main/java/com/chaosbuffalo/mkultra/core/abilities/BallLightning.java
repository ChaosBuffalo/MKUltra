package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.api.Targeting;
import com.chaosbuffalo.mkultra.entities.projectiles.EntityBallLightningProjectile;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BallLightning extends BaseAbility {

    public static float PROJECTILE_SPEED = 1.0f;
    public static float PROJECTILE_INACCURACY = 0.2f;

    public BallLightning() {
        super(MKUltra.MODID, "ability.ball_lightning");
    }

    @Override
    public String getAbilityName() {
        return "Ball Lightning";
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/balllightning.png");
    }


    @Override
    public String getAbilityDescription() {
        return "Launches a projectile that deals lightning damage";
    }

    @Override
    public String getAbilityType() {
        return "Projectile";
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
    public int getManaCost(int currentLevel) {
        return 1 +  2 * currentLevel;
    }


    @Override
    public int getRequiredLevel(int currentLevel) {
        return currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {

        int level = pData.getLevelForAbility(getAbilityId());
        pData.startAbility(this);
        EntityBallLightningProjectile ballP = new EntityBallLightningProjectile(theWorld, entity);
        ballP.setAmplifier(level);
        ballP.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, PROJECTILE_SPEED,
                PROJECTILE_INACCURACY);
        theWorld.spawnEntity(ballP);
    }
}


