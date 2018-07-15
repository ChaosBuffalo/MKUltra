package com.chaosbuffalo.mkultra.tiles;

import cyano.poweradvantage.api.ConduitType;
import cyano.poweradvantage.api.PowerConnectorContext;
import cyano.poweradvantage.api.PowerRequest;
import cyano.poweradvantage.api.PoweredEntity;
import cyano.poweradvantage.conduitnetwork.ConduitRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;

/**
 * Created by Jacob on 4/17/2016.
 */

public class PortalTileEntity extends PoweredEntity {
    private final ConduitType[] types = {new ConduitType("steam")};
    private final float[] energyBufferSizes = {10000.0f};
    private final float[] energyBuffers = {0.0f};
    private final String unlocalizedName = "portalTile";


    public PortalTileEntity() {

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
    public void tickUpdate(boolean b) {

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
        return 1000000.0F;
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


    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tagRoot) {
        return super.writeToNBT(tagRoot);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagRoot) {
        super.readFromNBT(tagRoot);
    }

    @Override
    public boolean isPowerSource(ConduitType conduitType) {
        return false;
    }

    @Override
    public void update() {
        func_73660_a();
    }
}
