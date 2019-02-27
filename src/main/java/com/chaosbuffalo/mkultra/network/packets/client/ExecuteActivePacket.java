package com.chaosbuffalo.mkultra.network.packets.client;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


public class ExecuteActivePacket implements IMessage {
    private int slotIndex;

    public ExecuteActivePacket() {
    }

    public ExecuteActivePacket(int slotIndex) {
        this.slotIndex = slotIndex;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.slotIndex = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.slotIndex);
    }


    public static class Handler extends MessageHandler.Server<ExecuteActivePacket> {
        @Override
        public void handleServerMessage(final EntityPlayer player,
                                        final ExecuteActivePacket message) {
            IPlayerData pData = MKUPlayerData.get(player);
            if (pData == null)
                return;

            pData.executeHotBarAbility(message.slotIndex);
        }
    }
}
