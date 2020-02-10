package com.chaosbuffalo.mkultra.core.abilities.ultimates;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.core.abilities.cast_states.CastState;
import com.chaosbuffalo.mkultra.core.abilities.cast_states.Vec3dCastState;
import com.chaosbuffalo.mkultra.entities.projectiles.EntityMeteorProjectile;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.init.ModSounds;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.mkultra.utils.AbilityUtils;
import com.chaosbuffalo.mkultra.utils.RayTraceUtils;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;


@Mod.EventBusSubscriber
public class Meteor extends PlayerAbility {

    public static final Meteor INSTANCE = new Meteor();
    public static float BASE_DAMAGE = 10.0f;
    public static float DAMAGE_SCALE = 5.0f;
    private static int SUMMON_RANGE = 3;
    private static int SUMMON_HEIGHT = 5;
    private static float VELOCITY = .1f;
    private static float INACCURACY = .05f;
    private static float RANGE = 20f;

    public Meteor() {
        super(MKUltra.MODID, "ability.meteor");
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<PlayerAbility> event) {
        event.getRegistry().register(INSTANCE.setRegistryName(INSTANCE.getAbilityId()));
    }

    public AbilityType getType() {
        return AbilityType.Ultimate;
    }

    @Override
    public int getCooldown(int currentRank) {
        return 35 - 10 * currentRank;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 10 + 2 * currentRank;
    }

    @Override
    public float getDistance(int currentRank) {
        return 2 * SUMMON_RANGE;
    }

    @Override
    public CastState createCastState(int castTime) {
        return new Vec3dCastState(castTime);
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 1;
    }

    @Override
    public SoundEvent getCastingSoundEvent() {
        return ModSounds.casting_fire;
    }

    @Nullable
    @Override
    public SoundEvent getSpellCompleteSoundEvent() {
        return null;
    }

    @Override
    public int getCastTime(int currentRank) {
        return GameConstants.TICKS_PER_SECOND * 4;
    }

    @Override
    public void continueCast(EntityPlayer entity, IPlayerData data, World theWorld, int castTimeLeft, CastState state) {
        super.continueCast(entity, data, theWorld, castTimeLeft, state);
        int tickSpeed = 4;
        if (castTimeLeft % tickSpeed == 0) {
            Vec3dCastState castState = AbilityUtils.getCastStateAsType(state, Vec3dCastState.class);
            if (castState == null)
                return;

            castState.getLocation().ifPresent(pos -> {
                EntityMeteorProjectile proj = new EntityMeteorProjectile(theWorld, entity);
                int randX = theWorld.rand.nextInt(SUMMON_RANGE * 2) - SUMMON_RANGE;
                int randZ = theWorld.rand.nextInt(SUMMON_RANGE * 2) - SUMMON_RANGE;
                proj.setPosition(pos.x + randX, pos.y + SUMMON_HEIGHT, pos.z + randZ);
                proj.shoot(pos.x, pos.y, pos.z, VELOCITY, INACCURACY);
                theWorld.spawnEntity(proj);

                Vec3d lookVec = entity.getLookVec();
                MKUltra.packetHandler.sendToAllAround(
                        new ParticleEffectSpawnPacket(
                                EnumParticleTypes.FLAME.getParticleID(),
                                ParticleEffects.SPHERE_MOTION, 8, 4,
                                entity.posX, entity.posY + 1.0,
                                entity.posZ, 1.0, 1.0, 1.0, 0.1,
                                lookVec),
                        entity, 50.0f);
            });
        }
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        CastState state = pData.startAbility(this);
        Vec3dCastState vec3dCastState = AbilityUtils.getCastStateAsType(state, Vec3dCastState.class);
        if (vec3dCastState != null) {
            RayTraceResult lookingAt = RayTraceUtils.getLookingAt(EntityLivingBase.class, entity, RANGE,
                    e -> e != null && isValidTarget(entity, e));
            if (lookingAt == null) {
                Vec3d look = entity.getLookVec().scale(RANGE);
                Vec3d from = entity.getPositionVector().add(0, entity.getEyeHeight(), 0);
                vec3dCastState.setLocation(from.add(look));
            } else {
                vec3dCastState.setLocation(lookingAt.hitVec);
            }
        }

    }
}