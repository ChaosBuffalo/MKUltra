package com.chaosbuffalo.mkultra.core;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.network.packets.server.AbilityCooldownPacket;
import net.minecraft.entity.player.EntityPlayerMP;

public class AbilityTrackerServer extends AbilityTracker {

    private EntityPlayerMP player;

    public AbilityTrackerServer(EntityPlayerMP player) {
        this.player = player;
    }

    protected void notifyOnSet(PlayerAbilityInfo itemIn, int ticksIn) {
        super.notifyOnSet(itemIn, ticksIn);
        MKUltra.packetHandler.sendTo(new AbilityCooldownPacket(itemIn.id, ticksIn), player);
    }

    protected void notifyOnRemove(PlayerAbilityInfo itemIn) {
        super.notifyOnRemove(itemIn);
        MKUltra.packetHandler.sendTo(new AbilityCooldownPacket(itemIn.id, 0), player);
    }
}
