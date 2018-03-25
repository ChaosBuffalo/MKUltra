package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.entities.projectiles.EntityDualityRuneProjectile;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Jacob on 3/24/2018.
 */
public class DualityRune extends BaseAbility {

    public static float PROJECTILE_SPEED = 1.5f;
    public static float PROJECTILE_INACCURACY = 0.2f;

    public DualityRune() {
        super(MKUltra.MODID, "ability.duality_rune");
    }

    @Override
    public String getAbilityName() {
        return "Duality Rune";
    }

    @Override
    public String getAbilityDescription() {
        return "Places a rune on the ground that explodes after a second, damaging enemies and healing allies around it.";
    }

    @Override
    public String getAbilityType() {
        return "Delayed Projectile";
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/duality_rune.png");
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 24 - 4 * currentLevel;
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
        return 4 + 4*currentLevel;
    }


    @Override
    public int getRequiredLevel(int currentLevel) {
        return 4 + currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {

        int level = pData.getLevelForAbility(getAbilityId());
        pData.startAbility(this);
        EntityDualityRuneProjectile projectile = new EntityDualityRuneProjectile(theWorld, entity);
        projectile.setAmplifier(level);
        projectile.shoot(entity, entity.rotationPitch,
                entity.rotationYaw, 0.0F, PROJECTILE_SPEED, PROJECTILE_INACCURACY);
        theWorld.spawnEntity(projectile);

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.PORTAL.getParticleID(),
                        ParticleEffects.CIRCLE_PILLAR_MOTION, 40, 10,
                        entity.posX, entity.posY + 0.05,
                        entity.posZ, 1.0, 1.0, 1.0, 1.5,
                        lookVec),
                entity.dimension, entity.posX,
                entity.posY, entity.posZ, 50.0f);
    }
}


