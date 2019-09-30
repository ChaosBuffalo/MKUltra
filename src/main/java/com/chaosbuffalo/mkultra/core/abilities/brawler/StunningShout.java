package com.chaosbuffalo.mkultra.core.abilities.brawler;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKDamageSource;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.effects.spells.AIStunPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.utils.AbilityUtils;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class StunningShout extends PlayerAbility {

    public static final float DAMAGE_BASE = 2.0f;
    public static final float DAMAGE_SCALE = 2.0f;

    public StunningShout() {
        super(MKUltra.MODID, "ability.stunning_shout");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 14 - currentRank * 2;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 2 + currentRank;
    }

    @Override
    public float getDistance(int currentRank) {
        return 10.0f;
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return ModSounds.spell_shout_1;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 4 + currentRank * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        int level = pData.getAbilityRank(getAbilityId());
        pData.startAbility(this);
        Vec3d look = entity.getLookVec().scale(getDistance(level));
        Vec3d from = entity.getPositionVector().add(0, entity.getEyeHeight(), 0);
        Vec3d to = from.add(look);

        Vec3d lookVec = entity.getLookVec();
        for (Entity ent : getTargetsInLine(entity, from, to, true, 2.5f)) {
            if (ent instanceof EntityLivingBase) {
                EntityLivingBase targetEntity = (EntityLivingBase) ent;
//                float randomPitch = theWorld.rand.nextFloat() * .2f - .1f;
//                AbilityUtils.playSoundAtServerEntity(targetEntity, ModSounds.spell_shout_1,
//                        SoundCategory.PLAYERS, 1.0f, randomPitch);
                targetEntity.addPotionEffect(
                        new PotionEffect(MobEffects.SLOWNESS,
                                level * GameConstants.TICKS_PER_SECOND,
                                100, false, true));
                targetEntity.addPotionEffect(
                        new PotionEffect(MobEffects.BLINDNESS,
                                level * GameConstants.TICKS_PER_SECOND,
                                100, false, true));
                targetEntity.addPotionEffect(
                        new PotionEffect(MobEffects.WEAKNESS,
                                level * GameConstants.TICKS_PER_SECOND,
                                100, false, true));
                targetEntity.attackEntityFrom(MKDamageSource.fromMeleeSkill(getAbilityId(), entity, entity),
                        DAMAGE_BASE + (level * DAMAGE_SCALE));
                targetEntity.addPotionEffect(AIStunPotion.Create(entity).setTarget(targetEntity)
                        .toPotionEffect(level * GameConstants.TICKS_PER_SECOND, level));

                MKUltra.packetHandler.sendToAllAround(
                        new ParticleEffectSpawnPacket(
                                EnumParticleTypes.FALLING_DUST.getParticleID(),
                                ParticleEffects.SPHERE_MOTION, 60, 10,
                                targetEntity.posX, targetEntity.posY + 1.0,
                                targetEntity.posZ, 1.0, 1.0, 1.0, 2.0,
                                lookVec),
                        entity.dimension, targetEntity.posX,
                        targetEntity.posY, targetEntity.posZ, 50.0f);
            }
        }

    }
}

