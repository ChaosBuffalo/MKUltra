package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.InstantIndirectDamagePotion;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WhirlwindBlades extends BaseAbility {

    public static float BASE_DAMAGE = 4.0f;
    public static float DAMAGE_SCALE = 4.0f;

    public WhirlwindBlades() {
        super(MKUltra.MODID, "ability.whirlwind_blades");
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 25 - 5 * currentLevel;
    }

    @Override
    public int getType() {
        return ACTIVE_ABILITY;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public int getManaCost(int currentLevel) {
        return 3 + 2 * currentLevel;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 3.0f + currentLevel * 1.0f;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return 6 + currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);

        int level = pData.getLevelForAbility(getAbilityId());

        // What to do for each target hit
        SpellCast damage = InstantIndirectDamagePotion.Create(entity, BASE_DAMAGE, DAMAGE_SCALE);
        SpellCast particlePotion = ParticlePotion.Create(entity,
                EnumParticleTypes.SWEEP_ATTACK.getParticleID(),
                ParticleEffects.CIRCLE_MOTION, false, new Vec3d(1.0, 1.0, 1.0),
                new Vec3d(0.0, 1.0, 0.0), 20, 0, 1.0);


        int totalDuration = level * GameConstants.TICKS_PER_SECOND;
        int tickSpeed = 5;

        AreaEffectBuilder.Create(entity, entity)
                .spellCast(damage, level, getTargetType())
                .spellCast(particlePotion, level, getTargetType())
                .duration(totalDuration).waitTime(0)
                .color(16409620).radius(getDistance(level), true)
                .period(tickSpeed)
                .particle(EnumParticleTypes.CRIT)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.SWEEP_ATTACK.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 20, 5,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 1.5,
                        lookVec),
                entity, 50.0f);
    }
}