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

    private List<PlayerAbilityInfo> skills;

    public AbilityUpdatePacket() {
    }

    public AbilityUpdatePacket(PlayerAbilityInfo abilityInfo) {
        skills = new ArrayList<>(1);
        skills.add(abilityInfo);
    }

    public AbilityUpdatePacket(Collection<PlayerAbilityInfo> knownSkills) {
        skills = new ArrayList<>(1);
        skills.addAll(knownSkills);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        int count = pb.readVarInt();
        skills = new ArrayList<>(count);

        for (int i = 0; i < count; i++) {
            PlayerAbilityInfo info = new PlayerAbilityInfo(pb.readResourceLocation(), pb.readVarInt());
            skills.add(info);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeVarInt(skills.size());

        for (PlayerAbilityInfo info : skills) {
            pb.writeResourceLocation(info.getId());
            pb.writeVarInt(info.getRank());
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
                    data.clientSkillListUpdate(info);
                }
            });
            return null;

        }
    }
}
