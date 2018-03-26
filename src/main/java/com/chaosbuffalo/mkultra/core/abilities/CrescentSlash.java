package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.effects.spells.InstantIndirectDamagePotion;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

/**
 * Created by Jacob on 3/24/2018.
 */
public class CrescentSlash extends BaseAbility {

    public static float BASE_DAMAGE = 4.0f;
    public static float DAMAGE_SCALE = 6.0f;

    public CrescentSlash() {
        super(MKUltra.MODID, "ability.crescent_slash");
    }

    @Override
    public String getAbilityName() {
        return "Crescent Slash";
    }

    @Override
    public String getAbilityDescription() {
        return "Strikes an enemy and all enemies around it.";
    }

    @Override
    public String getAbilityType() {
        return "AOE";
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/crescent_slash.png");
    }

    @Override
    public int getIconU() {
        return 18;
    }

    @Override
    public int getIconV() {
        return 18;
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 16 - 2 * currentLevel;
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
        return 10 - 2 * currentLevel;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 3.0f + currentLevel * 2.0f;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        int level = pData.getLevelForAbility(getAbilityId());

        EntityLivingBase targetEntity = getSingleLivingTarget(entity, getDistance(level));
        if (targetEntity != null) {
            pData.startAbility(this);

            // What to do for each target hit
            SpellCast damage = InstantIndirectDamagePotion.Create(entity, BASE_DAMAGE, DAMAGE_SCALE);
            SpellCast particlePotion = ParticlePotion.Create(entity,
                    EnumParticleTypes.SWEEP_ATTACK.getParticleID(),
                    ParticleEffects.CIRCLE_MOTION, false, new Vec3d(1.0, 1.0, 1.0),
                    new Vec3d(0.0, 1.0, 0.0), 4, 0, 1.0);

            AreaEffectBuilder.Create(entity, targetEntity)
                    .spellCast(damage, level, getTargetType())
                    .spellCast(particlePotion, level, getTargetType())
                    .instant()
                    .color(16409620).radius(5.0f, true)
                    .particle(EnumParticleTypes.CRIT)
                    .spawn();

            Vec3d lookVec = entity.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.SWEEP_ATTACK.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 5, 5,
                            targetEntity.posX, targetEntity.posY + 1.0,
                            targetEntity.posZ, 1.0, 1.0, 1.0, 1.5,
                            lookVec),
                    targetEntity, 50.0f);
        }
    }
}