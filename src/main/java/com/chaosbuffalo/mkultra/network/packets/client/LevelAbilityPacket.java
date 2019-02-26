package com.chaosbuffalo.mkultra.network.packets.client;

import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerData;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import com.chaosbuffalo.mkultra.utils.ServerUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class LevelAbilityPacket implements IMessage {
    private ResourceLocation abilityId;
    private boolean raise;

    public LevelAbilityPacket() {
    }

    public LevelAbilityPacket(ResourceLocation abilityId, boolean direction) {
        this.abilityId = abilityId;
        this.raise = direction;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        this.abilityId = pb.readResourceLocation();
        this.raise = pb.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeResourceLocation(this.abilityId);
        pb.writeBoolean(this.raise);
    }

    // ========================================================================

    public static class Handler extends MessageHandler.Server<LevelAbilityPacket> {


        @Override
        public void handleServerMessage(final EntityPlayer player,
                                        final LevelAbilityPacket msg,
                                        MessageContext ctx) {
            ServerUtils.addScheduledTask(() -> {
                PlayerData pData = (PlayerData) MKUPlayerData.get(player);
                if (pData == null)
                    return;

                Log.debug("Got LevelAbility packet for ability %s\n", msg.abilityId.toString());

                if (msg.raise) {
                    pData.learnAbility(msg.abilityId, true);
                } else {
                    pData.unlearnAbility(msg.abilityId, true, false);
                }
            });
        }

    }
}
