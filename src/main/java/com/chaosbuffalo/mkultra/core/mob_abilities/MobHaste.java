package com.chaosbuffalo.mkultra.core.mob_abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class MobHaste extends MobAbility {


    public MobHaste() {
        super(MKUltra.MODID, "mob_ability.haste");
    }

    @Override
    public int getCooldown() {
        return 30 * GameConstants.TICKS_PER_SECOND;
    }

    @Override
    public AbilityType getAbilityType() {
        return AbilityType.BUFF;
    }

    @Override
    public boolean canSelfCast() {
        return true;
    }

    @Override
    public float getDistance() {
        return 10.0f;
    }

    @Override
    public int getCastTime() {
        return GameConstants.TICKS_PER_SECOND / 2;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.SELF;
    }

    public int getDuration() {
        return 10 * GameConstants.TICKS_PER_SECOND;
    }

    @Override
    public Potion getEffectPotion() {
        return MobEffects.HASTE;
    }

    @Override
    public void execute(EntityLivingBase entity, IMobData data, EntityLivingBase target, World theWorld) {
        int duration = getDuration();
        entity.addPotionEffect(new PotionEffect(MobEffects.HASTE, duration, data.getMobLevel() / 2));
        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.CRIT_MAGIC.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 50, 0,
                        entity.posX, entity.posY + 1.5,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0f,
                        lookVec),
                entity, 50.0f);
    }
}






