package com.chaosbuffalo.mkultra.network.packets;

import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerData;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PlayerStartCastPacket implements IMessage {

    ResourceLocation abilityId;
    int castTicks;

    public PlayerStartCastPacket() {

    }

    public PlayerStartCastPacket(ResourceLocation abilityId, int castTicks) {
        this.abilityId = abilityId;
        this.castTicks = castTicks;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        abilityId = pb.readResourceLocation();
        castTicks = pb.readVarInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeResourceLocation(abilityId);
        pb.writeVarInt(castTicks);
    }

    public static class Handler extends MessageHandler.Client<PlayerStartCastPacket> {

        // Client reads the serialized data from the server
        @Override
        public void handleClientMessage(final EntityPlayer player,
                                        final PlayerStartCastPacket msg) {
            if (player == null)
                return;

            PlayerData playerData = (PlayerData) MKUPlayerData.get(player);
            if (playerData != null) {
                playerData.startCastClient(msg.abilityId, msg.castTicks);
            }
        }
    }
}
