package com.chaosbuffalo.mkultra.network.packets;

import com.chaosbuffalo.mkultra.network.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PlayerLeftClickEmptyPacket implements IMessage {

    public PlayerLeftClickEmptyPacket() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static class Handler extends MessageHandler.Server<PlayerLeftClickEmptyPacket> {

        @Override
        public void handleServerMessage(final EntityPlayer player, PlayerLeftClickEmptyPacket msg) {
            MinecraftForge.EVENT_BUS.post(new PlayerInteractEvent.LeftClickEmpty(player));
        }
    }
}