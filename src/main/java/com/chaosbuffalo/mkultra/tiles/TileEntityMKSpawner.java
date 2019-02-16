package com.chaosbuffalo.mkultra.tiles;
import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.spawn.MobDefinition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;


public class TileEntityMKSpawner extends TileEntity implements ITickable {
    public static double RANGE = 200.0;
    public static int TICK_INTERVAL = 10 * GameConstants.TICKS_PER_SECOND;
    public static int CLEANUP_THRESHOLD = 180 * GameConstants.TICKS_PER_SECOND;
    public static int SLEEP_TICK_INTERVAL = 30 * GameConstants.TICKS_PER_SECOND;
    public int ticksBeforeSpawn;
    public String mobDefinitionToSpawn;
    public int currentMob;
    public int tickCount;
    public boolean active;
    private int ticksSincePlayer;
    private int internalTickInterval;

    public TileEntityMKSpawner(){
        ticksBeforeSpawn = 5 * GameConstants.TICKS_PER_SECOND;
        mobDefinitionToSpawn = "test_skeleton";
        currentMob = -1;
        tickCount = ticksBeforeSpawn;
        ticksSincePlayer = 0;
        active = false;
        internalTickInterval = TICK_INTERVAL;
    }

    public List<EntityPlayer> getPlayersAround(){
        double halfRange = RANGE / 2.0;
        double x1 = (double)this.pos.getX() - halfRange;
        double y1 = (double)this.pos.getY() - halfRange;
        double z1 = (double)this.pos.getZ() - halfRange;
        double x2 = (double)this.pos.getX() + halfRange;
        double y2 = (double)this.pos.getY() + halfRange;
        double z2 = (double)this.pos.getZ() + halfRange;
        AxisAlignedBB scanBox = new AxisAlignedBB(x1, y1, z1, x2, y2, z2);
        return getWorld().getEntitiesWithinAABB(EntityPlayer.class, scanBox, x -> !x.isCreative());
    }

    public float getAverageLevel(List<EntityPlayer> players){
        float levelTotal = 0;
        int count = 0;
        for (EntityPlayer player : players){
            IPlayerData playerData = MKUPlayerData.get(player);
            if (playerData != null) {
                levelTotal += playerData.getLevel();
                count++;
            }
        }
        return levelTotal / count;
    }

    public void cleanupMob(){
        Entity mob = getWorld().getEntityByID(currentMob);
        if (mob != null){
            mob.setDead();
        }
    }

    @Override
    public void update()
    {
        if (!getWorld().isRemote){
            tickCount++;
            if (tickCount % internalTickInterval == 0){
                List<EntityPlayer> players = getPlayersAround();
                if (players.size() > 0){
                    if (!active){
                        active = true;
                        tickCount = ticksBeforeSpawn;
                        internalTickInterval = TICK_INTERVAL;
                        ticksSincePlayer = 0;
                    }
                    float averageLevel = getAverageLevel(players);
                    if (currentMob != -1){
                        Entity entity = getWorld().getEntityByID(currentMob);
                        if (entity == null){
                            currentMob = -1;
                            // we reset to 1 instead of 0 because otherwise we would trigger 2 ticks in a row
                            // everytime we reset
                            tickCount = 1;
                        }
                    } else if (tickCount >= ticksBeforeSpawn){
                        spawnEntity(getWorld(), mobDefinitionToSpawn, Math.round(averageLevel));
                    }
                } else {
                    ticksSincePlayer++;
                    if (ticksSincePlayer >= CLEANUP_THRESHOLD && currentMob != -1){
                        cleanupMob();
                        active = false;
                        internalTickInterval = SLEEP_TICK_INTERVAL;
                    }
                }
            }
        }
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTag = this.serializeNBT();
        return new SPacketUpdateTileEntity(this.pos, 0, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet) {
        this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagRoot) {
        return super.writeToNBT(tagRoot);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagRoot) {

        super.readFromNBT(tagRoot);
    }

    private EntityLivingBase getEntity(World theWorld, MobDefinition definition) {
        if (definition.entityClass != null){
            try {
                Constructor<?> ctor = definition.entityClass.getConstructor(World.class);
                Object object = ctor.newInstance(theWorld);
                return (EntityLivingBase)object;
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void spawnEntity(World theWorld, String mobDefinitionName, int level){
        MobDefinition definition = MKURegistry.getMobDefinition(new ResourceLocation(MKUltra.MODID, mobDefinitionName));
        if (definition != MKURegistry.EMPTY_MOB){
            EntityLivingBase entity = getEntity(theWorld, definition);
            if (entity == null){
                Log.info("Get entity returned null");
                return;
            }
            IMobData mobData = MKUMobData.get(entity);
            if (mobData == null){
                Log.info("Mob data empty");
                return;
            }
            definition.applyDefinition(entity, level);
            entity.setLocationAndAngles(
                    getPos().getX() + .5f, getPos().getY() + .5f, getPos().getZ() + .5f,
                    theWorld.rand.nextFloat() * 360.0F, 0.0F);
            currentMob = entity.getEntityId();
            Log.info("Spawning entity at %s", getPos().toString());
            theWorld.spawnEntity(entity);
            mobData.setMKSpawned(true);
        }
    }
}
