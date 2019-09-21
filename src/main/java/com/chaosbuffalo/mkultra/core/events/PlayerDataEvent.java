package com.chaosbuffalo.mkultra.core.events;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Event;

public class PlayerDataEvent extends Event {
    private EntityPlayer player;
    private IPlayerData data;

    protected PlayerDataEvent(EntityPlayer player, IPlayerData data) {
        this.player = player;
        this.data = data;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public IPlayerData getPlayerData() {
        return data;
    }
}
