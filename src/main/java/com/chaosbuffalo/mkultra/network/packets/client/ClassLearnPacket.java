
package com.chaosbuffalo.mkultra.network.packets.client;

import com.chaosbuffalo.mkultra.core.IPlayerData;
import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.item.DiamondDust;
import com.chaosbuffalo.mkultra.item.ItemHelper;
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
                IPlayerData data = MKUPlayerData.get(player);
                if (data != null) {
                    boolean canSwitch;
                    if (msg.learn) {
                        canSwitch = data.learnClass(msg.classId);
                        if (canSwitch) {
                            ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
                            ItemHelper.damageStack(player, heldItem, 1);
                        }
                    } else {
                        // switching. need to consume item
                        ItemStack dust = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
                        if (!(dust.getItem() instanceof DiamondDust)) {
                            return;
                        } else {
                            canSwitch = ItemHelper.shrinkStack(player, dust, 1);
                        }
                    }

                    if (canSwitch) {
                        data.activateClass(msg.classId);
                    }
                }
            });
            return null;
        }

    }
}
