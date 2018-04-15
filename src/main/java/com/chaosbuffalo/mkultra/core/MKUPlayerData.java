package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.Capabilities;
import net.minecraft.entity.player.EntityPlayer;

public class MKUPlayerData {
    public static IPlayerData get(EntityPlayer player)
    {
        if (player.hasCapability(Capabilities.PLAYER_DATA_CAPABILITY, null)) {
            return player.getCapability(Capabilities.PLAYER_DATA_CAPABILITY, null);
        }
        return null;
    }
}
