package com.chaosbuffalo.mkultra.core.abilities.druid;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.abilities.cast_states.CastState;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.entities.projectiles.EntityBallLightningProjectile;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BallLightning extends PlayerAbility {

    public static float PROJECTILE_SPEED = 1.0f;
    public static float PROJECTILE_INACCURACY = 0.2f;

    public BallLightning() {
        super(MKUltra.MODID, "ability.ball_lightning");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 8 - 2 * currentRank;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 2 + 2 * currentRank;
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_thunder_8;
    }

    @Override
    public int getCastTime(int currentRank) {
        return GameConstants.TICKS_PER_SECOND - 5 * (currentRank - 1);
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return currentRank * 2;
    }

    @Override
    public void endCast(EntityPlayer entity, IPlayerData data, World theWorld, CastState state) {
        super.endCast(entity, data, theWorld, state);
        int level = data.getAbilityRank(getAbilityId());
        EntityBallLightningProjectile ballP = new EntityBallLightningProjectile(theWorld, entity);
        ballP.setAmplifier(level);
        ballP.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, PROJECTILE_SPEED,
                PROJECTILE_INACCURACY);
        theWorld.spawnEntity(ballP);
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);
    }
}


