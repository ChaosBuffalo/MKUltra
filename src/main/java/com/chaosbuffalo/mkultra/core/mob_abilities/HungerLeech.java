package com.chaosbuffalo.mkultra.core.mob_abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class HungerLeech extends MobAbility {

    float BASE_DAMAGE = 1.0f;
    float DAMAGE_SCALE = .25f;
    float HUNGER_LEECH_BASE = 1.0f;
    float HUNGER_LEECH_SCALE = .5f;

    public HungerLeech(){
        super(MKUltra.MODID, "mob_ability.hunger_leech");
    }

    @Override
    public int getCooldown() {
        return 10 * GameConstants.TICKS_PER_SECOND;
    }

    @Override
    public MobAbility.AbilityType getAbilityType() {
        return MobAbility.AbilityType.ATTACK;
    }

    @Override
    public float getDistance() {
        return 5.0f;
    }

    @Override
    public int getCastTime(){
        return 2 * GameConstants.TICKS_PER_SECOND;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public void execute(EntityLivingBase entity, IMobData data, EntityLivingBase target, World theWorld) {
        if (target != null) {
            int level = data.getMobLevel();
            target.attackEntityFrom(MKDamageSource.causeIndirectMobMagicDamage(getAbilityId(), entity, entity),
                    BASE_DAMAGE + DAMAGE_SCALE * level);
            if (target instanceof EntityPlayer){
                EntityPlayer playerTarget = (EntityPlayer) target;
                playerTarget.getFoodStats().setFoodLevel(playerTarget.getFoodStats().getFoodLevel()
                        - Math.round(HUNGER_LEECH_BASE + HUNGER_LEECH_SCALE * level));
            }
            Vec3d lookVec = entity.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.DAMAGE_INDICATOR.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 40, 4,
                            entity.posX, entity.posY + 1.0,
                            entity.posZ, 1.0, 1.0, 1.0, .25,
                            lookVec),
                    entity.dimension, target.posX,
                    target.posY, target.posZ, 50.0f);
        }
    }
}

