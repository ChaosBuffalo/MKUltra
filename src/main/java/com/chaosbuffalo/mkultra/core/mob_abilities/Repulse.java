package com.chaosbuffalo.mkultra.core.mob_abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MobAbility;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.RepulsePotion;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Repulse extends MobAbility {
    public static float BASE_FORCE = 2.0f;
    public static float FORCE_SCALE = 0.3f;

    public Repulse() {
        super(MKUltra.MODID, "mob_ability.repulse");
    }

    @Override
    public int getCooldown() {
        return 20 * GameConstants.TICKS_PER_SECOND;
    }

    @Override
    public AbilityType getAbilityType() {
        return AbilityType.ATTACK;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public float getDistance() {
        return 3.0f;
    }

    @Override
    public int getCastTime() {
        return GameConstants.TICKS_PER_SECOND / 2;
    }


    @Override
    public void execute(EntityLivingBase entity, IMobData data, EntityLivingBase target, World theWorld) {
        int level = data.getMobLevel();
        SpellCast repulse = RepulsePotion.Create(entity, BASE_FORCE, FORCE_SCALE);
        AreaEffectBuilder.Create(entity, entity)
                .spellCast(repulse, level, getTargetType())
                .instant()
                .color(16409620).radius(getDistance() + 2.0f, true)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.SPELL_INSTANT.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 50, 0,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 5.0,
                        lookVec),
                entity, 50.0f);
    }
}

