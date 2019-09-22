package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.abilities.cast_states.CastState;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.AbilityMagicDamage;
import com.chaosbuffalo.mkultra.effects.spells.HeavingSeasPotion;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.utils.EnvironmentUtils;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

/**
 * Created by Jacob on 3/25/2018.
 */
public class HeavingSeas extends PlayerAbility {
    public static float BASE_DAMAGE = 2.0f;
    public static float DAMAGE_SCALE = 2.0f;
    public static int DURATION_BASE = 2;
    public static int DURATION_SCALE = 2;

    public HeavingSeas() {
        super(MKUltra.MODID, "ability.heaving_seas");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 12 - 2 * currentRank;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 6 + currentRank * 2;
    }

    @Override
    public float getDistance(int currentRank) {
        return 2.0f + currentRank * 2.0f;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 4 + currentRank * 2;
    }

    @Override
    public int getCastTime(int currentRank) {
        return GameConstants.TICKS_PER_SECOND * 2 - (currentRank * 5);
    }

    @Override
    public void endCast(EntityPlayer entity, IPlayerData data, World theWorld, CastState state) {
        super.endCast(entity, data, theWorld, state);
        int level = data.getAbilityRank(getAbilityId());

        // What to do for each target hit
        SpellCast damage = AbilityMagicDamage.Create(entity, BASE_DAMAGE, DAMAGE_SCALE);
        SpellCast particle = ParticlePotion.Create(entity,
                EnumParticleTypes.WATER_DROP.getParticleID(),
                ParticleEffects.CIRCLE_PILLAR_MOTION, false,
                new Vec3d(1.0, 1.0, 1.0),
                new Vec3d(0.0, 1.0, 0.0),
                40, 5, 1.0);

        SpellCast heavingSeas = HeavingSeasPotion.Create(entity);

        AreaEffectBuilder.Create(entity, entity)
                .spellCast(damage, level, getTargetType())
                .spellCast(particle, level, getTargetType())
                .spellCast(heavingSeas, level, getTargetType())
                .instant()
                .color(16711935).radius(getDistance(level), true)
                .particle(EnumParticleTypes.WATER_BUBBLE)
                .spawn();

        EnvironmentUtils.putOutFires(entity.getEntityWorld(), entity.getPosition(),
                new Vec3i(10, 6, 10));

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.WATER_DROP.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 20, 0,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 2.0,
                        lookVec),
                entity, 50.0f);
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);
    }
}
