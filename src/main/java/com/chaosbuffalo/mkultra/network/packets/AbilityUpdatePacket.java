package com.chaosbuffalo.mkultra.network.packets;

import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.log.Log;
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

        try {
            NBTTagCompound list = pb.readCompoundTag();
            if (list == null)
                return;

            for (String id : list.getKeySet()) {
                ResourceLocation abilityId = new ResourceLocation(id);
                PlayerAbility ability = MKURegistry.getAbility(abilityId);
                if (ability == null)
                    continue;
                PlayerAbilityInfo info = ability.createAbilityInfo();
                NBTTagCompound tag = list.getCompoundTag(id);
                if (info.deserialize(tag))
                    skills.add(info);
            }
        }
        catch (IOException e) {
            Log.error("Failed to parse AbilityUpdatePacket!");
            Log.error("%s", e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeVarInt(skills.size());

        NBTTagCompound list = new NBTTagCompound();
        for (PlayerAbilityInfo info : skills) {
            NBTTagCompound tag = new NBTTagCompound();
            info.serialize(tag);
            list.setTag(info.getId().toString(), tag);
        }

        pb.writeCompoundTag(list);
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
