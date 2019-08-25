package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.spells.WarpTargetPotion;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
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

public class PowerWordSummon extends PlayerAbility {

    public PowerWordSummon() {
        super(MKUltra.MODID, "ability.power_word_summon");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 16 - 4 * currentRank;
    }

    @Override
    public AbilityType getType() {
        return AbilityType.Active;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public int getManaCost(int currentRank) {
        return 4 + currentRank * 2;
    }

    @Override
    public float getDistance(int currentRank) {
        return 45.0f;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 4 + currentRank * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        int level = pData.getAbilityRank(getAbilityId());

        EntityLivingBase targetEntity = getSingleLivingTarget(entity, getDistance(level));
        if (targetEntity != null) {
            pData.startAbility(this);
            targetEntity.addPotionEffect(WarpTargetPotion.Create(entity).setTarget(targetEntity).toPotionEffect(level));
            targetEntity.addPotionEffect(
                    new PotionEffect(MobEffects.SLOWNESS,
                            (4 + level) * GameConstants.TICKS_PER_SECOND, 100, false, true));

            Vec3d lookVec = entity.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.SPELL_MOB.getParticleID(),
                            ParticleEffects.CIRCLE_PILLAR_MOTION, 60, 10,
                            targetEntity.posX, targetEntity.posY + 0.5,
                            targetEntity.posZ, 1.0, 1.0, 1.0, 1.0,
                            lookVec),
                    entity.dimension, targetEntity.posX,
                    targetEntity.posY, targetEntity.posZ, 50.0f);
        }
    }
}

