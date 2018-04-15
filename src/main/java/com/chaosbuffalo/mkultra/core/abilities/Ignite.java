package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.AreaEffectBuilder;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.api.Targeting;
import com.chaosbuffalo.mkultra.effects.spells.IgnitePotion;
import com.chaosbuffalo.mkultra.effects.spells.ParticlePotion;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


public class Ignite extends BaseAbility {

    public static float BASE_DAMAGE = 2.0f;
    public static float DAMAGE_SCALE = 4.0f;

    public Ignite() {
        super(MKUltra.MODID, "ability.ignite");
    }

    @Override
    public String getAbilityName() {
        return "Ignite";
    }

    @Override
    public String getAbilityDescription() {
        return "Damages your target, and if the target is on fire, ignites the area damaging all" +
                "surrounding targets and setting them on fire.";
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/ignite.png");
    }


    @Override
    public String getAbilityType() {
        return "Single Target";
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 17 - 2 * currentLevel;
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
        return 10 + currentLevel * 4;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 15.0f + 5.0f * currentLevel;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return 6 + currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        int level = pData.getLevelForAbility(getAbilityId());

        EntityLivingBase targetEntity = getSingleLivingTarget(entity, getDistance(level) * 2);
        if (targetEntity != null) {
            pData.startAbility(this);

            targetEntity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(entity, entity), BASE_DAMAGE + level * DAMAGE_SCALE);

            if (targetEntity.isBurning()){
                SpellCast ignite = IgnitePotion.Create(entity, BASE_DAMAGE, DAMAGE_SCALE);
                SpellCast particle = ParticlePotion.Create(entity,
                        EnumParticleTypes.FLAME.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, false, new Vec3d(1.0, 1.0, 1.0),
                        new Vec3d(0.0, 1.0, 0.0), 40, 5, 1.0);

                AreaEffectBuilder.Create(entity, entity)
                        .spellCast(ignite, level, getTargetType())
                        .spellCast(particle, level, getTargetType())
                        .instant()
                        .color(16737305).radius(getDistance(level), true)
                        .particle(EnumParticleTypes.FLAME)
                        .spawn();
            }

            Vec3d lookVec = entity.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.FLAME.getParticleID(),
                            ParticleEffects.CIRCLE_MOTION, 40, 10,
                            targetEntity.posX, targetEntity.posY + 1.0,
                            targetEntity.posZ, 1.0, 1.0, 1.0, 1.5,
                            lookVec),
                    entity.dimension, targetEntity.posX,
                    targetEntity.posY, targetEntity.posZ, 50.0f);
        }
    }
}
