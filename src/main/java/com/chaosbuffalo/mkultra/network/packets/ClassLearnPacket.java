package com.chaosbuffalo.mkultra.network.packets;

import com.chaosbuffalo.mkultra.core.*;
import com.chaosbuffalo.mkultra.log.Log;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
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
                return IClassProvider.getProvider(tileEntity);
            } else if (msg.source == LearnSource.ITEM) {
                return IClassProvider.getProvider(player.getHeldItemMainhand());
            }
            return null;
        }

        boolean isPlayerRestricted(EntityPlayer player) {
            // Make sure the player is an OP if they want to bypass checks
            return !player.canUseCommand(2, "");
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

            if (isPlayerRestricted(player)) {
                msg.enforceChecks = true;
            }

            IClassProvider provider = msg.enforceChecks ? getProvider(player, msg) : IClassProvider.TEACH_ALL;
            if (provider == null) {
                if (msg.enforceChecks) {
                    Log.error("Player %s tried to learn class %s with a null class provider!", player, msg.classId);
                    return;
                } else {
                    provider = IClassProvider.TEACH_ALL;
                }
            }

            if (msg.enforceChecks && !provider.meetsRequirements(player, baseClass))
                return;

            boolean canActivate = true;
            if (msg.learn) {
                canActivate = data.learnClass(provider, msg.classId);
            }
            if (canActivate) {
                if (msg.enforceChecks) {
                    provider.onProviderUse(player, baseClass);
                }
                data.activateClass(msg.classId);
            }
        }
    }
}
