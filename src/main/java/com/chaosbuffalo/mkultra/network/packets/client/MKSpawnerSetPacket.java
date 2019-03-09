package com.chaosbuffalo.mkultra.network.packets.client;

import com.chaosbuffalo.mkultra.network.MessageHandler;
import com.chaosbuffalo.mkultra.tiles.TileEntityMKSpawner;
import com.chaosbuffalo.mkultra.utils.ServerUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MKSpawnerSetPacket implements IMessage {
    public ResourceLocation factionId;
    public String spawnerType;
    private int x, y, z;
    public int spawnTime;

    public MKSpawnerSetPacket() {
    }

    public MKSpawnerSetPacket(ResourceLocation faction, String spawnerType, int spawnTime, int x, int y, int z) {
        this.factionId = faction;
        this.spawnerType = spawnerType;
        this.x = x;
        this.y = y;
        this.z = z;
        this.spawnTime = spawnTime;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        factionId = pb.readResourceLocation();
        spawnerType = pb.readString(512);
        x = pb.readInt();
        y = pb.readInt();
        z = pb.readInt();
        spawnTime = pb.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeResourceLocation(factionId);
        pb.writeString(spawnerType);
        pb.writeInt(x);
        pb.writeInt(y);
        pb.writeInt(z);
        pb.writeInt(spawnTime);
    }

    // ========================================================================

    public static class Handler extends MessageHandler.Server<MKSpawnerSetPacket> {

        @Override
        public IMessage handleServerMessage(final EntityPlayer player,
                                            final MKSpawnerSetPacket msg,
                                            MessageContext ctx) {
            ServerUtils.addScheduledTask(() -> {
                TileEntity entity = player.getEntityWorld().getTileEntity(new BlockPos(msg.x, msg.y, msg.z));
                if (entity instanceof TileEntityMKSpawner){
                    TileEntityMKSpawner mkSpawner = (TileEntityMKSpawner)entity;
                    mkSpawner.setSpawnerWithPacket(msg);
                }
            });
            return null;
        }

    }
}
