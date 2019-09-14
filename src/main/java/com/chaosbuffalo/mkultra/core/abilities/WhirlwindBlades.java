package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.CastState;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.AbilityMeleeDamage;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class WhirlwindBlades extends PlayerAbility {

    public static float BASE_DAMAGE = 2.0f;
    public static float DAMAGE_SCALE = 1.0f;

    public WhirlwindBlades() {
        super(MKUltra.MODID, "ability.whirlwind_blades");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 25 - 5 * currentRank;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 4 + 2 * currentRank;
    }

    @Override
    public float getDistance(int currentRank) {
        return 3.0f + currentRank * 1.0f;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 6 + currentRank * 2;
    }

    @Override
    public int getCastTime(int currentRank) {
        return currentRank * GameConstants.TICKS_PER_SECOND;
    }

    @Override
    public void continueCast(EntityPlayer entity, IPlayerData data, World theWorld, int castTimeLeft, CastState state) {
        super.continueCast(entity, data, theWorld, castTimeLeft, state);
        int tickSpeed = 6;
        if (castTimeLeft % tickSpeed == 0){
            int level = data.getAbilityRank(getAbilityId());
            int totalDuration = getCastTime(level);
            int count = (totalDuration - castTimeLeft) / tickSpeed;
            float baseAmount = level > 1 ? 0.2f : 0.3f;
            float scaling = count * baseAmount;
            // What to do for each target hit
            SpellCast damage = AbilityMeleeDamage.Create(entity, BASE_DAMAGE, DAMAGE_SCALE, scaling);
            SpellCast particlePotion = ParticlePotion.Create(entity,
                    EnumParticleTypes.SWEEP_ATTACK.getParticleID(),
                    ParticleEffects.CIRCLE_MOTION, false,
                    new Vec3d(1.0, 1.0, 1.0),
                    new Vec3d(0.0, 1.0, 0.0),
                    20, 0, 1.0);


            AreaEffectBuilder.Create(entity, entity)
                    .spellCast(damage, level, getTargetType())
                    .spellCast(particlePotion, level, getTargetType())
                    .instant()
                    .color(16409620).radius(getDistance(level), true)
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

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);
    }
}