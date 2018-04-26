package com.chaosbuffalo.mkultra.network.packets.server;

import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbilityInfo;
import com.chaosbuffalo.mkultra.core.PlayerData;
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

public class AbilityUpdatePacket implements IMessage {

    private boolean removed;
    private List<PlayerAbilityInfo> skills;

    public AbilityUpdatePacket() {
    }

    public AbilityUpdatePacket(PlayerAbilityInfo abilityInfo, boolean remove) {
        skills = new ArrayList<>(1);
        skills.add(abilityInfo);
        removed = remove;
    }

    public AbilityUpdatePacket(Collection<PlayerAbilityInfo> knownSkills) {
        skills = new ArrayList<>(1);
        skills.addAll(knownSkills);
        removed = false;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        int count = pb.readInt();
        removed = pb.readBoolean();
        skills = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            PlayerAbilityInfo info = new PlayerAbilityInfo(pb.readResourceLocation());
            if (!removed) {
                info.level = buf.readInt();
            }

            skills.add(info);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeInt(skills.size());
        pb.writeBoolean(removed);

        for (PlayerAbilityInfo info : skills) {
            pb.writeResourceLocation(info.id);
            if (!removed) {
                buf.writeInt(info.level);
            }
        }
    }

    public static class Handler extends MessageHandler.Client<AbilityUpdatePacket> {

        // Client reads the serialized data from the server
        @Override
        public IMessage handleClientMessage(final EntityPlayer player, final AbilityUpdatePacket msg, MessageContext ctx) {
            ClientUtils.addScheduledTask(() -> {
                if (player == null)
                    return;
                PlayerData data = (PlayerData) MKUPlayerData.get(player);
                if (data == null)
                    return;

                for (PlayerAbilityInfo info : msg.skills) {
                    data.clientSkillListUpdate(info, msg.removed);
                }
            });
            return null;

        }
    }
}
