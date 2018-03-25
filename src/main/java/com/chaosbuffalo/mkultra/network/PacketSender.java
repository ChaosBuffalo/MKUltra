/*
 * PacketSender.java (PacketSender)
 * Package minersbasic.api.network
 *
 * Project: Miners Basic
 *
 * Author: _Bedrock_Miner_ (minerbedrock@gmail.com)
 *
 * Last Modified: 04.04.2015
 */
package com.chaosbuffalo.mkultra.network;

import com.chaosbuffalo.mkultra.utils.ServerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Method collection for easy broadcasting of vanilla packages.
 *
 * @author _Bedrock_Miner_ (minerbedrock@gmail.com)
 */
public final class PacketSender {

	/**
	 * Sends a vanilla packet to the server.<br>
	 * This may only be called on client side!
	 *
	 * @param packet
	 * The packet to send.
	 */
	@SideOnly(Side.CLIENT)
	public static void sendToServer(Packet packet) {
		Minecraft.getMinecraft().getConnection().sendPacket(packet);
	}


	/**
	 * Sends a vanilla packet to the specified player.<br>
	 * This may only be called on server side!
	 *
	 * @param packet
	 * The packet to send. If <code>null</code>, the packet is sent to everyone.
	 * @param player
	 * The recipient of the packet.
	 */
	public static void sendToPlayer(Packet packet, EntityPlayerMP player) {
		if (player == null)
			sendToAll(packet);
		else
			player.connection.sendPacket(packet);
	}

	/**
	 * Sends a vanilla packet to every player.<br>
	 * This may only be called on server side!
	 *
	 * @param packet
	 * The packet to send.
	 */
	public static void sendToAll(Packet packet) {
		FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().sendPacketToAllPlayers(packet);
	}

	/**
	 * Sends a vanilla packet to every player near by the target point.<br>
	 * This may only be called on server side!
	 *
	 * @param packet
	 * The packet to send.
	 * @param target
	 * The target point to send the packet to.
	 */
	public static void sendToAllAround(Packet packet, NetworkRegistry.TargetPoint target) {
		ServerUtils.mc().getPlayerList().sendToAllNearExcept(null, target.x, target.y, target.z, target.range, target.dimension, packet);
	}

	/**
	 * Sends a vanilla packet to every player near by the target point.<br>
	 * This may only be called on server side!
	 *
	 * @param packet The packet to send.
	 * @param dimension the dimension
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @param z the z coordinate
	 * @param range the range
	 */
	public static void sendToAllAround(Packet packet, int dimension, double x, double y, double z, double range) {
		ServerUtils.mc().getPlayerList().sendToAllNearExcept(null, x, y, z, range, dimension, packet);
	}

	/**
	 * Sends a vanilla packet to every player near by the target.<br>
	 * This may only be called on server side!
	 *
	 * @param packet The packet to send.
	 * @param target the target entity
	 * @param range the range around the entity
	 */
	public static void sendToAllAround(Packet packet, Entity target, double range) {
		ServerUtils.mc().getPlayerList().sendToAllNearExcept(null, target.posX, target.posY, target.posZ, range, target.dimension, packet);
	}

	/**
	 * Sends a vanilla packet to every player in the specified dimension.<br>
	 * This may only be called on server side!
	 *
	 * @param packet
	 * The packet to send
	 * @param dim
	 * The dimension to send the packet into.
	 */
	public static void sendToDimension(Packet packet, int dim) {
		ServerUtils.mc().getPlayerList().sendPacketToAllPlayersInDimension(packet, dim);
	}
}
