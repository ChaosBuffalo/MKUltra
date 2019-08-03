/*
 * PacketHandler.java (PacketHandler)
 * Package minersbasic.api.network
 *
 * Project: Miners Basic
 *
 * Author: _Bedrock_Miner_ (minerbedrock@gmail.com)
 *
 * Last Modified: 04.04.2015
 */
package com.chaosbuffalo.mkultra.network;


import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.packets.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * This class is used to send Packets ({@link IMessage}) between server and clients.
 * <p>
 * <strong>Usage:</strong><br>
 * <strong>Creation:</strong><br>
 * {@code PacketHandler handler = new PacketHandler(String channelid)}<br>
 * <em>OR</em><br>
 * {@code PacketHandler handler = MinersbasicAPI.createPacketHandler(String channelid)}
 * <p>
 * <strong>Registering a packet:</strong><br>
 * See: {@linkplain #registerPacket(Class, MessageHandler, Side)}<br>
 * <p>
 * <strong>Sending packets:</strong><br>
 * See: {@linkplain #sendTo(IMessage, EntityPlayerMP)}<br>
 * See: {@linkplain #sendToAll(IMessage)}<br>
 * See: {@linkplain #sendToAllAround(IMessage, NetworkRegistry.TargetPoint)}<br>
 * See: {@linkplain #sendToAllAround(IMessage, int, double, double, double, double)}<br>
 * See: {@linkplain #sendToAllAround(IMessage, Entity, double)}<br>
 * See: {@linkplain #sendToServer(IMessage)}<br>
 *
 * @author _Bedrock_Miner_ (minerbedrock@gmail.com)
 */
public final class PacketHandler {

	/** The ID for the next packet registration. */

	private byte nextPacketID = 0;
	/** The internal network wrapper. */
	private SimpleNetworkWrapper wrapper;
	/** The channelid. */
	private String channelid;

	/**
	 * Instantiates a new packet handler with the given channelid and reserves
	 * the channel.
	 *
	 * @param channelid
	 * the channelid. This is mostly the modid.
	 */
	public PacketHandler(String channelid) {
		this.wrapper = NetworkRegistry.INSTANCE.newSimpleChannel(channelid);
		this.channelid = channelid;
	}

	public void registerPackets() {
		registerPacket(PlayerSyncRequestPacket.class, new PlayerSyncRequestPacket.Handler(), Side.SERVER);
		registerPacket(ClassLearnPacket.class, new ClassLearnPacket.Handler(), Side.SERVER);
		registerPacket(LevelAbilityPacket.class, new LevelAbilityPacket.Handler(), Side.SERVER);
		registerPacket(ExecuteActivePacket.class, new ExecuteActivePacket.Handler(), Side.SERVER);
		registerPacket(LevelUpRequestPacket.class, new LevelUpRequestPacket.Handler(), Side.SERVER);
		registerPacket(PartyInviteResponsePacket.class, new PartyInviteResponsePacket.Handler(), Side.SERVER);
		registerPacket(MKSpawnerSetPacket.class, new MKSpawnerSetPacket.Handler(), Side.SERVER);
		registerPacket(AddTalentRequestPacket.class, new AddTalentRequestPacket.Handler(), Side.SERVER);
		registerPacket(AddRemoveTalentPointPacket.class, new AddRemoveTalentPointPacket.Handler(), Side.SERVER);


		registerPacket(ParticleEffectSpawnPacket.class, new ParticleEffectSpawnPacket.Handler(), Side.CLIENT);
		registerPacket(AbilityUpdatePacket.class, new AbilityUpdatePacket.Handler(), Side.CLIENT);
		registerPacket(ClassUpdatePacket.class, new ClassUpdatePacket.Handler(), Side.CLIENT);
		registerPacket(PartyInvitePacket.class, new PartyInvitePacket.Handler(), Side.CLIENT);
		registerPacket(ForceOpenClientGUIPacket.class, new ForceOpenClientGUIPacket.Handler(), Side.CLIENT);
		registerPacket(AbilityCooldownPacket.class, new AbilityCooldownPacket.Handler(), Side.CLIENT);
		registerPacket(CritMessagePacket.class, new CritMessagePacket.Handler(), Side.CLIENT);
		registerPacket(OpenLearnClassTileEntityPacket.class, new OpenLearnClassTileEntityPacket.Handler(), Side.CLIENT);
		registerPacket(OpenTalentGuiPacket.class, new OpenTalentGuiPacket.Handler(), Side.CLIENT);
	}

	/**
	 * Register an IMessage packet with it's corresponding message handler.
	 *
	 * @param packetClass
	 * the packet's class that should be registered.
	 * @param messageHandler
	 * the message handler for this packet type.
	 * @param target
	 * The side to which this packet can be sent.
	 * @return <code>true</code>, if successful
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean registerPacket(Class<? extends IMessage> packetClass, MessageHandler messageHandler, Side target) {
		if (this.nextPacketID == -1)
			throw new IllegalStateException("Too many packets registered for channel " + this.channelid);

		this.wrapper.registerMessage(messageHandler, packetClass, this.nextPacketID, target);
		Log.debug("Registered packet class %s with handler class %s for the channel %s. Send direction: to %s. The discriminator is %s.", packetClass.getSimpleName(), messageHandler.getClass().getSimpleName(), this.channelid, target.name().toLowerCase(), this.nextPacketID);
		this.nextPacketID++;
		return true;
	}

	/**
	 * Sends the given packet to every client.
	 *
	 * @param message
	 * the packet to send.
	 */
	public void sendToAll(IMessage message) {
		this.wrapper.sendToAll(message);
	}

	/**
	 * Sends the given packet to the given player.
	 *
	 * @param message
	 * the packet to send.
	 * @param player
	 * the player to send the packet to.
	 */
	public void sendTo(IMessage message, EntityPlayerMP player) {
		if (player.connection != null)
			this.wrapper.sendTo(message, player);
	}

	/**
	 * Sends the given packet to all players around the given target point.
	 *
	 * @param message
	 * the packet to send.
	 * @param point
	 * the target point.
	 */
	public void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
		this.wrapper.sendToAllAround(message, point);
	}

	/**
	 * Sends the given packet to all players within the radius around the given coordinates.
	 *
	 * @param message
	 * the packet to send.
	 * @param dimension
	 * the dimension.
	 * @param x
	 * the x coordinate.
	 * @param y
	 * the y coordinate.
	 * @param z
	 * the z coordinate.
	 * @param range
	 * the radius.
	 */
	public void sendToAllAround(IMessage message, int dimension, double x, double y, double z, double range) {
		this.sendToAllAround(message, new NetworkRegistry.TargetPoint(dimension, x, y, z, range));
	}

	/**
	 * Sends the given packet to all players within the radius around the given entity.
	 *
	 * @param message
	 * the packet to send.
	 * @param entity
	 * the entity.
	 * @param range
	 * the radius.
	 */
	public void sendToAllAround(IMessage message, Entity entity, double range) {
		this.sendToAllAround(message, entity.dimension, entity.posX, entity.posY, entity.posZ, range);
	}

	/**
	 * Sends the given packet to the server.
	 *
	 * @param message
	 * the packet to send.
	 */
	public void sendToServer(IMessage message) {
		this.wrapper.sendToServer(message);
	}
}