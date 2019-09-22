package com.chaosbuffalo.mkultra.core.events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.player.PlayerEvent;


public class ServerSideLeftClickEmpty extends PlayerEvent {
    private final EnumHand hand;
    private final BlockPos pos;

    public ServerSideLeftClickEmpty(EntityPlayer player) {
        super(player);
        this.hand = EnumHand.MAIN_HAND;
        this.pos = new BlockPos(player);
    }

    public EnumHand getHand() {
        return hand;
    }

    public BlockPos getPos() {
        return pos;
    }
}
