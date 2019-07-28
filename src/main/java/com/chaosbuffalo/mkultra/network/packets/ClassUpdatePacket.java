package com.chaosbuffalo.mkultra.network.packets;

import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerClassInfo;
import com.chaosbuffalo.mkultra.core.PlayerData;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ClassUpdatePacket implements IMessage {

    private List<PlayerClassInfo> classes;
    private boolean fullUpdate;

    public ClassUpdatePacket() {
    }

    public ClassUpdatePacket(Collection<PlayerClassInfo> knownClasses) {
        classes = new ArrayList<>(1);
        classes.addAll(knownClasses);
        fullUpdate = true;
    }

    public ClassUpdatePacket(Collection<PlayerClassInfo> knownClasses, boolean fullUpdateIn) {
        classes = new ArrayList<>(1);
        classes.addAll(knownClasses);
        fullUpdate = fullUpdateIn;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        int count = pb.readInt();
        fullUpdate = pb.readBoolean();
        classes = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            PlayerClassInfo info = new PlayerClassInfo(pb.readResourceLocation());

            info.level = pb.readInt();
            try {
                NBTTagCompound talentData = pb.readCompoundTag();
                info.deserializeTalentInfo(talentData);
            } catch (IOException e) {
                e.printStackTrace();
            }
            classes.add(info);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeInt(classes.size());
        pb.writeBoolean(fullUpdate);

        for (PlayerClassInfo info : classes) {
            pb.writeResourceLocation(info.classId);
            pb.writeInt(info.level);
            NBTTagCompound talentData = new NBTTagCompound();
            info.serializeTalentInfo(talentData);
            pb.writeCompoundTag(talentData);
        }
    }

    public static class Handler extends MessageHandler.Client<ClassUpdatePacket> {

        // Client reads the serialized data from the server
        @Override
        public void handleClientMessage(final EntityPlayer player, final ClassUpdatePacket msg) {
            if (player == null)
                return;
            PlayerData data = (PlayerData) MKUPlayerData.get(player);
            if (data == null)
                return;

            data.clientBulkKnownClassUpdate(msg.classes, msg.fullUpdate);
        }
    }
}
