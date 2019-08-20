package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.PlayerFormulas;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class LavaWanderer extends PlayerAbility {
    private static final int DURATION_PER_LEVEL = 20;

    public LavaWanderer() {
        super(MKUltra.MODID, "ability.lava_wanderer");
    }

    @Override
    public int getCooldown(int currentRank){
        return 30;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public int getManaCost(int currentRank){
        return 5 + 5 * currentRank;
    }

    @Override
    public float getDistance(int currentRank) {
        return 5.0f + 5.0f * currentRank;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 6 + currentRank * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);

        int level = pData.getAbilityRank(getAbilityId());

        int duration = GameConstants.TICKS_PER_SECOND * DURATION_PER_LEVEL * level;
        duration = PlayerFormulas.applyBuffDurationBonus(pData, duration);
        PotionEffect fireResist = new PotionEffect(MobEffects.FIRE_RESISTANCE,
                duration,
                level, false, true);
        PotionEffect speed = new PotionEffect(MobEffects.HASTE,
                duration,
                level, false, true);

        SpellCast particle = ParticlePotion.Create(entity,
                EnumParticleTypes.DRIP_LAVA.getParticleID(),
                ParticleEffects.CIRCLE_MOTION, false, new Vec3d(1.0, 1.0, 1.0),
                new Vec3d(0.0, 1.0, 0.0), 40, 5, 1.0);

        AreaEffectBuilder.Create(entity, entity)
                .effect(speed, getTargetType())
                .effect(fireResist, getTargetType())
                .spellCast(particle, level, getTargetType())
                .instant()
                .particle(EnumParticleTypes.DRIP_LAVA)
                .color(16762880).radius(getDistance(level), true)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.FLAME.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 40, 10,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0,
                        lookVec),
                entity, 50.0f);
    }
}
