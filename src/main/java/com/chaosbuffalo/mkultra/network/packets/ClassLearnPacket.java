
package com.chaosbuffalo.mkultra.network.packets;

import com.chaosbuffalo.mkultra.core.MKUPlayerData;
import com.chaosbuffalo.mkultra.core.MKURegistry;
import com.chaosbuffalo.mkultra.core.PlayerClass;
import com.chaosbuffalo.mkultra.core.PlayerData;
import com.chaosbuffalo.mkultra.item.DiamondDust;
import com.chaosbuffalo.mkultra.item.ItemHelper;
import com.chaosbuffalo.mkultra.core.IClassProvider;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ClassLearnPacket implements IMessage {
    private ResourceLocation classId;
    private boolean learn;
    private boolean enforceChecks;
    private BlockPos pos;
    private LearnSource source;

    enum LearnSource {
        ITEM,
        TILE_ENTITY
    }

    public ClassLearnPacket() {
    }

    public ClassLearnPacket(ResourceLocation classId, boolean learn, boolean enforceChecks) {
        this.classId = classId;
        this.learn = learn;
        this.enforceChecks = enforceChecks;
        this.source = LearnSource.ITEM;
        this.pos = null;
    }

    public ClassLearnPacket(ResourceLocation classId, boolean learn, boolean enforceChecks, BlockPos pos) {
        this.classId = classId;
        this.learn = learn;
        this.enforceChecks = enforceChecks;
        this.pos = pos;
        this.source = LearnSource.TILE_ENTITY;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        classId = pb.readResourceLocation();
        learn = pb.readBoolean();
        enforceChecks = pb.readBoolean();
        source = LearnSource.values()[pb.readInt()];
        if (source == LearnSource.TILE_ENTITY) {
            pos = pb.readBlockPos();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketBuffer pb = new PacketBuffer(buf);
        pb.writeResourceLocation(classId);
        pb.writeBoolean(learn);
        pb.writeBoolean(enforceChecks);
        pb.writeInt(source.ordinal());
        if (source == LearnSource.TILE_ENTITY) {
            pb.writeBlockPos(pos);
        }
    }

    // ========================================================================

    public static class Handler extends MessageHandler.Server<ClassLearnPacket> {

        private IClassProvider getProvider(EntityPlayer player, ClassLearnPacket msg) {

            if (msg.source == LearnSource.TILE_ENTITY) {
                TileEntity tileEntity = player.world.getTileEntity(msg.pos);
                if (tileEntity instanceof IClassProvider){
                    return (IClassProvider) tileEntity;
                } else {
                    Log.error("could not find TE!");
                    return null;
                }
            } else if (msg.source == LearnSource.ITEM) {
                ItemStack mainHandStack = player.getHeldItemMainhand();
                if (mainHandStack.isEmpty())
                    return null;
                Item mainHand = mainHandStack.getItem();
                if (mainHand instanceof IClassProvider) {
                    return ((IClassProvider) mainHand);
                }
            }
            return null;
        }

        @Override
        public void handleServerMessage(final EntityPlayer player,
                                        final ClassLearnPacket msg) {
            PlayerData data = (PlayerData) MKUPlayerData.get(player);
            if (data == null)
                return;

            PlayerClass baseClass = MKURegistry.getClass(msg.classId);
            if (baseClass == null)
                return;

            boolean canSwitch;
            // Make sure the player is an OP if they want to bypass checks
            if (!player.canUseCommand(2, "")) {
                msg.enforceChecks = true;
            }

            IClassProvider provider = getProvider(player, msg);
            if (msg.enforceChecks && provider == null)
                return;

            if (msg.learn) {
                canSwitch = data.learnClass(provider, msg.classId, msg.enforceChecks);
                if (msg.source == LearnSource.ITEM && canSwitch && msg.enforceChecks) {
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
