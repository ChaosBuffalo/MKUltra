package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class StunningShout extends BaseAbility {

    public StunningShout() {
        super(MKUltra.MODID, "ability.stunning_shout");
    }

    @Override
    public String getAbilityName() {
        return "Stunning Shout";
    }

    @Override
    public String getAbilityDescription() {
        return "Stuns a single target for 4.0/6.0 seconds.";
    }

    @Override
    public String getAbilityType() {
        return "Single Target";
    }

    @Override
    public int getIconU() {
        return 0;
    }

    @Override
    public int getIconV() {
        return 18;
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/stunningshout.png");
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 10;
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
        return 2 + 2 * currentLevel;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 10.0f;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return 4 + currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        int level = pData.getLevelForAbility(getAbilityId());

        EntityLivingBase targetEntity = getSingleLivingTarget(entity, getDistance(level));
        if (targetEntity != null) {
            pData.startAbility(this);

            targetEntity.addPotionEffect(
                    new PotionEffect(MobEffects.SLOWNESS,
                            (2 + 2 * level) * 20, 100, false, true));
            targetEntity.addPotionEffect(
                    new PotionEffect(MobEffects.BLINDNESS,
                            (2 + 2 * level) * 20, 100, false, true));
            targetEntity.addPotionEffect(
                    new PotionEffect(MobEffects.WEAKNESS,
                            (2 + 2 * level) * 20, 100, false, true));

            Vec3d lookVec = entity.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.BLOCK_DUST.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 60, 10,
                            targetEntity.posX, targetEntity.posY + 1.0,
                            targetEntity.posZ, 1.0, 1.0, 1.0, 2.0,
                            lookVec),
                    entity.dimension, targetEntity.posX,
                    targetEntity.posY, targetEntity.posZ, 50.0f);
        }
    }
}

