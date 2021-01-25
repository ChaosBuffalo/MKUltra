//package com.chaosbuffalo.mkultra.abilities.green_knight;
//
//import com.chaosbuffalo.mkultra.GameConstants;
//import com.chaosbuffalo.mkultra.MKUltra;
//import com.chaosbuffalo.mkultra.abilities.cast_states.CastState;
//import com.chaosbuffalo.mkultra.core.IPlayerData;
//import com.chaosbuffalo.mkultra.core.PlayerAbility;
//import com.chaosbuffalo.mkultra.entities.projectiles.EntitySpiritBombProjectile;
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
//public class SpiritBomb extends PlayerAbility {
//    public static final SpiritBomb INSTANCE = new SpiritBomb();
//
//    @SubscribeEvent
//    public static void register(RegistryEvent.Register<PlayerAbility> event) {
//        event.getRegistry().register(INSTANCE.setRegistryName(INSTANCE.getAbilityId()));
//    }
//
//    public final static float BASE = 6.0f;
//    public final static float SCALE = 6.0f;
//
//    public final static float PROJECTILE_SPEED = 1.5f;
//    public final static float PROJECTILE_INACCURACY = 0.2f;
//
//    private SpiritBomb() {
//        super(MKUltra.MODID, "ability.spirit_bomb");
//    }
//
//    @Override
//    public int getCooldown(int currentRank) {
//        return 16 - 4 * currentRank;
//    }
//
//    @Override
//    public Targeting.TargetType getTargetType() {
//        return Targeting.TargetType.ENEMY;
//    }
//
//    @Override
//    public float getManaCost(int currentRank) {
//        return 4 + 2 * currentRank;
//    }
//
//    @Override
//    public int getCastTime(int currentRank) {
//        return GameConstants.TICKS_PER_SECOND + (currentRank * GameConstants.TICKS_PER_SECOND / 4);
//    }
//
//    @Override
//    public SoundEvent getCastingSoundEvent() {
//        return ModSounds.casting_holy;
//    }
//
//    @Nullable
//    @Override
//    public SoundEvent getSpellCompleteSoundEvent() {
//        return ModSounds.spell_magic_whoosh_1;
//    }
//
//    @Override
//    public void endCast(EntityPlayer entity, IPlayerData data, World theWorld, CastState state) {
//        super.endCast(entity, data, theWorld, state);
//        int level = data.getAbilityRank(getAbilityId());
//        EntitySpiritBombProjectile ballP = new EntitySpiritBombProjectile(theWorld, entity);
//        ballP.setAmplifier(level);
//        ballP.shoot(entity, entity.rotationPitch, entity.rotationYaw, 0.0F, PROJECTILE_SPEED,
//                PROJECTILE_INACCURACY);
//        theWorld.spawnEntity(ballP);
//    }
//
//    @Override
//    public int getRequiredLevel(int currentRank) {
//        return 4 + currentRank * 2;
//    }
//
//    @Override
//    public void execute(EntityPlayer entity, IPlayerData pData, World theWorld) {
//        pData.startAbility(this);
//    }
//}
