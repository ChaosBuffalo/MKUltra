package com.chaosbuffalo.mkultra.network.packets.client;

import com.chaosbuffalo.mkultra.item.interfaces.IExtendedReach;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import com.chaosbuffalo.mkultra.utils.ServerUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class RangeSwordAttackPacket implements IMessage {

    private int entityId;

    public RangeSwordAttackPacket() {
    }

    public RangeSwordAttackPacket(int entityId) {
        this.entityId = entityId;

    }

    @Override
    public void fromBytes(ByteBuf buf) {
        entityId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(entityId);
    }

    public static class Handler extends MessageHandler.Server<RangeSwordAttackPacket> {
        @Override
        public IMessage handleServerMessage(final EntityPlayer thePlayer,
                                            final RangeSwordAttackPacket message,
                                            MessageContext ctx) {

            // Know it will be on the server so make it thread-safe
            ServerUtils.addScheduledTask(
                    () -> {
                        {
                            Entity theEntity = thePlayer.world.getEntityByID(message.entityId);
                            if (theEntity == null)
                                return;

                            ItemStack mainHand = thePlayer.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);

                            // Need to ensure that hackers can't cause trick kills,
                            // so double check weapon type and reach
                            if (!(mainHand.getItem() instanceof IExtendedReach)) {
                                return;
                            }

                            IExtendedReach weapon = (IExtendedReach) mainHand.getItem();
                            double distanceSq = thePlayer.getDistanceSq(theEntity);
                            double reachSq = weapon.getReach() * weapon.getReach();
                            if (reachSq >= distanceSq) {
                                thePlayer.attackTargetEntityWithCurrentItem(theEntity);
                            }
                        }
                    }
            );
            return null; // no response message
        }
    }
}
