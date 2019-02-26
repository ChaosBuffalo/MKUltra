package com.chaosbuffalo.mkultra.network.packets.client;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import com.chaosbuffalo.mkultra.utils.ServerUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class LevelUpRequestPacket implements IMessage {


    public LevelUpRequestPacket() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static class Handler extends MessageHandler.Server<LevelUpRequestPacket> {

        // Client reads the serialized data from the server
        @Override
        public void handleServerMessage(final EntityPlayer player,
                                            LevelUpRequestPacket msg,
                                            MessageContext ctx) {
            ServerUtils.addScheduledTask(() -> {
                IPlayerData data = MKUPlayerData.get(player);
                if (data != null) {
                    data.levelUp();
                }
            });
        }
    }
}
