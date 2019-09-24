package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.abilities.cast_states.CastState;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.PlayerFormulas;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Inspire extends PlayerAbility {

    public Inspire() {
        super(MKUltra.MODID, "ability.inspire");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 35;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 4 + currentRank * 2;
    }

    @Override
    public float getDistance(int currentRank) {
        return 20.0f;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 6 + currentRank * 2;
    }

    @Override
    public int getCastTime(int currentRank) {
        return GameConstants.TICKS_PER_SECOND * 2 / currentRank;
    }

    @Override
    public void endCast(EntityPlayer entity, IPlayerData data, World theWorld, CastState state) {
        super.endCast(entity, data, theWorld, state);
        int level = data.getAbilityRank(getAbilityId());

        int duration = GameConstants.TICKS_PER_SECOND * 15;
        duration = PlayerFormulas.applyBuffDurationBonus(data, duration);

        PotionEffect hasteEffect = new PotionEffect(MobEffects.HASTE, duration, level, false, true);
        PotionEffect regenEffect = new PotionEffect(MobEffects.REGENERATION, duration, level + 1, false, true);

        AreaEffectBuilder.Create(entity, entity)
                .effect(hasteEffect, Targeting.TargetType.FRIENDLY)
                .effect(regenEffect, Targeting.TargetType.FRIENDLY)
                .instant().color(1034415).radius(getDistance(level), true)
                .particle(EnumParticleTypes.VILLAGER_HAPPY)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.VILLAGER_HAPPY.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 50, 5,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 0.8, 1.5, 0.8, 0.5,
                        lookVec),
                entity, 50.0f);
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);
    }
}
