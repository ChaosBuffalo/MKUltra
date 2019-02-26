
package com.chaosbuffalo.mkultra.network.packets.client;

import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerData;
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
    private boolean enforceChecks;

    public ClassLearnPacket() {
    }

    public ClassLearnPacket(ResourceLocation classId, boolean learn, boolean enforceChecks) {
        this.classId = classId;
        this.learn = learn;
        this.enforceChecks = enforceChecks;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        classId = pb.readResourceLocation();
        learn = pb.readBoolean();
        enforceChecks = pb.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeResourceLocation(classId);
        pb.writeBoolean(learn);
        pb.writeBoolean(enforceChecks);
    }

    // ========================================================================

    public static class Handler extends MessageHandler.Server<ClassLearnPacket> {

        @Override
        public void handleServerMessage(final EntityPlayer player,
                                        final ClassLearnPacket msg,
                                        MessageContext ctx) {
            ServerUtils.addScheduledTask(() -> {
                PlayerData data = (PlayerData) MKUPlayerData.get(player);
                if (data != null) {
                    boolean canSwitch;
                    // Make sure the player is an OP if they want to bypass checks
                    if (!player.canUseCommand(2, "")) {
                        msg.enforceChecks = true;
                    }
                    if (msg.learn) {
                        canSwitch = data.learnClass(msg.classId, msg.enforceChecks);
                        if (canSwitch && msg.enforceChecks) {
                            ItemStack heldItem = player.getHeldItem(EnumHand.MAIN_HAND);
                            ItemHelper.damageStack(player, heldItem, 1);
                        }
                    } else {
                        if (msg.enforceChecks) {
                            // switching. need to consume item
                            ItemStack dust = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
                            if (!(dust.getItem() instanceof DiamondDust)) {
                                return;
                            } else {
                                canSwitch = ItemHelper.shrinkStack(player, dust, 1);
                            }
                        }
                        else {
                            canSwitch = true;
                        }
                    }

                    if (canSwitch) {
                        data.activateClass(msg.classId);
                    }
                }
            });
        }

    }
}
