package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.api.Targeting;
import com.chaosbuffalo.mkultra.entities.projectiles.EntityWhirlpoolProjectile;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Jacob on 3/25/2018.
 */
public class Whirlpool extends BaseAbility {

    public static float PROJECTILE_SPEED = 1.25f;
    public static float PROJECTILE_INACCURACY = 0.2f;

    public Whirlpool() {
        super(MKUltra.MODID, "ability.whirlpool");
    }

    @Override
    public String getAbilityName() {
        return "Whirlpool";
    }

    @Override
    public String getAbilityDescription() {
        return "Places a whirlpool on the ground, sucking enemies in and dotting them.";
    }

    @Override
    public String getAbilityType() {
        return "Delayed Projectile";
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/whirlpool.png");
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 12 - 4 * currentLevel;
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
        return 4 + 2*currentLevel;
    }


    @Override
    public int getRequiredLevel(int currentLevel) {
        return currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {

        int level = pData.getLevelForAbility(getAbilityId());
        pData.startAbility(this);
        EntityWhirlpoolProjectile projectile = new EntityWhirlpoolProjectile(theWorld, entity);
        projectile.setAmplifier(level);
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
    }
}


