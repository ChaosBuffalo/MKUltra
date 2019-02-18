package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.CriticalChancePotion;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
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

public class Yaup extends PlayerAbility {

    public Yaup() {
        super(MKUltra.MODID, "ability.yaup");
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 45;
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
        return 2 + currentLevel * 2;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 10.0f + (currentLevel * 5.0f);
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return 4 + currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);

        int level = pData.getLevelForAbility(getAbilityId());

        int duration = 15 + (level * 15);
        duration *= GameConstants.TICKS_PER_SECOND;

        PotionEffect hasteEffect = new PotionEffect(MobEffects.HASTE, duration, level - 1, false, true);
        PotionEffect damageEffect = new PotionEffect(MobEffects.STRENGTH, duration, level - 1, false, true);
        SpellCast particlePotion = ParticlePotion.Create(entity,
                EnumParticleTypes.CRIT.getParticleID(),
                ParticleEffects.SPHERE_MOTION, false, new Vec3d(1.0, 1.0, 1.0),
                new Vec3d(0.0, 1.0, 0.0), 50, 5, 0.5);

        AreaEffectBuilder.Create(entity, entity)
                .effect(hasteEffect, getTargetType())
                .effect(damageEffect, getTargetType())
                .spellCast(CriticalChancePotion.Create(entity), level + 1, Targeting.TargetType.SELF)
                .spellCast(particlePotion, 0, getTargetType())
                .instant().color(16751360).radius(getDistance(level), true)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.CRIT.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 50, 5,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 0.5,
                        lookVec), entity, 50.0f);
    }
}