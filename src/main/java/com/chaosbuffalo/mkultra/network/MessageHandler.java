package com.chaosbuffalo.mkultra.network;

import com.chaosbuffalo.mkultra.utils.ClientUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public abstract class MessageHandler<T extends IMessage> implements IMessageHandler<T, IMessage> {

	@SideOnly(Side.CLIENT)
	public abstract IMessage handleClientMessage(final EntityPlayer player, final T msg, final MessageContext ctx);

	public abstract IMessage handleServerMessage(final EntityPlayer player, final T msg, final MessageContext ctx);

	@SideOnly(Side.CLIENT)
	private IMessage runHandleClient(T message, MessageContext ctx) {
		return this.handleClientMessage(ctx.side.isClient() ? ClientUtils.mc().player : ctx.getServerHandler().player, message, ctx);
	}

	@Override
	public final IMessage onMessage(T message, MessageContext ctx) {
		if (ctx.side.isClient()) {
			return this.runHandleClient(message, ctx);
		} else {
			return this.handleServerMessage(ctx.getServerHandler().player, message, ctx);
		}
	}

	public static abstract class Client<T extends IMessage> extends MessageHandler<T> {
		@Override
		public final IMessage handleServerMessage(EntityPlayer player, T message, MessageContext ctx) {
			return null;
		}
	}

	public static abstract class Server<T extends IMessage> extends MessageHandler<T> {
		@Override
		public final IMessage handleClientMessage(EntityPlayer player, T message, MessageContext ctx) {
			return null;
		}
	}

}
