package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKDamageSource;
import com.chaosbuffalo.mkultra.effects.spells.CurePotion;
import com.chaosbuffalo.mkultra.effects.spells.NaturesRemedyPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.utils.RayTraceUtils;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

/**
 * Created by Jacob on 7/28/2018.
 */
public class ExplosiveGrowth extends PlayerAbility {

    public static float BASE_DAMAGE = 10.0f;
    public static float DAMAGE_SCALE = 5.0f;
    public static float DASH_BASE = 6.0f;
    public static float DASH_SCALE = 2.0f;

    public ExplosiveGrowth() {
        super(MKUltra.MODID, "ability.explosive_growth");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 35 - 5 * currentRank;
    }

    @Override
    public AbilityType getType() {
        return AbilityType.Active;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ALL;
    }

    @Override
    public int getManaCost(int currentRank) {
        return 6 + currentRank * 2;
    }

    @Override
    public float getDistance(int currentRank) {
        return DASH_BASE + currentRank * DASH_SCALE;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 6 + currentRank * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        int level = pData.getAbilityRank(getAbilityId());
        pData.startAbility(this);
        Vec3d look = entity.getLookVec().scale(getDistance(level));
        Vec3d from = entity.getPositionVector().add(0, entity.getEyeHeight(), 0);
        Vec3d to = from.add(look);

        Vec3d lookVec = entity.getLookVec();
        List<EntityLivingBase> entityHit = getTargetsInLine(entity, from, to, true, 1.0f);
        float damage = BASE_DAMAGE + DAMAGE_SCALE * level;

        for (EntityLivingBase entHit : entityHit){

            if (Targeting.isValidTarget(Targeting.TargetType.ENEMY, entity, entHit, true)){
                entHit.attackEntityFrom(MKDamageSource.fromMeleeSkill(getAbilityId(), entity, entity), damage);
            } else if (Targeting.isValidTarget(Targeting.TargetType.FRIENDLY, entity, entHit, false)){
                entHit.addPotionEffect(CurePotion.Create(entity).setTarget(entHit).toPotionEffect(level));
                entHit.addPotionEffect(
                        NaturesRemedyPotion.Create(
                                entity, entHit, NaturesRemedy.BASE_VALUE, NaturesRemedy.VALUE_SCALE)
                        .toPotionEffect((NaturesRemedy.BASE_DURATION + NaturesRemedy.DURATION_SCALE * level)
                                * GameConstants.TICKS_PER_SECOND, level));
            }
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(),
                            ParticleEffects.CIRCLE_MOTION, 20, 10,
                            entHit.posX, entHit.posY + 1.0,
                            entHit.posZ, 1.0, 1.0, 1.0, 2.0,
                            lookVec),
                    entity.dimension, entHit.posX,
                    entHit.posY, entHit.posZ, 50.0f);
        }

        RayTraceResult blockHit = RayTraceUtils.rayTraceBlocks(entity.getEntityWorld(), from, to, false);
        if (blockHit != null && blockHit.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            to = blockHit.hitVec;
        }
        entity.addPotionEffect(CurePotion.Create(entity).setTarget(entity).toPotionEffect(level));
        entity.addPotionEffect(
                NaturesRemedyPotion.Create(
                        entity, entity, NaturesRemedy.BASE_VALUE, NaturesRemedy.VALUE_SCALE)
                        .toPotionEffect((NaturesRemedy.BASE_DURATION + NaturesRemedy.DURATION_SCALE * level)
                                * GameConstants.TICKS_PER_SECOND, level));
        entity.setPositionAndUpdate(to.x, to.y, to.z);
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.SPELL_MOB_AMBIENT.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 30, 10,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 2.0,
                        lookVec),
                entity.dimension, entity.posX,
                entity.posY, entity.posZ, 50.0f);

    }
}


