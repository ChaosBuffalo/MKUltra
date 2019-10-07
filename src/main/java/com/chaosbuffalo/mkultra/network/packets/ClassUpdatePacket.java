package com.chaosbuffalo.mkultra.network.packets;

import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class ClassUpdatePacket implements IMessage {

    private Collection<PlayerClassInfo> classes;
    private boolean fullUpdate;

    public ClassUpdatePacket() {
    }

    public ClassUpdatePacket(Collection<PlayerClassInfo> knownClasses) {
        classes = Collections.unmodifiableCollection(knownClasses);
        fullUpdate = true;
    }

    public ClassUpdatePacket(PlayerClassInfo info) {
        classes = Collections.singletonList(info);
        fullUpdate = false;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        int count = pb.readInt();
        fullUpdate = pb.readBoolean();
        classes = new ArrayList<>(count);

        try {
            NBTTagCompound list = pb.readCompoundTag();
            if (list == null) {
                return;
            }
            for (String key : list.getKeySet()) {
                ResourceLocation classId = new ResourceLocation(key);
                PlayerClass playerClass = MKURegistry.getClass(classId);
                if (playerClass != null) {
                    NBTTagCompound tag = list.getCompoundTag(key);
                    PlayerClassInfo classInfo = playerClass.createClassInfo();
                    classInfo.deserialize(tag);
                    classes.add(classInfo);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeInt(classes.size());
        pb.writeBoolean(fullUpdate);

        NBTTagCompound list = new NBTTagCompound();
        for (PlayerClassInfo info : classes) {
            NBTTagCompound tag = new NBTTagCompound();
            info.serialize(tag);
            list.setTag(info.getClassId().toString(), tag);
        }
        pb.writeCompoundTag(list);
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
