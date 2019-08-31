package com.chaosbuffalo.mkultra.network.packets;

import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerAbilityInfo;
import com.chaosbuffalo.mkultra.core.PlayerData;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class AbilityUpdatePacket implements IMessage {

    private Collection<PlayerAbilityInfo> skills;

    public AbilityUpdatePacket() {
    }

    public AbilityUpdatePacket(PlayerAbilityInfo abilityInfo) {
        skills = Collections.singletonList(abilityInfo);
    }

    public AbilityUpdatePacket(Collection<PlayerAbilityInfo> knownSkills) {
        skills = Collections.unmodifiableCollection(knownSkills);
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
        public void handleClientMessage(final EntityPlayer player, final AbilityUpdatePacket msg) {
            if (player == null)
                return;
            PlayerData data = (PlayerData) MKUPlayerData.get(player);
            if (data == null)
                return;

            for (PlayerAbilityInfo info : msg.skills) {
                data.clientSkillListUpdate(info);
            }
        }
    }
}
