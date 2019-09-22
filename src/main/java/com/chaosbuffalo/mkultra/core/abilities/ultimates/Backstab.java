package com.chaosbuffalo.mkultra.core.abilities.ultimates;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKDamageSource;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.utils.MathUtils;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class Backstab extends PlayerAbility {

    public static final Backstab INSTANCE = new Backstab();

    public static float BASE_DAMAGE = 10.0f;
    public static float DAMAGE_SCALE = 10.0f;

    public Backstab() {
        super(MKUltra.MODID, "ability.backstab");
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<PlayerAbility> event) {
        INSTANCE.setRegistryName(INSTANCE.getAbilityId());
        event.getRegistry().register(INSTANCE);
    }

    @Override
    public int getCooldown(int currentRank) {
        return 20 - 4 * currentRank;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 8 + currentRank * 2;
    }

    @Override
    public float getDistance(int currentRank) {
        return 4.0f;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 1;
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        int level = pData.getAbilityRank(getAbilityId());

        EntityLivingBase targetEntity = getSingleLivingTarget(entity, getDistance(level));
        if (targetEntity != null) {
            pData.startAbility(this);
            boolean isBehind = MathUtils.isBehind(targetEntity, entity);
            float damage = BASE_DAMAGE + DAMAGE_SCALE * level;
            if (!isBehind){
                targetEntity.attackEntityFrom(
                        MKDamageSource.fromMeleeSkill(getAbilityId(), entity, entity, 0.75f),
                        damage / 2.0f);
            } else {
                pData.addToAllCooldowns(-GameConstants.TICKS_PER_SECOND * 2);
                targetEntity.attackEntityFrom(
                        MKDamageSource.fromMeleeSkill(getAbilityId(), entity, entity, 1.25f), damage);
            }

            Vec3d lookVec = entity.getLookVec();
            MKUltra.packetHandler.sendToAllAround(
                    new ParticleEffectSpawnPacket(
                            EnumParticleTypes.SWEEP_ATTACK.getParticleID(),
                            ParticleEffects.CIRCLE_MOTION, 4, 1,
                            targetEntity.posX, targetEntity.posY + 1.0,
                            targetEntity.posZ, 1.0, 1.0, 1.0, 0.1,
                            lookVec),
                    entity.dimension, targetEntity.posX,
                    targetEntity.posY, targetEntity.posZ, 50.0f);
        }
    }
}

