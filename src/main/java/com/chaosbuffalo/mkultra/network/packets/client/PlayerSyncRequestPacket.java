package com.chaosbuffalo.mkultra.network.packets.client;

import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerData;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


public class PlayerSyncRequestPacket implements IMessage {

    public PlayerSyncRequestPacket() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static class Handler extends MessageHandler.Server<PlayerSyncRequestPacket> {

        @Override
        public void handleServerMessage(final EntityPlayer player, PlayerSyncRequestPacket msg) {
            PlayerData data = (PlayerData) MKUPlayerData.get(player);
            if (data != null) {
                data.forceUpdate();
            }
        }
    }
}
