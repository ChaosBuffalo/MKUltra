package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerFormulas;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.NaturesRemedyPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Jacob on 7/28/2018.
 */
public class NaturesRemedy extends PlayerAbility {

    public static float BASE_VALUE = 2.0f;
    public static float VALUE_SCALE = 0.0f;
    public static int BASE_DURATION = 4;
    public static int DURATION_SCALE = 2;

    public NaturesRemedy() {
        super(MKUltra.MODID, "ability.natures_remedy");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 10 - 2 * currentRank;
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
    public int getManaCost(int currentRank) {
        return 2 + currentRank * 2;
    }

    @Override
    public float getDistance(int currentRank) {
        return 10.0f + 5.0f * currentRank;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return currentRank * 2;
    }


    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {

        int level = pData.getAbilityRank(getAbilityId());
        EntityLivingBase targetEntity = getSingleLivingTargetOrSelf(entity, getDistance(level), true);

        pData.startAbility(this);
        Log.info(String.format("Adding natures remedy to %s", targetEntity.getName()));

        int duration = (BASE_DURATION + level * DURATION_SCALE) * GameConstants.TICKS_PER_SECOND;
        duration = PlayerFormulas.applyBuffDurationBonus(pData, duration);
        SpellCast heal = NaturesRemedyPotion.Create(entity, targetEntity, BASE_VALUE, VALUE_SCALE);
        targetEntity.addPotionEffect(heal.toPotionEffect(duration, level));

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.SLIME.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 30, 10,
                        targetEntity.posX, targetEntity.posY + 1.0f,
                        targetEntity.posZ, 1.0, 1.0, 1.0, .5,
                        lookVec),
                entity.dimension, targetEntity.posX,
                targetEntity.posY, targetEntity.posZ, 50.0f);

    }
}

