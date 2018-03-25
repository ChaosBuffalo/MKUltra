
package com.chaosbuffalo.mkultra.network.packets.client;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerDataProvider;
import com.chaosbuffalo.mkultra.item.AngelDust;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import com.chaosbuffalo.mkultra.utils.ServerUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class ClassLearnPacket implements IMessage {
    private ResourceLocation classId;
    private boolean learn;

    public ClassLearnPacket() {
    }

    public ClassLearnPacket(ResourceLocation classId, boolean learn) {
        this.classId = classId;
        this.learn = learn;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        classId = pb.readResourceLocation();
        learn = pb.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeResourceLocation(classId);
        pb.writeBoolean(learn);
    }

    // ========================================================================

    public static class Handler extends MessageHandler.Server<ClassLearnPacket> {

        @Override
        public IMessage handleServerMessage(final EntityPlayer player,
                                            final ClassLearnPacket msg,
                                            MessageContext ctx) {
            ServerUtils.addScheduledTask(() -> {
                IPlayerData data = PlayerDataProvider.get(player);
                if (data != null) {
                    if (msg.learn) {
                        data.learnClass(msg.classId);
                        ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
                        heldItem.damageItem(1, player);
                        if (heldItem.getItemDamage() >= heldItem.getMaxDamage()) {
                            player.inventory.deleteStack(heldItem);
                        }
                    } else {
                        // switching. need to consume item
                        ItemStack dust = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
                        if (!(dust.getItem() instanceof AngelDust)) {
                            return;
                        } else {
                            dust.shrink(1);
                            if (dust.getCount() == 0) {
                                player.inventory.deleteStack(dust);
                            }
                        }
                    }

                    data.activateClass(msg.classId);
                }
            });
            return null;
        }

    }
}
