package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.ClericHealPotion;
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

public class LifeSpring  extends BaseAbility {
    public static float BASE = 4.0f;
    public static float SCALE = 2.0f;

    public LifeSpring() {
        super(MKUltra.MODID, "ability.life_spring");
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 28 - currentLevel * 5;
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
        return 8 + 3 * currentLevel;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 3.0f + currentLevel * 3.0f;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return 4 + currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);

        int level = pData.getLevelForAbility(getAbilityId());

        // What to do for each target hit
        SpellCast heal = ClericHealPotion.Create(entity, BASE, SCALE);
        SpellCast particle = ParticlePotion.Create(entity,
                EnumParticleTypes.VILLAGER_HAPPY.getParticleID(),
                ParticleEffects.CIRCLE_MOTION, false, new Vec3d(1.0, 1.0, 1.0),
                new Vec3d(0.0, 1.0, 0.0), 10, 0, .25);


        int totalDuration = GameConstants.TICKS_PER_SECOND * 5 + level * 5;
        int tickSpeed = 30;

        AreaEffectBuilder.Create(entity, entity)
                .spellCast(heal, level, getTargetType())
                .spellCast(particle, level, getTargetType())
                .duration(totalDuration).waitTime(0)
                .color(65480).radius(getDistance(level), true)
                .period(tickSpeed)
                .particle(EnumParticleTypes.VILLAGER_HAPPY)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.VILLAGER_HAPPY.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 50, 5,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0f,
                        lookVec),
                entity, 50.0f);

    }
}
