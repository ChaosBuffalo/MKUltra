package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.MoonTrancePotion;
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

/**
 * Created by Jacob on 3/24/2018.
 */
public class MoonTrance extends BaseAbility {

    public static int BASE_DURATION = 30;
    public static int DURATION_SCALE = 15;

    public MoonTrance() {
        super(MKUltra.MODID, "ability.moon_trance");
    }

    @Override
    public int getCooldown(int currentLevel) {
        return BASE_DURATION + currentLevel * DURATION_SCALE;
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
        return 10 + currentLevel * 3;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 10.0f + 2.0f * currentLevel;
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
        int duration = (BASE_DURATION + DURATION_SCALE * level) * GameConstants.TICKS_PER_SECOND;
        SpellCast effect = MoonTrancePotion.Create(entity);
        SpellCast particlePotion = ParticlePotion.Create(entity,
                EnumParticleTypes.SPELL_MOB.getParticleID(),
                ParticleEffects.SPHERE_MOTION, false, new Vec3d(1.0, 1.5, 1.0),
                new Vec3d(0.0, 1.0, 0.0), 40, 5, 1.0);

        AreaEffectBuilder.Create(entity, entity)
                .spellCast(effect, duration, level, getTargetType())
                .spellCast(particlePotion, level, getTargetType())
                .instant()
                .particle(EnumParticleTypes.SPELL_MOB_AMBIENT)
                .color(13147120).radius(getDistance(level), true)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.SPELL_MOB.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 50, 0,
                        entity.posX, entity.posY + 1.5,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0f,
                        lookVec),
                entity, 50.0f);
    }
}
