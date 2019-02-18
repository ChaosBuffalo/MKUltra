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
    public int spawnerType;
    public int spawnTime;
    private BlockPos pos;

    public MKSpawnerSetPacket() {
    }

    public MKSpawnerSetPacket(ResourceLocation faction, int spawnerType, int spawnTime, BlockPos pos) {
        this.factionId = faction;
        this.spawnerType = spawnerType;
        this.pos = pos;
        this.spawnTime = spawnTime;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        factionId = pb.readResourceLocation();
        spawnerType = pb.readInt();
        pos = pb.readBlockPos();
        spawnTime = pb.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeResourceLocation(factionId);
        pb.writeInt(spawnerType);
        pb.writeBlockPos(pos);
        pb.writeInt(spawnTime);
    }

    // ========================================================================

    public static class Handler extends MessageHandler.Server<MKSpawnerSetPacket> {

        @Override
        public IMessage handleServerMessage(final EntityPlayer player,
                                            final MKSpawnerSetPacket msg,
                                            MessageContext ctx) {
            ServerUtils.addScheduledTask(() -> {
                TileEntity entity = player.getEntityWorld().getTileEntity(msg.pos);
                if (entity instanceof TileEntityMKSpawner){
                    TileEntityMKSpawner mkSpawner = (TileEntityMKSpawner)entity;
                    mkSpawner.setSpawnerWithPacket(msg);
                }
            });
            return null;
        }

    }
}
