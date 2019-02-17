package com.chaosbuffalo.mkultra.tiles;
import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.packets.client.MKSpawnerSetPacket;
import com.chaosbuffalo.mkultra.spawn.MobDefinition;
import com.chaosbuffalo.mkultra.spawn.MobFaction;
import com.chaosbuffalo.mkultra.spawn.SpawnList;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
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
    private static double RANGE = 100.0;
    private static int TICK_INTERVAL = 10 * GameConstants.TICKS_PER_SECOND;
    private static int CLEANUP_THRESHOLD = 180 * GameConstants.TICKS_PER_SECOND;
    private static int SLEEP_TICK_INTERVAL = 30 * GameConstants.TICKS_PER_SECOND;
    private int ticksBeforeSpawn;
    private int currentMob;
    private int tickCount;
    private boolean active;
    private int ticksSincePlayer;
    private int internalTickInterval;
    private MobFaction faction;
    private MobFaction.MobGroups spawnerType;
    private SpawnList spawnList;

    public TileEntityMKSpawner(){
        internalTickInterval = TICK_INTERVAL;
        reset();
    }

    private void reset(){
        ticksBeforeSpawn = 5 * GameConstants.TICKS_PER_SECOND;
        currentMob = -1;
        tickCount = ticksBeforeSpawn;
        ticksSincePlayer = 0;
        active = false;
        faction = null;
        spawnerType = MobFaction.MobGroups.INVALID;
        spawnList = null;
    }

    private List<EntityPlayer> getPlayersAround(){
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

    public ResourceLocation getFactionName(){
        if (faction == null){
            return MKURegistry.INVALID_FACTION;
        } else {
            return faction.getRegistryName();
        }
    }

    public int getSpawnTimeSeconds(){
        return ticksBeforeSpawn / GameConstants.TICKS_PER_SECOND;
    }

    public String getMobGroupName(){
        return spawnerType.name();
    }

    public MobFaction.MobGroups getMobGroup(){
        return spawnerType;
    }

    private float getAverageLevel(List<EntityPlayer> players){
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

    private void cleanupMob(){
        Entity mob = getWorld().getEntityByID(currentMob);
        if (mob != null){
            mob.setDead();
        }
    }

    private boolean checkSpawnListAndInit(){
        if (spawnList == null){
            if (faction != null && spawnerType != MobFaction.MobGroups.INVALID){
                spawnList = faction.getSpawnListForGroup(spawnerType);
                this.sync();
                if (spawnList == null || spawnList.isEmpty()){
                    return false;
                } else {
                    return true;
                }

            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public void update()
    {
        if (!getWorld().isRemote){
            tickCount++;
            if (tickCount % internalTickInterval == 0){
                List<EntityPlayer> players = getPlayersAround();
                if (players.size() > 0){
                    if (checkSpawnListAndInit()){
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
                            spawnEntity(getWorld(), spawnList.getNextDefinition(), Math.round(averageLevel));
                        }
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

    public final void sync() {
        this.markDirty();
        Packet<?> packet = this.getUpdatePacket();
        if (packet == null) return;
        List<EntityPlayerMP> players = this.getWorld().getPlayers(EntityPlayerMP.class, (EntityPlayerMP p) -> p.getPosition().distanceSq(getPos()) < 256);
        for (EntityPlayerMP player : players) {
            player.connection.sendPacket(packet);
        }
    }


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagRoot) {
        if (faction != null){
            tagRoot.setString("faction", faction.getRegistryName().toString());
        }
        if (spawnerType != MobFaction.MobGroups.INVALID){
            tagRoot.setInteger("spawnerType", spawnerType.ordinal());
        }
        if (spawnList != null){
            tagRoot.setString("spawnList", spawnList.getRegistryName().toString());
        }
        tagRoot.setInteger("ticksBeforeSpawn", ticksBeforeSpawn);
        return super.writeToNBT(tagRoot);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagRoot) {
        if (tagRoot.hasKey("faction")){
            faction = MKURegistry.getFaction(new ResourceLocation(tagRoot.getString("faction")));
        }
        if (tagRoot.hasKey("spawnerType")){
            spawnerType = MobFaction.MobGroups.values()[tagRoot.getInteger("spawnerType")];
        }
        if (tagRoot.hasKey("spawnList")){
            spawnList = MKURegistry.getSpawnList(new ResourceLocation(tagRoot.getString("spawnList")));
        }
        if (tagRoot.hasKey("ticksBeforeSpawn")){
            ticksBeforeSpawn = tagRoot.getInteger("ticksBeforeSpawn");
            tickCount = ticksBeforeSpawn;
        }
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

    public void setSpawnerWithPacket(MKSpawnerSetPacket packet){
        reset();
        faction = MKURegistry.getFaction(packet.factionId);
        spawnerType = MobFaction.MobGroups.values()[packet.spawnerType];
        ticksBeforeSpawn = packet.spawnTime * GameConstants.TICKS_PER_SECOND;
        sync();
    }

    private void spawnEntity(World theWorld, MobDefinition definition, int level){
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
            mobData.setMobFaction(faction.getRegistryName());
            mobData.setMKSpawned(true);
        }
    }
}
