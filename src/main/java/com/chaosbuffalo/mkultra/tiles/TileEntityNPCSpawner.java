package com.chaosbuffalo.mkultra.tiles;

import com.chaosbuffalo.mkultra.GameConstants;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.client.gui.NPCEquipmentContainer;
import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.item.interfaces.IClassProvider;
import com.chaosbuffalo.mkultra.log.Log;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class TileEntityNPCSpawner extends TileEntity implements ITickable, IClassProvider {
    private static double RANGE = 100.0;
    private static int TICK_INTERVAL = 10 * GameConstants.TICKS_PER_SECOND;
    private static int CLEANUP_THRESHOLD = 180 * GameConstants.TICKS_PER_SECOND;
    private static int SLEEP_TICK_INTERVAL = 30 * GameConstants.TICKS_PER_SECOND;
    public static final int SIZE = 6;
    private int ticksBeforeSpawn;
    private int currentMob;
    private int tickCount;
    private boolean active;
    private int ticksSincePlayer;
    private int internalTickInterval;
    private ResourceLocation npcName;
    private ItemStackHandler itemStackHandler = new ItemStackHandler(SIZE) {
        @Override
        protected void onContentsChanged(int slot) {
            // We need to tell the tile entity that something has changed so
            // that the chest contents is persisted
            TileEntityNPCSpawner.this.handleContentsChanged(slot);
        }
    };

    public TileEntityNPCSpawner(){
        internalTickInterval = TICK_INTERVAL;
        npcName = new ResourceLocation(MKUltra.MODID, "ranger");
        reset();
    }

    public void handleContentsChanged(int slot){
        markDirty();
        ItemStack newItem = itemStackHandler.getStackInSlot(slot);
        EntityEquipmentSlot slotType = NPCEquipmentContainer.slotTypes.get(slot);
        if (currentMob != -1){
            Entity mob = getWorld().getEntityByID(currentMob);
            if (mob != null){
                mob.setItemStackToSlot(slotType, newItem);
            }
        }
    }

    public void setNPCName(ResourceLocation npcName){
        this.npcName = npcName;
    }

    private void reset(){
        ticksBeforeSpawn = 120 * GameConstants.TICKS_PER_SECOND;
        currentMob = -1;
        tickCount = ticksBeforeSpawn;
        ticksSincePlayer = 0;
        active = false;
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
        return getWorld().getEntitiesWithinAABB(EntityPlayer.class, scanBox);
    }

    public int getSpawnTimeSeconds(){
        return ticksBeforeSpawn / GameConstants.TICKS_PER_SECOND;
    }

    public void cleanupMob(){
        Entity mob = getWorld().getEntityByID(currentMob);
        if (mob != null){
            mob.setDead();
        }
        currentMob = -1;
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
                    if (currentMob != -1){
                        Entity entity = getWorld().getEntityByID(currentMob);
                        if (entity == null){
                            currentMob = -1;
                            // we reset to 1 instead of 0 because otherwise we would trigger 2 ticks in a row
                            // everytime we reset
                            tickCount = 1;
                        }
                    } else if (tickCount >= ticksBeforeSpawn){
                        spawnEntity(getWorld());
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
        tagRoot.setInteger("ticksBeforeSpawn", ticksBeforeSpawn);
        tagRoot.setString("mobName", npcName.toString());
        tagRoot.setTag("items", itemStackHandler.serializeNBT());
        return super.writeToNBT(tagRoot);
    }

    public NBTTagCompound serializeForItem(){
        NBTTagCompound tagRoot = new NBTTagCompound();
        tagRoot.setInteger("ticksBeforeSpawn", ticksBeforeSpawn);
        tagRoot.setString("mobName", npcName.toString());
        tagRoot.setTag("items", itemStackHandler.serializeNBT());
        return tagRoot;
    }

    @Override
    public void readFromNBT(NBTTagCompound tagRoot) {
        if (tagRoot.hasKey("ticksBeforeSpawn")){
            ticksBeforeSpawn = tagRoot.getInteger("ticksBeforeSpawn");
            tickCount = ticksBeforeSpawn;
        }
        if (tagRoot.hasKey("items")) {
            itemStackHandler.deserializeNBT((NBTTagCompound) tagRoot.getTag("items"));
        }
        if (tagRoot.hasKey("mobName")){
            npcName = new ResourceLocation(tagRoot.getString("mobName"));
        }
        super.readFromNBT(tagRoot);
    }

    public void readFromNBTItem(NBTTagCompound tagRoot){
        if (tagRoot.hasKey("ticksBeforeSpawn")){
            ticksBeforeSpawn = tagRoot.getInteger("ticksBeforeSpawn");
            tickCount = ticksBeforeSpawn;
        }
        if (tagRoot.hasKey("items")) {
            itemStackHandler.deserializeNBT((NBTTagCompound) tagRoot.getTag("items"));
        }
        if (tagRoot.hasKey("mobName")){
            npcName = new ResourceLocation(tagRoot.getString("mobName"));
        }
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

    private EntityLivingBase getEntity(World theWorld) {
        Class<? extends Entity> mobClass = EntityList.getClass(npcName);
//        Log.info("Got class for %s, %s", npcName.toString(), mobClass.toString());
        if (mobClass != null){
            try {
                Constructor<?> ctor = mobClass.getConstructor(World.class);
                Object object = ctor.newInstance(theWorld);
                return (EntityLivingBase)object;
            } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(itemStackHandler);
        }
        return super.getCapability(capability, facing);
    }

    private void spawnEntity(World theWorld){
        if (npcName != null){
//            Log.info("Trying spawn entity");
            EntityLivingBase entity = getEntity(theWorld);
            if (entity == null){
//                Log.info("Get entity returned null");
                return;
            }
            IMobData mobData = MKUMobData.get(entity);
            if (mobData == null){
//                Log.info("Mob data empty");
                return;
            }
            entity.setLocationAndAngles(
                    getPos().getX() + .5f, getPos().getY() + .5f, getPos().getZ() + .5f,
                    theWorld.rand.nextFloat() * 360.0F, 0.0F);
            currentMob = entity.getEntityId();
            entity.getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH)
                    .setBaseValue(200.0f);
            entity.setHealth(entity.getMaxHealth());
//            Log.info("Spawning entity at %s", getPos().toString());
            int index = 0;
            for (EntityEquipmentSlot slot : NPCEquipmentContainer.slotTypes){
                ItemStack toEquip = itemStackHandler.getStackInSlot(index);
                entity.setItemStackToSlot(slot, toEquip);
                index++;
            }
            theWorld.spawnEntity(entity);
            mobData.setMKSpawned(true);
            mobData.setSpawnPoint(getPos());
        }
    }

    @Override
    public ResourceLocation getIconForProvider() {
        return new ResourceLocation(MKUltra.MODID, "textures/class/icons/ranger.png");
    }

    @Override
    public String getClassSelectionText() {
        return "The Watchful Ranger offers to teach you his knowledge. Choose your class: ";
    }

    @Override
    public String getXpTableText() {
        return "The Watchful Ranger taught you the basics, but you must exchange brouzouf to learn more.";
    }

    @Override
    public ResourceLocation getXpTableBackground() {
        return new ResourceLocation(MKUltra.MODID, "textures/gui/xp_table_background_ranger.png");
    }

    @Override
    public int getXpTableTextColor() {
        return 16707252;
    }
}
