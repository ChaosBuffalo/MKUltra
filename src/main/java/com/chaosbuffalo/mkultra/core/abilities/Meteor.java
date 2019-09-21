package com.chaosbuffalo.mkultra.core.abilities;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.CastState;
import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbility;
import com.chaosbuffalo.mkultra.entities.projectiles.EntityMeteorProjectile;
import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.packets.ParticleEffectSpawnPacket;
import com.chaosbuffalo.targeting_api.Targeting;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


@Mod.EventBusSubscriber
public class Meteor extends PlayerAbility {

    public static final Meteor INSTANCE = new Meteor();
    public static float BASE_DAMAGE = 10.0f;
    public static float DAMAGE_SCALE = 5.0f;
    private static int SUMMON_RANGE = 3;
    private static int SUMMON_HEIGHT = 5;
    private static float VELOCITY = .1f;
    private static float INACCURACY = .05f;

    public Meteor() {
        super(MKUltra.MODID, "ability.meteor");
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<PlayerAbility> event) {
        Log.info(INSTANCE.toString());
        INSTANCE.setRegistryName(INSTANCE.getAbilityId());
        event.getRegistry().register(INSTANCE);
    }

    @Override
    public int getCooldown(int currentRank) {
        return 40 - 10 * currentRank;
    }

    @Override
    public Targeting.TargetType getTargetType() {
        return Targeting.TargetType.ENEMY;
    }

    @Override
    public float getManaCost(int currentRank) {
        return 8 + 2 * currentRank;
    }

    @Override
    public float getDistance(int currentRank) {
        return 2 * SUMMON_RANGE;
    }

    @Override
    public int getRequiredLevel(int currentRank) {
        return 1;
    }

    @Override
    public int getCastTime(int currentRank) {
        return GameConstants.TICKS_PER_SECOND * 4;
    }

    @Override
    public void continueCast(EntityPlayer entity, IPlayerData data, World theWorld, int castTimeLeft, CastState state) {
        super.continueCast(entity, data, theWorld, castTimeLeft, state);
        int tickSpeed = 4;
        if (castTimeLeft % tickSpeed == 0){

            EntityMeteorProjectile proj = new EntityMeteorProjectile(theWorld, entity);
            BlockPos pos = entity.getPosition();
            int randX = theWorld.rand.nextInt(SUMMON_RANGE * 2) - SUMMON_RANGE;
            int randZ = theWorld.rand.nextInt(SUMMON_RANGE * 2) - SUMMON_RANGE;
            proj.setPosition(pos.getX() + randX, pos.getY() + SUMMON_HEIGHT,
                    pos.getZ() + randZ);
            proj.shoot(pos.getX(), pos.getY(), pos.getZ(), VELOCITY, INACCURACY);
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
        }
    }

    @Override
    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
        pData.startAbility(this);
    }
}