package com.chaosbuffalo.mkultra.core.mob_abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FullHeal extends MobAbility {
    public static final float HEAL_THRESHOLD = .25f;

    public FullHeal(){
        super(MKUltra.MODID, "mob_ability.full_heal");
    }

    @Override
    public int getCooldown() {
        return 30 * GameConstants.TICKS_PER_SECOND;
    }

    @Override
    public AbilityType getAbilityType() {
        return AbilityType.HEAL;
    }

    @Override
    public boolean canSelfCast(){
        return true;
    }

    @Override
    public float getDistance() {
        return 10.0f;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public boolean shouldCast(EntityLivingBase caster, EntityLivingBase target){
        if (target.getHealth() >= target.getMaxHealth() * HEAL_THRESHOLD){
            return false;
        }
        return super.shouldCast(caster, target);
    }


    @Override
    public void execute(EntityLivingBase entity, IMobData data, EntityLivingBase target, World theWorld) {
        target.setHealth(target.getMaxHealth());
        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.VILLAGER_HAPPY.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 50, 10,
                        target.posX, target.posY + 1.0f,
                        target.posZ, 1.0, 1.0, 1.0, 1.5,
                        lookVec),
                entity.dimension, target.posX,
                target.posY, target.posZ, 50.0f);
    }
}
