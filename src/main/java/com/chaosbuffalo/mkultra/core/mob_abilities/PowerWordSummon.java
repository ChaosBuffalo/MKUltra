package com.chaosbuffalo.mkultra.core.mob_abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.effects.spells.WarpTargetPotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class PowerWordSummon extends MobAbility {

    public PowerWordSummon(){
        super(MKUltra.MODID, "mob_ability.power_word_summon");
    }

    @Override
    public int getCooldown() {
        return 15 * GameConstants.TICKS_PER_SECOND;
    }

    @Override
    public MobAbility.AbilityType getAbilityType() {
        return MobAbility.AbilityType.ATTACK;
    }

    @Override
    public float getDistance() {
        return 10.0f;
    }

    @Override
    public int getCastTime(){
        return GameConstants.TICKS_PER_SECOND * 2;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public void execute(EntityLivingBase entity, IMobData data, EntityLivingBase target, World theWorld) {
        if (target != null) {
            target.addPotionEffect(WarpTargetPotion.Create(entity).setTarget(target).toPotionEffect(1));
            target.addPotionEffect(
                    new PotionEffect(MobEffects.SLOWNESS,
                            2 * GameConstants.TICKS_PER_SECOND, 100, false, true));

            Vec3d lookVec = entity.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.SPELL_MOB.getParticleID(),
                            ParticleEffects.CIRCLE_PILLAR_MOTION, 60, 10,
                            target.posX, target.posY + 0.5,
                            target.posZ, 1.0, 1.0, 1.0, 1.0,
                            lookVec),
                    entity.dimension, target.posX,
                    target.posY, target.posZ, 50.0f);
        }
    }
}


