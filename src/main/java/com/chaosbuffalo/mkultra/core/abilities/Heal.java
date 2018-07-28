package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.SpellCast;
import com.chaosbuffalo.mkultra.effects.spells.ClericHealPotion;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Heal extends BaseAbility {

    public static float BASE_VALUE = 5.0f;
    public static float VALUE_SCALE = 5.0f;

    public Heal() {
        super(MKUltra.MODID, "ability.heal");
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 6 - currentLevel;
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
    public boolean canSelfCast() {
        return true;
    }

    @Override
    public int getManaCost(int currentLevel) {
        return 2 + currentLevel * 2;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 10.0f + 5.0f * currentLevel;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return currentLevel * 2;
    }

    @Override
    protected boolean isValidTarget(EntityLivingBase caster, EntityLivingBase target) {
        return ClericHealPotion.INSTANCE.isValidTarget(getTargetType(), caster, target, !canSelfCast());
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {

        int level = pData.getLevelForAbility(getAbilityId());
        EntityLivingBase targetEntity = getSingleLivingTargetOrSelf(entity, getDistance(level), true);

        pData.startAbility(this);

        SpellCast heal = ClericHealPotion.Create(entity, BASE_VALUE, VALUE_SCALE).setTarget(targetEntity);
        targetEntity.addPotionEffect(heal.toPotionEffect(level));

        Vec3d lookVec = entity.getLookVec();
        MKUltra.packetHandler.sendToAllAround(
                new ParticleEffectSpawnPacket(
                        EnumParticleTypes.VILLAGER_HAPPY.getParticleID(),
                        ParticleEffects.SPHERE_MOTION, 50, 10,
                        targetEntity.posX, targetEntity.posY + 1.0f,
                        targetEntity.posZ, 1.0, 1.0, 1.0, 1.5,
                        lookVec),
                entity.dimension, targetEntity.posX,
                targetEntity.posY, targetEntity.posZ, 50.0f);

    }
}

