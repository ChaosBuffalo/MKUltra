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

public class MobFuriousBrooding extends MobAbility {


    public MobFuriousBrooding() {
        super(MKUltra.MODID, "mob_ability.furious_brooding");
    }

    @Override
    public int getCooldown() {
        return 20 * GameConstants.TICKS_PER_SECOND;
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
    public int getCastTime(){
        return GameConstants.TICKS_PER_SECOND / 2;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.SELF;
    }

    @Override
    public Potion getEffectPotion() {
        return MobEffects.REGENERATION;
    }

    @Override
    public void execute(EntityLivingBase entity, IMobData data, EntityLivingBase target, World theWorld) {
        int level = data.getMobLevel() > 5 ? 2 : 1;
        entity.addPotionEffect(
                new PotionEffect(MobEffects.REGENERATION,
                        (3 + 2 * level) * GameConstants.TICKS_PER_SECOND,
                        level + 1, false, true));
        entity.addPotionEffect(
                new PotionEffect(MobEffects.SLOWNESS,
                        (3 + 2 * level) * GameConstants.TICKS_PER_SECOND,
                        level - 1, false, true));

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.VILLAGER_ANGRY.getParticleID(),
                        ParticleEffects.DIRECTED_SPOUT, 60, 1,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0,
                        lookVec),
                entity.dimension, entity.posX,
                entity.posY, entity.posZ, 50.0f);
    }
}



