package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.CastState;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.entities.projectiles.EntitySpiritBombProjectile;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Jacob on 7/28/2018.
 */
public class SpiritBomb extends PlayerAbility {

    public final static float BASE = 6.0f;
    public final static float SCALE = 6.0f;

    public final static float PROJECTILE_SPEED = 1.5f;
    public final static float PROJECTILE_INACCURACY = 0.2f;

    public SpiritBomb() {
        super(MKUltra.MODID, "ability.spirit_bomb");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 16 - 4 * currentRank;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 4 + 2 * currentRank;
    }

    @Override
    public int getCastTime(int currentRank) {
        return GameConstants.TICKS_PER_SECOND + (currentRank * GameConstants.TICKS_PER_SECOND / 4);
    }

    @Override
    public void endCast(EntityPlayer entity, IPlayerData data, World theWorld, CastState state) {
        super.endCast(entity, data, theWorld, state);
        int level = data.getAbilityRank(getAbilityId());
        EntitySpiritBombProjectile ballP = new EntitySpiritBombProjectile(theWorld, entity);
        ballP.setAmplifier(level);
        ballP.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, PROJECTILE_SPEED,
                PROJECTILE_INACCURACY);
        theWorld.spawnEntity(ballP);
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 4 + currentRank * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);
    }
}
