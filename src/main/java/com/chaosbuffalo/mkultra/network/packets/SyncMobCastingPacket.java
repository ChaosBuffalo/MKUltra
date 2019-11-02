package com.chaosbuffalo.mkultra.network.packets;

import com.chaosbuffalo.mkultra.core.IMobData;
import com.chaosbuffalo.mkultra.core.MKUMobData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerData;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


public class SyncMobCastingPacket implements IMessage {

    private int entityId;
    private int castTicks;
    private ResourceLocation currentCast;

    public SyncMobCastingPacket() {
    }

    public SyncMobCastingPacket(Entity entity, int castTicks, ResourceLocation currentCast) {
        this.entityId = entity.getEntityId();
        this.castTicks = castTicks;
        this.currentCast = currentCast;
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        entityId = pb.readInt();
        castTicks = pb.readInt();
        currentCast = pb.readResourceLocation();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeInt(entityId);
        pb.writeInt(castTicks);
        pb.writeResourceLocation(currentCast);
    }

    public static class Handler extends MessageHandler.Client<SyncMobCastingPacket> {

        // Client reads the serialized data from the server
        @Override
        public void handleClientMessage(final EntityPlayer player, final SyncMobCastingPacket msg) {
            Entity entity = player.world.getEntityByID(msg.entityId);
            if (entity instanceof EntityLivingBase){
                IMobData mobData = MKUMobData.get((EntityLivingBase) entity);
                if (mobData != null){
                    mobData.setCastTicks(msg.castTicks);
                    mobData.setCastingAbility(msg.currentCast);
                }
            }
        }
    }
}