package com.chaosbuffalo.mkultra;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class Capabilities {
    @CapabilityInject(IPlayerData.class)
    public static Capability<IPlayerData> PLAYER_DATA_CAPABILITY = null;
}
