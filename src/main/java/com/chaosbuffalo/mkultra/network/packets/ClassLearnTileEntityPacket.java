package com.chaosbuffalo.mkultra.network.packets;

import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.PlayerData;
import com.chaosbuffalo.mkultra.item.DiamondDust;
import com.chaosbuffalo.mkultra.item.ItemHelper;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ClassLearnTileEntityPacket implements IMessage {
    private ResourceLocation classId;
    private boolean learn;
    private boolean enforceChecks;
    private BlockPos pos;

    public ClassLearnTileEntityPacket() {
    }

    public ClassLearnTileEntityPacket(ResourceLocation classId, boolean learn, boolean enforceChecks, BlockPos pos) {
        this.classId = classId;
        this.learn = learn;
        this.enforceChecks = enforceChecks;
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        classId = pb.readResourceLocation();
        learn = pb.readBoolean();
        enforceChecks = pb.readBoolean();
        pos = pb.readBlockPos();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeResourceLocation(classId);
        pb.writeBoolean(learn);
        pb.writeBoolean(enforceChecks);
        pb.writeBlockPos(pos);
    }

    // ========================================================================

    public static class Handler extends MessageHandler.Server<ClassLearnTileEntityPacket> {

        @Override
        public void handleServerMessage(final EntityPlayer player,
                                        final ClassLearnTileEntityPacket msg) {
            PlayerData data = (PlayerData) MKUPlayerData.get(player);
            if (data != null) {
                boolean canSwitch;
                // Make sure the player is an OP if they want to bypass checks
                if (!player.canUseCommand(2, "")) {
                    msg.enforceChecks = true;
                }
                if (msg.learn) {
                    canSwitch = data.learnClassTileEntity(msg.classId, msg.enforceChecks, msg.pos);
                } else {
                    if (msg.enforceChecks) {
                        // switching. need to consume item
                        ItemStack dust = player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
                        if (!(dust.getItem() instanceof DiamondDust)) {
                            return;
                        } else {
                            canSwitch = ItemHelper.shrinkStack(player, dust, 1);
                        }
                    } else {
                        canSwitch = true;
                    }
                }

                if (canSwitch) {
                    data.activateClass(msg.classId);
                }
            }
        }
    }
}