/*
 * ClientUtils.java (ClientUtils)
 * Package minersbasic.api.utils
 *
 * Project: Miners Basic
 *
 * Author: _Bedrock_Miner_ (minerbedrock@gmail.com)
 *
 * Last Modified: 04.04.2015
 */
package com.chaosbuffalo.mkultra.utils;

import net.minecraft.client.Minecraft;


/**
 * A collection of utility methods for the client side.
 *
 * @author _Bedrock_Miner_ (minerbedrock@gmail.com)
 */
public class ClientUtils {

    /**
     * Returns the instance of Minecraft.
     *
     * @return The instance of Minecraft.
     */
    public static Minecraft mc() {
        return Minecraft.getMinecraft();
    }

    /**
     * Adds a task to the Minecraft client task queue. Should be used when
     * processing packets, as they are executed in another thread.
     *
     * @param task the task to schedule.
     */
    public static void addScheduledTask(Runnable task) {
        mc().addScheduledTask(task);
    }
}
