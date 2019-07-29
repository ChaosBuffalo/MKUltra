package com.chaosbuffalo.mkultra.network.packets;

import com.chaosbuffalo.mkultra.MKUltra;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import com.chaosbuffalo.mkultra.network.ModGuiHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class OpenTalentGuiPacket implements IMessage {
    public BlockPos tile_entity_pos;

    public OpenTalentGuiPacket() {
    }

    public OpenTalentGuiPacket(BlockPos tile_entity_pos) {
        this.tile_entity_pos = tile_entity_pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pbuff = new PacketBuffer(buf);
        this.tile_entity_pos = pbuff.readBlockPos();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pbuff = new PacketBuffer(buf);
        pbuff.writeBlockPos(tile_entity_pos);
    }

    public static class Handler extends MessageHandler.Client<OpenTalentGuiPacket> {

        // Client reads the serialized data from the server
        @Override
        public void handleClientMessage(final EntityPlayer player,
                                        final OpenTalentGuiPacket msg) {
            if (player == null)
                return;
            player.openGui(MKUltra.INSTANCE, ModGuiHandler.TALENT_SCREEN, player.world,
                    msg.tile_entity_pos.getX(), msg.tile_entity_pos.getY(), msg.tile_entity_pos.getZ());
        }
    }
}