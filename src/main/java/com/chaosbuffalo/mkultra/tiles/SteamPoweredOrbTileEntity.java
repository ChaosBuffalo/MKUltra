package com.chaosbuffalo.mkultra.tiles;

import com.chaosbuffalo.mkultra.blocks.SteamPoweredOrbBlock;
import cyano.poweradvantage.api.ConduitType;
import cyano.poweradvantage.api.PowerConnectorContext;
import cyano.poweradvantage.api.PowerRequest;
import cyano.poweradvantage.api.PoweredEntity;
import cyano.poweradvantage.conduitnetwork.ConduitRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

/**
 * Created by Jacob on 8/8/2016.
 */

public class SteamPoweredOrbTileEntity  extends PoweredEntity {
    private final ConduitType[] types = {new ConduitType("steam")};
    private final float[] energyBufferSizes = {1000.0f};
    private final float[] energyBuffers = {0.0f};
    private final String unlocalizedName = "steamPoweredOrbTile";


    public SteamPoweredOrbTileEntity() {

    }

    @Override
    public float getEnergyCapacity(ConduitType conduitType) {
        ConduitType[] types = this.getTypes();

        for (int i = 0; i < types.length; ++i) {
            ConduitType t = types[i];
            if (ConduitType.areSameType(t, conduitType)) {
                return this.energyBufferSizes[i];
            }
        }
        return 0.0F;
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
    public float getEnergy(ConduitType conduitType) {
        ConduitType[] types = this.getTypes();
        int limit = Math.min(types.length, this.energyBuffers.length);

        for (int i = 0; i < limit; ++i) {
            ConduitType t = types[i];
            if (ConduitType.areSameType(t, conduitType)) {
                return this.energyBuffers[i];
            }
        }

        return 0.0F;
    }

    @Override
    public void setEnergy(float v, ConduitType conduitType) {
        ConduitType[] types = this.getTypes();
        for (int i = 0; i < types.length; ++i) {
            ConduitType t = types[i];
            if (ConduitType.areSameType(t, conduitType)) {
                this.energyBuffers[i] = v;

            }
        }

    }

    @Override
    public void tickUpdate(boolean isServer) {

        if (isServer) {
            int newState = 0;
            ConduitType steamType = this.getTypes()[0];
            float currentSteam = this.getEnergy(steamType);
            float ratio = currentSteam / this.getEnergyCapacity(steamType);
            if (currentSteam > 0.0) {
                newState = Math.max((int) Math.round(ratio * 7.0), 1);
            }
            SteamPoweredOrbBlock block = (SteamPoweredOrbBlock) this.world.getBlockState(this.getPos()).getBlock();
            this.world.setBlockState(this.getPos(), block.withPoweredState(newState));
            int count = Math.max(Math.round(ratio * 10), 1);
            if (this.world.rand.nextInt(count) > 8) {
                damageSurrounding();
            }
            if (this.getEnergy(this.getTypes()[0]) >= 0.5f) {
                this.subtractEnergy(0.5f, this.getTypes()[0]);
            }

        }
    }

    public void damageSurrounding() {
        double collisionRadius = 1.65;
        BlockPos pos = this.getPos();
        Vec3d middle = new Vec3d(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5);

        List<EntityLivingBase> targets = this.world.getEntitiesWithinAABB(EntityLivingBase.class,
                new AxisAlignedBB(middle.x - collisionRadius,
                        middle.y - collisionRadius, middle.z - collisionRadius, middle.x + collisionRadius,
                        middle.y + collisionRadius, middle.z + collisionRadius));
        for (EntityLivingBase target : targets) {
            if (this.getEnergy(this.getTypes()[0]) >= 10.0f) {
                target.attackEntityFrom(DamageSource.MAGIC, 1.0f);
                this.subtractEnergy(10.0f, this.getTypes()[0]);
                if (target instanceof EntityPlayer) {
                    EntityPlayer playerTarget = (EntityPlayer) target;
                    playerTarget.getFoodStats().setFoodLevel(Math.max(playerTarget.getFoodStats().getFoodLevel() - 1, 0));
                }
            }

        }
    }

    float oldSteam = 0;

    @Override
    public void powerUpdate() {
        ConduitType[] types = this.getTypes();
        boolean flagUpdate = true;
        for (ConduitType type : types) {
            if (this.isPowerSource(type)) {
                float availableEnergy = this.getEnergy(type);
                if (this.getEnergy(type) != oldSteam) {
                    flagUpdate = true;
                    oldSteam = this.getEnergy(type);
                }
                if (availableEnergy > 0.0F) {
                    this.subtractEnergy(
                            this.transmitPowerToConsumers(availableEnergy, type, this.getMinimumSinkPriority()), type);
                }
            }
        }
        if (flagUpdate) {
            this.sync();
        }
    }

    protected float transmitPowerToConsumers(float availableEnergy, ConduitType powerType, byte minimumPriority) {
        return ConduitRegistry.transmitPowerToConsumers(Math.min(this.getMaximumPowerFlux(), availableEnergy),
                powerType, minimumPriority, this);
    }

    public float getMaximumPowerFlux() {
        return 30.0F;
    }

    protected byte getMinimumSinkPriority() {
        return (byte) -50;
    }

    @Override
    public PowerRequest getPowerRequest(ConduitType conduitType) {
        if (this.isPowerSink(conduitType)) {
            ConduitType[] types = this.getTypes();

            for (ConduitType type : types) {
                if (ConduitType.areSameType(type, conduitType) &&
                        this.getEnergyCapacity(conduitType) > this.getEnergy(conduitType)) {
                    return new PowerRequest(
                            this.isPowerSource(conduitType) ? this.getMinimumSinkPriority() - 1 : 50,
                            Math.min(this.getMaximumPowerFlux(),
                                    this.getEnergyCapacity(conduitType) - this.getEnergy(conduitType)),
                            this);
                }
            }
        }

        return PowerRequest.REQUEST_NOTHING;
    }


    @Override
    public boolean canAcceptConnection(PowerConnectorContext powerConnectorContext) {
        ConduitType[] myTypes = this.getTypes();

        for (ConduitType myType : myTypes) {
            if (ConduitType.areSameType(myType, powerConnectorContext.powerType)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ConduitType[] getTypes() {
        return this.types;
    }

    @Override
    public boolean isPowerSink(ConduitType conduitType) {
        ConduitType[] types = this.getTypes();
        for (ConduitType type : types) {
            if (ConduitType.areSameType(type, conduitType)) {
                return true;
            }
        }
        return false;
    }


//    @Override
//    public NBTTagCompound writeToNBT(NBTTagCompound tagRoot) {
//        return super.writeToNBT(tagRoot);
//    }
//
//    @Override
//    public void readFromNBT(NBTTagCompound tagRoot) {
//        super.readFromNBT(tagRoot);
//    }

    @Override
    public boolean isPowerSource(ConduitType conduitType) {
        return false;
    }

    @Override
    public void update() {
        func_73660_a();
    }
}
