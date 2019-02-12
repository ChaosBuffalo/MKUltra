package com.chaosbuffalo.mkultra.tiles;
import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MKUMobData;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.spawner.MobDefinition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;


public class TileEntityMKSpawner extends TileEntity implements ITickable {

    public int ticksBeforeSpawn;
    public String mobDefinitionToSpawn;
    public int currentMob;
    public int tickCount;

    public TileEntityMKSpawner(){
        ticksBeforeSpawn = 5 * GameConstants.TICKS_PER_SECOND;
        mobDefinitionToSpawn = "test_skeleton";
        currentMob = -1;
        tickCount = 0;
    }


    @Override
    public void update()
    {
        if (!getWorld().isRemote){
            tickCount++;
            Log.info("In mk spawner update, entity id is %d, tick count is: %d", currentMob, tickCount);
            if (currentMob != -1){
                Entity entity = getWorld().getEntityByID(currentMob);
                if (entity == null){
                    Log.info("No entity found with id %d", currentMob);
                    currentMob = -1;
                    tickCount = 0;
                } else {
                    Log.info("Found entity with id %d", currentMob);
                }
            } else if (tickCount > ticksBeforeSpawn){
                Log.info("Trying to spawn new entity %s", mobDefinitionToSpawn);
                spawnEntity(getWorld(), mobDefinitionToSpawn);
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

    protected final void sync() {
        this.markDirty();
        Packet<?> packet = this.getUpdatePacket();
        if (packet == null) return;
        List<EntityPlayerMP> players = this.getWorld().getPlayers(EntityPlayerMP.class,
                (EntityPlayerMP p) -> p.getPosition().distanceSq(getPos()) < 256);
        for (EntityPlayerMP player : players) {
            player.connection.sendPacket(packet);
        }
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

    private void spawnEntity(World theWorld, String mobDefinitionName){
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

            definition.applyDefinition(entity, 5);
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
