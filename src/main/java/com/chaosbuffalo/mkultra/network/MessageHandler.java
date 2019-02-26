package com.chaosbuffalo.mkultra.network;

import com.chaosbuffalo.mkultra.utils.ClientUtils;
import com.chaosbuffalo.mkultra.utils.ServerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public abstract class MessageHandler<T extends IMessage> implements IMessageHandler<T, IMessage> {

	@SideOnly(Side.CLIENT)
	public abstract void handleClientMessage(final EntityPlayer player, final T msg, final MessageContext ctx);

	public abstract void handleServerMessage(final EntityPlayer player, final T msg, final MessageContext ctx);

	@SideOnly(Side.CLIENT)
	private void runHandleClient(T message, MessageContext ctx) {
		EntityPlayer player = ctx.side.isClient() ? ClientUtils.mc().player : ctx.getServerHandler().player;
		ClientUtils.addScheduledTask(() -> handleClientMessage(player, message, ctx));
	}

	private void runHandleServer(T message, MessageContext ctx) {
		EntityPlayer player = ctx.getServerHandler().player;
		ServerUtils.addScheduledTask(() -> handleServerMessage(player, message, ctx));
	}

	@Override
	public final IMessage onMessage(T message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			this.runHandleClient(message, ctx);
		} else {
			this.runHandleServer(message, ctx);
		}
		return null;
	}

	public static abstract class Client<T extends IMessage> extends MessageHandler<T> {
		@Override
		public final void handleServerMessage(EntityPlayer player, T message, MessageContext ctx) {
		}
	}

	public static abstract class Server<T extends IMessage> extends MessageHandler<T> {
		@Override
		public final void handleClientMessage(EntityPlayer player, T message, MessageContext ctx) {
		}
	}

}
