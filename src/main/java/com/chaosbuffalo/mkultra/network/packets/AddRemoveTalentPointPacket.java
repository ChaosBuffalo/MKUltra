package com.chaosbuffalo.mkultra.network.packets;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class AddRemoveTalentPointPacket implements IMessage {
    private ResourceLocation talentTree;
    private String line;
    private int index;
    private Mode mode;

    public enum Mode {
        SPEND,
        REFUND
    }

    public AddRemoveTalentPointPacket() {

    }

    public AddRemoveTalentPointPacket(ResourceLocation tree, String line, int index, Mode mode) {
        talentTree = tree;
        this.line = line;
        this.index = index;
        this.mode = mode;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        talentTree = pb.readResourceLocation();
        line = pb.readString(1024);
        index = pb.readInt();
        mode = pb.readEnumValue(Mode.class);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeResourceLocation(talentTree);
        pb.writeString(line);
        pb.writeInt(index);
        pb.writeEnumValue(mode);

    }

    public static class Handler extends MessageHandler.Server<AddRemoveTalentPointPacket> {

        // Client reads the serialized data from the server
        @Override
        public void handleServerMessage(final EntityPlayer player,
                                        AddRemoveTalentPointPacket msg) {

            IPlayerData data = MKUPlayerData.get(player);
            if (data != null) {
                Log.info("Handling packet for %s %s, %s, %d", msg.mode.toString(), msg.talentTree.toString(), msg.line, msg.index);
                if (msg.mode == Mode.REFUND) {
                    data.refundTalentPoint(msg.talentTree, msg.line, msg.index);
                } else if (msg.mode == Mode.SPEND) {
                    data.spendTalentPoint(msg.talentTree, msg.line, msg.index);
                }
            }
        }
    }
}
