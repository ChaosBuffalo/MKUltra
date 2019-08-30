/*
 * ServerUtils.java (ServerUtils)
 * Package minersbasic.api.utils
 *
 * Project: Miners Basic
 *
 * Author: _Bedrock_Miner_ (minerbedrock@gmail.com)
 *
 * Last Modified: 04.04.2015
 */
package com.chaosbuffalo.mkultra.utils;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * A collection of utility methods for server side usage.
 *
 * @author _Bedrock_Miner_ (minerbedrock@gmail.com)
 */
public class ServerUtils {

    /**
     * Returns the instance of MinecraftServer.
     *
     * @return The instance of MinecraftServer.
     */
    public static MinecraftServer mc() {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }

    /**
     * Adds a task to the Minecraft server task queue. Should be used when
     * processing packets, as they are executed in another thread.
     *
     * @param task the task to schedule.
     */
    public static void addScheduledTask(Runnable task) {
        mc().addScheduledTask(task);
    }

}
