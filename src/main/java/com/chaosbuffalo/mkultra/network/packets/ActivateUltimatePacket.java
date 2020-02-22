package com.chaosbuffalo.mkultra.network.packets;

import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerData;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ActivateUltimatePacket implements IMessage {
    private ResourceLocation loc;
    private int slotIndex;

    public ActivateUltimatePacket() {

    }

    public ActivateUltimatePacket(ResourceLocation loc, int slotIndex) {
        this.loc = loc;
        this.slotIndex = slotIndex;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        loc = pb.readResourceLocation();
        slotIndex = pb.readInt();

    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeResourceLocation(loc);
        pb.writeInt(slotIndex);
    }

    public static class Handler extends MessageHandler.Server<ActivateUltimatePacket> {

        // Client reads the serialized data from the server
        @Override
        public void handleServerMessage(final EntityPlayer player,
                                        ActivateUltimatePacket msg) {

            PlayerData data = (PlayerData) MKUPlayerData.get(player);
            if (data != null) {
                data.activateUltimate(msg.loc, msg.slotIndex);
            }
        }
    }
}