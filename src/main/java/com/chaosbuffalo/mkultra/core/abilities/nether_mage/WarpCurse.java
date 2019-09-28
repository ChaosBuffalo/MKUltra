package com.chaosbuffalo.mkultra.core.abilities.nether_mage;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.core.abilities.cast_states.CastState;
import com.chaosbuffalo.mkultra.core.abilities.cast_states.SingleTargetCastState;
import com.chaosbuffalo.mkultra.effects.spells.WarpCursePotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.utils.AbilityUtils;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WarpCurse extends PlayerAbility {

    public static float BASE_DAMAGE = 4.0f;
    public static float DAMAGE_SCALE = 2.0f;

    public WarpCurse() {
        super(MKUltra.MODID, "ability.warp_curse");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 16 - currentRank * 4;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 6 + currentRank * 2;
    }

    @Override
    public float getDistance(int currentRank) {
        return 25.0f;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return currentRank * 2;
    }

    @Override
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.casting_shadow;
    }

    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_dark_15;
    }

    @Override
    public int getCastTime(int currentRank) {
        return (int) (GameConstants.TICKS_PER_SECOND * 1.5f) / currentRank;
    }

    @Override
    public void endCast(EntityPlayer entity, IPlayerData data, World theWorld, CastState state) {
        super.endCast(entity, data, theWorld, state);
        SingleTargetCastState singleTargetState = AbilityUtils.getCastStateAsType(state, SingleTargetCastState.class);
        PlayerAbilityInfo info = data.getAbilityInfo(getAbilityId());
        if (singleTargetState == null || info == null){
            return;
        }
        if (singleTargetState.hasTarget()){
            EntityLivingBase targetEntity = singleTargetState.getTarget();
            int level = info.getRank();
            AbilityUtils.playSoundAtServerEntity(targetEntity, ModSounds.spell_fire_5, SoundCategory.PLAYERS);
            targetEntity.addPotionEffect(WarpCursePotion.Create(entity)
                    .setTarget(targetEntity)
                    .toPotionEffect(GameConstants.TICKS_PER_SECOND * level * 4, level));
            targetEntity.addPotionEffect(
                    new PotionEffect(MobEffects.SLOWNESS,
                            GameConstants.TICKS_PER_SECOND * level * 4, level,
                            false, true));

            Vec3d lookVec = entity.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.DRIP_LAVA.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 60, 10,
                            targetEntity.posX, targetEntity.posY + 1.0,
                            targetEntity.posZ, 1.0, 1.0, 1.0, 1.0,
                            lookVec),
                    entity.dimension, targetEntity.posX,
                    targetEntity.posY, targetEntity.posZ, 50.0f);

        }
    }

    @Override
    public CastState createCastState(int castTime) {
        return new SingleTargetCastState(castTime);
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        int level = pData.getAbilityRank(getAbilityId());
        EntityLivingBase targetEntity = getSingleLivingTarget(entity, getDistance(level));
        if (targetEntity != null) {
            CastState castState = pData.startAbility(this);
            SingleTargetCastState singleTargetState = AbilityUtils.getCastStateAsType(castState,
                    SingleTargetCastState.class);
            if (singleTargetState != null){
                singleTargetState.setTarget(targetEntity);
            }
        }
    }
}
