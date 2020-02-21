package com.chaosbuffalo.mkultra.network.packets;

import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerData;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.io.IOException;
import java.util.UUID;

public class PlayerDataSyncPacket implements IMessage {
    UUID targetId;
    NBTTagCompound updateTag;

    public PlayerDataSyncPacket() {

    }

    public PlayerDataSyncPacket(PlayerData playerData, UUID targetId) {
        this.targetId = targetId;
        updateTag = new NBTTagCompound();
        playerData.serializeClientUpdate(updateTag);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        try {
            targetId = pb.readUniqueId();
            updateTag = pb.readCompoundTag();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeUniqueId(targetId);
        pb.writeCompoundTag(updateTag);
    }

    public static class Handler extends MessageHandler.Client<PlayerDataSyncPacket> {

        // Client reads the serialized data from the server
        @Override
        public void handleClientMessage(final EntityPlayer player,
                                        final PlayerDataSyncPacket msg) {
            if (player == null)
                return;

            EntityPlayer target = player.getEntityWorld().getPlayerEntityByUUID(msg.targetId);
            if (target == null)
                return;

            PlayerData playerData = (PlayerData) MKUPlayerData.get(target);
            if (playerData != null) {
                playerData.deserializeClientUpdate(msg.updateTag);
            }
        }
    }
}
