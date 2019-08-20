package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerFormulas;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SpiritOfWolf extends PlayerAbility {

    public static int BASE_DURATION = 0;
    public static int DURATION_SCALE = 10;

    public SpiritOfWolf() {
        super(MKUltra.MODID, "ability.spirit_of_wolf");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 40 - 10 * currentRank;
    }

    @Override
    public int getType() {
        return ACTIVE_ABILITY;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public boolean canSelfCast() {
        return true;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 4 + currentRank * 4;
    }

    @Override
    public float getDistance(int currentRank) {
        return 10.0f;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 4 + currentRank * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {

        int level = pData.getAbilityRank(getAbilityId());
        EntityLivingBase targetEntity = getSingleLivingTargetOrSelf(entity, getDistance(level), true);
        
        pData.startAbility(this);

        int duration = GameConstants.TICKS_PER_SECOND * 60 * (BASE_DURATION + DURATION_SCALE * level);
        duration = PlayerFormulas.applyBuffDurationBonus(pData, duration);
        PotionEffect addSpeed = new PotionEffect(MobEffects.SPEED, duration, level - 1);
        targetEntity.addPotionEffect(addSpeed);

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.SPELL_MOB.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 50, 10,
                        targetEntity.posX, targetEntity.posY + 1.0f,
                        targetEntity.posZ, 1.0, 1.0, 1.0, 1.5,
                        lookVec),
                entity.dimension, targetEntity.posX,
                targetEntity.posY, targetEntity.posZ, 50.0f);

    }
}

