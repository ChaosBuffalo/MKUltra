package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
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
    public int getCooldown(int currentLevel) {
        return 35;
    }

    @Override
    public int getType() {
        return ACTIVE_ABILITY;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public int getManaCost(int currentLevel) {
        return 4 + currentLevel * 2;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 20.0f;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return 6 + currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {

        pData.startAbility(this);

        int level = pData.getLevelForAbility(getAbilityId());

        PotionEffect hasteEffect = new PotionEffect(MobEffects.HASTE, 900, level, false, true);
        PotionEffect regenEffect = new PotionEffect(MobEffects.REGENERATION, 900, level + 1, false, true);

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
}
