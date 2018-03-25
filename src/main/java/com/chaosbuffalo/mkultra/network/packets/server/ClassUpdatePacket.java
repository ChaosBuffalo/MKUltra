package com.chaosbuffalo.mkultra.network.packets.server;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerClassInfo;
import com.chaosbuffalo.mkultra.core.PlayerDataProvider;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import com.chaosbuffalo.mkultra.utils.ClientUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClassUpdatePacket implements IMessage {

    private List<PlayerClassInfo> classes;

    public ClassUpdatePacket() {
    }

    public ClassUpdatePacket(Collection<PlayerClassInfo> knownClasses) {
        classes = new ArrayList<>(1);
        classes.addAll(knownClasses);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        int count = pb.readInt();
        classes = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            PlayerClassInfo info = new PlayerClassInfo(pb.readResourceLocation());

            info.level = buf.readInt();

            classes.add(info);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeInt(classes.size());

        for (PlayerClassInfo info : classes) {
            pb.writeResourceLocation(info.classId);
            pb.writeInt(info.level);
        }
    }

    public static class Handler extends MessageHandler.Client<ClassUpdatePacket> {

        // Client reads the serialized data from the server
        @Override
        public IMessage handleClientMessage(final EntityPlayer player, final ClassUpdatePacket msg, MessageContext ctx) {
            ClientUtils.addScheduledTask(() -> {
                if (player == null)
                    return;
                IPlayerData data = PlayerDataProvider.get(player);
                if (data == null)
                    return;

                for (PlayerClassInfo info : msg.classes) {
                    data.clientKnownClassUpdate(info);
                }
            });
            return null;

        }
    }
}
