package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.core.PlayerFormulas;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.FlameBladePotion;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FlameBlade extends PlayerAbility {

    public static int BASE_DURATION = 10;
    public static int DURATION_SCALE = 5;
    public static float BASE_DAMAGE = 1.0f;
    public static float DAMAGE_SCALE = 1.0f;

    public static float PROJECTILE_SPEED = 1.0f;
    public static float PROJECTILE_INACCURACY = 0.5f;

    public FlameBlade() {
        super(MKUltra.MODID, "ability.flame_blade");
    }

    @Override
    public int getCooldown(int currentRank) {
        return 18 + currentRank * 5;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.FRIENDLY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 2 + currentRank * 4;
    }

    @Override
    public float getDistance(int currentRank) {
        return 10.0f + 2.0f * currentRank;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return currentRank * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);

        int level = pData.getAbilityRank(getAbilityId());

        // What to do for each target hit
        int duration = (BASE_DURATION + DURATION_SCALE * level) * GameConstants.TICKS_PER_SECOND;
        duration = PlayerFormulas.applyBuffDurationBonus(pData, duration);
        SpellCast effect = FlameBladePotion.Create(entity);
        SpellCast particlePotion = ParticlePotion.Create(entity,
                EnumParticleTypes.DRIP_LAVA.getParticleID(),
                ParticleEffects.DIRECTED_SPOUT, false, new Vec3d(1.0, 1.5, 1.0),
                new Vec3d(0.0, 1.0, 0.0), 40, 5, 1.0);

        AreaEffectBuilder.Create(entity, entity)
                .spellCast(effect, duration, level, getTargetType())
                .spellCast(particlePotion, level, getTargetType())
                .instant()
                .particle(EnumParticleTypes.FIREWORKS_SPARK)
                .color(16737330).radius(getDistance(level), true)
                .spawn();


        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.DRIP_LAVA.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 50, 0,
                        entity.posX, entity.posY + 1.5,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0f,
                        lookVec),
                entity, 50.0f);
    }
}
