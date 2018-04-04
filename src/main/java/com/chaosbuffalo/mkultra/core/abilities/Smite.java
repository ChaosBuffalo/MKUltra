package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.effects.spells.SmitePotion;
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

public class Smite extends BaseAbility {

    public static float BASE_DAMAGE = 4.0f;
    public static float DAMAGE_SCALE = 2.0f;

    public Smite() {
        super(MKUltra.MODID, "ability.smite");
    }

    @Override
    public String getAbilityName() {
        return "Smite";
    }

    @Override
    public String getAbilityDescription() {
        return "Calls upon holy powers to damage your enemy, rooting them for a short time.";
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/smite.png");
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
        return 0;
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 6 - currentLevel;
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
        return 3 + currentLevel * 2;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 15.0f;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        int level = pData.getLevelForAbility(getAbilityId());

        EntityLivingBase targetEntity = getSingleLivingTarget(entity, getDistance(level));
        if (targetEntity != null) {
            pData.startAbility(this);

            targetEntity.addPotionEffect(SmitePotion.Create(entity, targetEntity, BASE_DAMAGE, DAMAGE_SCALE).toPotionEffect(level));
            targetEntity.addPotionEffect(
                    new PotionEffect(MobEffects.SLOWNESS,
                            GameConstants.TICKS_PER_SECOND * level, 100, false, true));

            Vec3d lookVec = entity.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.CRIT_MAGIC.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 60, 10,
                            targetEntity.posX, targetEntity.posY + 1.0,
                            targetEntity.posZ, 1.0, 1.0, 1.0, 1.0,
                            lookVec),
                    entity.dimension, targetEntity.posX,
                    targetEntity.posY, targetEntity.posZ, 50.0f);
        }
    }
}
