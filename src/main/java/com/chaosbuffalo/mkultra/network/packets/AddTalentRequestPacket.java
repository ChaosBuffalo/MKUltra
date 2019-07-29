package com.chaosbuffalo.mkultra.network.packets;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class AddTalentRequestPacket implements IMessage {


    public AddTalentRequestPacket() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static class Handler extends MessageHandler.Server<AddTalentRequestPacket> {

        // Client reads the serialized data from the server
        @Override
        public void handleServerMessage(final EntityPlayer player,
                                        AddTalentRequestPacket msg) {
            IPlayerData data = MKUPlayerData.get(player);
            if (data != null) {
                data.gainTalentPoint();
            }
        }
    }
}