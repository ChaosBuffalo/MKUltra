package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.effects.spells.FeatherFallPotion;
import com.chaosbuffalo.mkultra.effects.spells.FlightPotion;
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

public class EagleAspect extends BaseAbility {

    public static int BASE_DURATION = 0;
    public static int DURATION_SCALE = 60;

    public EagleAspect() {
        super(MKUltra.MODID, "ability.eagle_aspect");
    }

    @Override
    public String getAbilityName() {
        return "Eagle Aspect";
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/eagle_aspect.png");
    }


    @Override
    public String getAbilityDescription() {
        return "Buffs all surrounding players, granting them flight.";
    }

    @Override
    public String getAbilityType() {
        return "Group Buff";
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 1200 - currentLevel * 300;
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
        return 10 + currentLevel * 5;
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
        SpellCast effect = FlightPotion.Create(entity);
        SpellCast feather = FeatherFallPotion.Create(entity);
        SpellCast particlePotion = ParticlePotion.Create(entity,
                EnumParticleTypes.FIREWORKS_SPARK.getParticleID(),
                ParticleEffects.DIRECTED_SPOUT, false, new Vec3d(1.0, 1.5, 1.0),
                new Vec3d(0.0, 1.0, 0.0), 40, 5, 1.0);

        AreaEffectBuilder.Create(entity, entity)
                .spellCast(effect, duration, level, getTargetType())
                .spellCast(feather, duration + 10 * GameConstants.TICKS_PER_SECOND, level, getTargetType())
                .spellCast(particlePotion, level, getTargetType())
                .instant()
                .particle(EnumParticleTypes.FIREWORKS_SPARK)
                .color(65480).radius(getDistance(level), true)
                .spawn();

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.FIREWORKS_SPARK.getParticleID(),
                        ParticleEffects.CIRCLE_MOTION, 50, 0,
                        entity.posX, entity.posY + 1.5,
                        entity.posZ, 1.0, 1.0, 1.0, 1.0f,
                        lookVec),
                entity, 50.0f);
    }
}
