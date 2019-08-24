package com.chaosbuffalo.mkultra.effects;

import com.chaosbuffalo.mkultra.log.Log;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldEventListener;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public class SpellManager implements IWorldEventListener {
    public static SpellManager INSTANCE = new SpellManager();

    private static Map<UUID, Map<SpellPotionBase, SpellCast>> allCasts =
            new HashMap<>(new HashMap<>());

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load loadEvent) {
        World world = loadEvent.getWorld();

        Log.info("Loading world %d", world.provider.getDimension());

        if (!world.isRemote) {
            world.addEventListener(INSTANCE);
        }
    }

    public static SpellCast create(SpellPotionBase potion, Entity caster) {
        return new SpellCast(potion, caster);
    }

    public static SpellCast get(EntityLivingBase target, SpellPotionBase potion) {

        Map<SpellPotionBase, SpellCast> targetSpells = allCasts.get(target.getUniqueID());
        if (targetSpells == null) {
//            Log.warn("Tried to get a spell on an unregistered target! Spell: %s", potion.getName());
            return null;
        }

        SpellCast cast = targetSpells.get(potion);
        if (cast != null) {
            cast.updateRefs(target.world);
        }
        return cast;
    }

    public static void registerTarget(SpellCast cast, EntityLivingBase target) {
        allCasts.computeIfAbsent(target.getUniqueID(), k -> new HashMap<>()).put(cast.getPotion(), cast);
    }

    @Override
    public void notifyBlockUpdate(World worldIn, BlockPos pos, IBlockState oldState, IBlockState newState, int flags) {

    }

    @Override
    public void notifyLightSet(BlockPos pos) {

    }

    @Override
    public void markBlockRangeForRenderUpdate(int x1, int y1, int z1, int x2, int y2, int z2) {

    }

    @Override
    public void playSoundToAllNearExcept(@Nullable EntityPlayer player, SoundEvent soundIn, SoundCategory category, double x, double y, double z, float volume, float pitch) {

    }

    @Override
    public void playRecord(SoundEvent soundIn, BlockPos pos) {

    }

    @Override
    public void spawnParticle(int particleID, boolean ignoreRange, double xCoord, double yCoord, double zCoord, double xSpeed, double ySpeed, double zSpeed, int... parameters) {

    }

    @Override
    public void spawnParticle(int id, boolean ignoreRange, boolean p_190570_3_, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int... parameters) {

    }

    @Override
    public void onEntityAdded(Entity entityIn) {


    }

    @Override
    public void onEntityRemoved(Entity entityIn) {
        Map<SpellPotionBase, SpellCast> entityCasts = allCasts.remove(entityIn.getUniqueID());
        if (entityCasts != null) {
            entityCasts.clear();
//            Log.info("SpellManager::onEntityRemoved deleted %s from casts. Remaining %d", entityIn.toString(), allCasts.size());
//            allCasts.forEach((key, value) -> Log.info("SpellManager::casts %s", key));
        }
    }

    @Override
    public void broadcastSound(int soundID, BlockPos pos, int data) {

    }

    @Override
    public void playEvent(EntityPlayer player, int type, BlockPos blockPosIn, int data) {

    }

    @Override
    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {

    }
}
