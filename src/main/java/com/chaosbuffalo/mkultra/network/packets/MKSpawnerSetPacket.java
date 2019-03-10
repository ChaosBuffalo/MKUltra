package com.chaosbuffalo.mkultra.network.packets;

import com.chaosbuffalo.mkultra.network.MessageHandler;
import com.chaosbuffalo.mkultra.tiles.TileEntityMKSpawner;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MKSpawnerSetPacket implements IMessage {
    public ResourceLocation factionId;
    public String spawnerType;
    public int spawnTime;
    private BlockPos pos;

    public MKSpawnerSetPacket() {
    }


    public MKSpawnerSetPacket(ResourceLocation faction, String spawnerType, int spawnTime, BlockPos pos) {
        this.factionId = faction;
        this.spawnerType = spawnerType;
        this.pos = pos;
        this.spawnTime = spawnTime;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        factionId = pb.readResourceLocation();
        spawnerType = pb.readString(512);
        pos = pb.readBlockPos();
        spawnTime = pb.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeResourceLocation(factionId);
        pb.writeString(spawnerType);
        pb.writeBlockPos(pos);
        pb.writeInt(spawnTime);
    }

    // ========================================================================

    public static class Handler extends MessageHandler.Server<MKSpawnerSetPacket> {

        @Override
        public void handleServerMessage(final EntityPlayer player,
                                        final MKSpawnerSetPacket msg) {
            TileEntity entity = player.getEntityWorld().getTileEntity(msg.pos);
            if (entity instanceof TileEntityMKSpawner && player.canUseCommandBlock()) {
                TileEntityMKSpawner mkSpawner = (TileEntityMKSpawner) entity;
                mkSpawner.setSpawnerWithPacket(msg);
            }
        }

    }
}
