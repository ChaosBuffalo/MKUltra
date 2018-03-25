package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.effects.spells.FlameWavePotion;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class FlameWave extends BaseAbility {

    public static float BASE_DAMAGE = 8.0f;
    public static float DAMAGE_SCALE = 2.0f;

    public FlameWave() {
        super(MKUltra.MODID, "ability.flame_wave");
    }

    @Override
    public String getAbilityName() {
        return "Flame wave";
    }

    @Override
    public String getAbilityDescription() {
        return "Damages all enemies around you, dealing double damage if they are burning.";
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/flamewave.png");
    }


    @Override
    public String getAbilityType() {
        return "PBAOE";
    }

    @Override
    public int getIconU() {
        return 180;
    }

    @Override
    public int getIconV() {
        return 18;
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 20 - currentLevel * 5;
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
        return 8 + currentLevel * 2;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 5.0f + 2.0f * currentLevel;
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
        SpellCast flames = FlameWavePotion.Create(entity, BASE_DAMAGE, DAMAGE_SCALE);
        SpellCast particles = ParticlePotion.Create(entity,
                EnumParticleTypes.LAVA.getParticleID(),
                ParticleEffects.SPHERE_MOTION, false, new Vec3d(1.0, 1.0, 1.0),
                new Vec3d(0.0, 1.0, 0.0), 40, 5, 1.0);

        AreaEffectBuilder.Create(entity, entity)
                .spellCast(flames, level, getTargetType())
                .spellCast(particles, level, getTargetType())
                .instant()
                .color(16737305).radius(getDistance(level), true)
                .particle(EnumParticleTypes.LAVA)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.LAVA.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 50, 0,
                        entity.posX, entity.posY + 1.0,
                        entity.posZ, 1.0, 1.0, 1.0, 2.5f,
                        lookVec),
                entity, 50.0f);
    }
}