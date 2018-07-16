package com.chaosbuffalo.mkultra.network.packets.server;

import com.chaosbuffalo.mkultra.core.BaseAbility;
import com.chaosbuffalo.mkultra.core.ClassData;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import com.chaosbuffalo.mkultra.utils.ClientUtils;
import com.chaosbuffalo.mkultra.utils.ServerUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by Jacob on 7/15/2018.
 */
public class CritMessagePacket implements IMessage {
    public enum CritType{
        INDIRECT_MAGIC_CRIT,
        MELEE_CRIT,
        SPELL_CRIT,
        INDIRECT_CRIT,
    }
    private int targetId;
    private UUID sourceUUID;
    private ResourceLocation abilityName;
    private float critDamage;
    private CritType type;

    public CritMessagePacket() {
    }

    public CritMessagePacket(int targetId, UUID sourceUUID, float critDamage, CritType type) {
        this.targetId = targetId;
        this.sourceUUID = sourceUUID;
        this.critDamage = critDamage;
        this.type = type;
    }

    public CritMessagePacket(int targetId, UUID sourceUUID, float critDamage, ResourceLocation abilityName) {
        this.targetId = targetId;
        this.sourceUUID = sourceUUID;
        this.critDamage = critDamage;
        this.type = CritType.SPELL_CRIT;
        this.abilityName = abilityName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        this.type = CritType.values()[pb.readInt()];
        this.targetId = pb.readInt();
        this.sourceUUID = pb.readUniqueId();
        this.critDamage = pb.readFloat();
        if (type == CritType.SPELL_CRIT){
            this.abilityName = pb.readResourceLocation();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeInt(type.ordinal());
        pb.writeInt(targetId);
        pb.writeUniqueId(sourceUUID);
        pb.writeFloat(critDamage);
        if (type == CritType.SPELL_CRIT){
            pb.writeResourceLocation(this.abilityName);
        }
    }

    // ========================================================================

    public static class Handler extends MessageHandler.Client<CritMessagePacket> {


        @Override
        public IMessage handleClientMessage(final EntityPlayer player,
                                            final CritMessagePacket msg,
                                            MessageContext ctx) {
            ClientUtils.addScheduledTask(() -> {
                Style messageStyle = new Style();
                boolean isSelf = player.getUniqueID().equals(msg.sourceUUID);
                EntityPlayer playerSource = player.getEntityWorld().getPlayerEntityByUUID(msg.sourceUUID);
                Entity target = player.getEntityWorld().getEntityByID(msg.targetId);
                if (target == null || playerSource == null){
                    return;
                }
                switch (msg.type){
                    case MELEE_CRIT:
                    case INDIRECT_CRIT:
                        messageStyle.setColor(
                                msg.type == CritType.MELEE_CRIT ? TextFormatting.DARK_RED : TextFormatting.GOLD
                        );
                        if (isSelf){
                            player.sendMessage(new TextComponentString(
                                    String.format("You just crit %s with %s for %s",
                                            target.getDisplayName().getUnformattedText(),
                                            playerSource.getHeldItemMainhand().getDisplayName(),
                                            Float.toString(msg.critDamage)))
                                    .setStyle(messageStyle));
                        } else {
                            player.sendMessage(new TextComponentString(
                                    String.format("%s just crit %s with %s for %s",
                                            playerSource.getDisplayName().getUnformattedText(),
                                            target.getDisplayName().getUnformattedText(),
                                            playerSource.getHeldItemMainhand().getDisplayName(),
                                            Float.toString(msg.critDamage))
                            ).setStyle(messageStyle));
                        }
                    case INDIRECT_MAGIC_CRIT:
                        messageStyle.setColor(TextFormatting.BLUE);
                        if (isSelf){
                            player.sendMessage(new TextComponentString(
                                    String.format("Your magic spell just crit %s for %s",
                                            target.getDisplayName().getUnformattedText(),
                                            Float.toString(msg.critDamage)))
                                    .setStyle(messageStyle));
                        } else {
                            player.sendMessage(new TextComponentString(
                                    String.format("%s's magic spell just crit %s for %s",
                                            playerSource.getDisplayName().getUnformattedText(),
                                            target.getDisplayName().getUnformattedText(),
                                            Float.toString(msg.critDamage)))
                                    .setStyle(messageStyle));
                        }
                    case SPELL_CRIT:
                        messageStyle.setColor(TextFormatting.AQUA);
                        BaseAbility ability = ClassData.getAbility(msg.abilityName);
                        if (isSelf) {
                            player.sendMessage(new TextComponentString(
                                    String.format("Your %s spell just crit %s for %s",
                                            ability.getAbilityName(),
                                            target.getDisplayName().getUnformattedText(),
                                            Float.toString(msg.critDamage)))
                                    .setStyle(messageStyle)
                            );

                        } else {
                            player.sendMessage(new TextComponentString(
                                    String.format("%s's %s spell just crit %s for %s",
                                            playerSource.getDisplayName().getUnformattedText(),
                                            ability.getAbilityName(),
                                            target.getDisplayName().getUnformattedText(),
                                            Float.toString(msg.critDamage)))
                                    .setStyle(messageStyle)
                            );
                        }
                }
            });
            return null;
        }

    }
}