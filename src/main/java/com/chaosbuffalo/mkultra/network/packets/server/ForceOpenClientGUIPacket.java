package com.chaosbuffalo.mkultra.network.packets.server;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


public class ForceOpenClientGUIPacket implements IMessage {
    private int guiId;

    public ForceOpenClientGUIPacket() {
    }

    public ForceOpenClientGUIPacket(int guiId) {
        this.guiId = guiId;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.guiId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.guiId);
    }

    public static class Handler extends MessageHandler.Client<ForceOpenClientGUIPacket> {

        // Client reads the serialized data from the server
        @Override
        public void handleClientMessage(final EntityPlayer player,
                                        final ForceOpenClientGUIPacket msg) {
            if (player == null)
                return;
            player.openGui(MKUltra.INSTANCE, msg.guiId, player.world,
                    (int) player.posX, (int) player.posY, (int) player.posZ);
        }
    }
}
