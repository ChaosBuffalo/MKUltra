package com.chaosbuffalo.mkultra.network.packets.client;

import com.chaosbuffalo.mkultra.init.ModItems;
import com.chaosbuffalo.mkultra.integration.Integrations;
import com.chaosbuffalo.mkultra.item.ItemHelper;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import com.chaosbuffalo.mkultra.utils.ServerUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;


public class ResurrectPlayerPacket implements IMessage {

    public ResurrectPlayerPacket() {

    }

    @Override
    public void fromBytes(ByteBuf buf) {
    }

    @Override
    public void toBytes(ByteBuf buf) {
    }

    public static class Handler extends MessageHandler.Server<ResurrectPlayerPacket> {

        @Override
        public IMessage handleServerMessage(final EntityPlayer player, ResurrectPlayerPacket msg, MessageContext ctx) {
            ServerUtils.addScheduledTask(() -> {

                if (player.getHeldItemMainhand().getItem() == ModItems.phoenix_dust) {

                    List<Entity> corpses = Integrations.getLootableBodiesAroundPlayer(player, 10);

                    Log.info("S corpses size %d", corpses.size());
                    corpses.forEach(c -> Log.info("nearby corpse %s", c.toString()));

                    Entity corpse = Integrations.getLootableBodyAroundPlayer(player, 10);
                    if (corpse == null) {
                        Log.info("could not find any corpses nearby!");
                        return;
                    }

                    Log.info("found target corpse %s", corpse.toString());

                    String corpsePlayerName = corpse.getCustomNameTag();

                    EntityPlayer targetPlayer = player.world.getPlayerEntityByName(corpsePlayerName);
                    if (targetPlayer != null) {
                        Log.info("found player for corpse %s", targetPlayer.toString());
                    } else {
                        Log.info("player for corpse not found!");
                        return;
                    }

                    targetPlayer.setPositionAndUpdate(player.posX, player.posY + 1, player.posZ);

                    ItemHelper.shrinkStack(player, player.getHeldItemMainhand(), 1);
                }
            });
            return null;
        }
    }
}
