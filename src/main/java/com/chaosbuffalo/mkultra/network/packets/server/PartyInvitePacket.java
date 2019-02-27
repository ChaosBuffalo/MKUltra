package com.chaosbuffalo.mkultra.network.packets.server;

import com.chaosbuffalo.mkultra.ClientProxy;
import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import com.chaosbuffalo.mkultra.network.ModGuiHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.UUID;


public class PartyInvitePacket implements IMessage {
    private UUID invitingUUID;
    private String invitingName;

    public PartyInvitePacket() {
    }

    public PartyInvitePacket(UUID invitingUUID, String playerName) {
        this.invitingUUID = invitingUUID;
        this.invitingName = playerName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pbuff = new PacketBuffer(buf);
        this.invitingUUID = pbuff.readUniqueId();
        this.invitingName = pbuff.readString(40);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pbuff = new PacketBuffer(buf);
        pbuff.writeUniqueId(this.invitingUUID);
        pbuff.writeString(this.invitingName);
    }

    public static class Handler extends MessageHandler.Client<PartyInvitePacket> {

        // Client reads the serialized data from the server
        @Override
        public void handleClientMessage(final EntityPlayer player,
                                        final PartyInvitePacket msg) {
            if (player == null)
                return;
            ClientProxy.partyData.setInvitingUUID(msg.invitingUUID);
            ClientProxy.partyData.setInvitingName(msg.invitingName);
            player.openGui(MKUltra.INSTANCE, ModGuiHandler.PARTY_INVITE_SCREEN, player.world,
                    (int) player.posX, (int) player.posY, (int) player.posZ);
        }
    }
}
