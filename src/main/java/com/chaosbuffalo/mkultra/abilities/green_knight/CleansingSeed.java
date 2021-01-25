//package com.chaosbuffalo.mkultra.abilities.green_knight;
//
//import com.chaosbuffalo.mkultra.GameConstants;
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.abilities.cast_states.CastState;
//import com.chaosbuffalo.mkultra.core.IPlayerData;
//import com.chaosbuffalo.mkultra.core.PlayerAbility;
//import com.chaosbuffalo.mkultra.entities.projectiles.EntityCleansingSeedProjectile;
//import com.chaosbuffalo.mkultra.init.ModSounds;
//import com.chaosbuffalo.targeting_api.Targeting;
//import net.minecraft.entity.player.EntityPlayer;
//import net.minecraft.util.SoundEvent;
//import net.minecraft.world.World;
//import net.minecraftforge.event.RegistryEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
//
//import javax.annotation.Nullable;
//
//@Mod.EventBusSubscriber(modid = MKUltra.MODID)
//public class CleansingSeed extends PlayerAbility {
//    public static final CleansingSeed INSTANCE = new CleansingSeed();
//
//    @SubscribeEvent
//    public static void register(RegistryEvent.Register<PlayerAbility> event) {
//        event.getRegistry().register(INSTANCE.setRegistryName(INSTANCE.getAbilityId()));
//    }
//
//    public static float PROJECTILE_SPEED = 1.25f;
//    public static float PROJECTILE_INACCURACY = 0.2f;
//    public static float BASE_DAMAGE = 4.0f;
//    public static float DAMAGE_SCALE = 4.0f;
//
//    private CleansingSeed() {
//        super(MKUltra.MODID, "ability.cleansing_seed");
//    }
//
//    @Override
//    public int getCooldown(int currentRank) {
//        return 8 - 2 * currentRank;
//    }
//
//    @Override
//    public Targeting.TargetType getTargetType() {
//        return Targeting.TargetType.ALL;
//    }
//
//    @Override
//    public float getManaCost(int currentRank) {
//        return 1 + 2 * currentRank;
//    }
//
//
//    @Override
//    public int getRequiredLevel(int currentRank) {
//        return 4 + currentRank * 2;
//    }
//
//
//    @Nullable
//    @Override
//    public SoundEvent getSpellCompleteSoundEvent() {
//        return ModSounds.spell_cast_6;
//    }
//
//    @Override
//    public SoundEvent getCastingSoundEvent() {
//        return ModSounds.casting_water;
//    }
//
//    @Override
//    public int getCastTime(int currentRank) {
//        return GameConstants.TICKS_PER_SECOND - 5 * (currentRank - 1);
//    }
//
//    @Override
//    public void endCast(EntityPlayer entity, IPlayerData data, World theWorld, CastState state) {
//        super.endCast(entity, data, theWorld, state);
//        int level = data.getAbilityRank(getAbilityId());
//        EntityCleansingSeedProjectile ballP = new EntityCleansingSeedProjectile(theWorld, entity);
//        ballP.setAmplifier(level);
//        ballP.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, PROJECTILE_SPEED,
//                PROJECTILE_INACCURACY);
//        theWorld.spawnEntity(ballP);
//    }
//
//    @Override
//    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
//        pData.startAbility(this);
//    }
//}
//
//
