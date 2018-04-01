package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.effects.Targeting;
import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.server.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.utils.RayTraceUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class KanyeCutter extends BaseAbility {

    public static float BASE_DAMAGE = 6.0f;
    public static float DAMAGE_SCALE = 2.0f;

    public KanyeCutter() {
        super(MKUltra.MODID, "ability.kanye_cutter");
    }

    @Override
    public String getAbilityName() {
        return "Kanye Cutter";
    }

    @Override
    public String getAbilityDescription() {
        return "Warps you to your target, damaging and withering them.";
    }

    @Override
    public String getAbilityType() {
        return "Single Target";
    }

    @Override
    public int getIconU() {
        return 0;
    }

    @Override
    public ResourceLocation getAbilityIcon(){
        return new ResourceLocation(MKUltra.MODID, "textures/class/abilities/kanyecutter.png");
    }


    @Override
    public int getIconV() {
        return 36;
    }

    @Override
    public int getCooldown(int currentLevel) {
        return 8 - currentLevel;
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
        return 3 + currentLevel * 2;
    }

    @Override
    public float getDistance(int currentLevel) {
        return 25.0f;
    }

    @Override
    public int getRequiredLevel(int currentLevel) {
        return currentLevel * 2;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        int level = pData.getLevelForAbility(getAbilityId());

        EntityLivingBase targetEntity = getSingleLivingTarget(entity, getDistance(level), false);
        if (targetEntity != null) {
            pData.startAbility(this);

            if (isValidTarget(entity, targetEntity)) {
                targetEntity.addPotionEffect(new PotionEffect(MobEffects.WITHER, 20 * 3, level));
                targetEntity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(entity, entity),
                        BASE_DAMAGE + DAMAGE_SCALE * level);
            }

            Vec3d position = entity.getPositionVector();
            Vec3d movementVec = position.subtract(targetEntity.getPositionVector()).normalize().scale(5.0f);
            Vec3d teleLoc = targetEntity.getPositionVector().add(new Vec3d(movementVec.x, 0.0, movementVec.z));
            RayTraceResult colTrace = RayTraceUtils.rayTraceBlocks(theWorld, targetEntity.getPositionVector(),
                    teleLoc, false);
            if (colTrace != null && colTrace.typeOfHit == RayTraceResult.Type.BLOCK){
                teleLoc = colTrace.hitVec;
            }
            Vec3d newLookVec = targetEntity.getPositionVector().subtract(teleLoc).normalize();
            float pitch = (float)Math.asin(-newLookVec.y);
            float yaw = (float)Math.atan2(newLookVec.x, newLookVec.z);
            // Unfortunately setRotation is protected so we have to set these directly.
            entity.rotationYaw = yaw;
            entity.rotationPitch = pitch;
            entity.setPositionAndUpdate(teleLoc.x, teleLoc.y, teleLoc.z);
            entity.addPotionEffect(new PotionEffect(MobEffects.RESISTANCE, 3 * 20, 5));

            Vec3d lookVec = entity.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.END_ROD.getParticleID(),
                            ParticleEffects.SPHERE_MOTION, 60, 10,
                            targetEntity.posX, targetEntity.posY + 1.0,
                            targetEntity.posZ, 1.0, 1.0, 1.0, 2.0,
                            lookVec),
                    entity.dimension, targetEntity.posX,
                    targetEntity.posY, targetEntity.posZ, 50.0f);
        }
    }
}

