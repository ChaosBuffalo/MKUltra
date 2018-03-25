package com.chaosbuffalo.mkultra.network.packets.server;

import com.chaosbuffalo.mkultra.fx.ParticleEffects;
import com.chaosbuffalo.mkultra.network.MessageHandler;
import com.chaosbuffalo.mkultra.utils.ClientUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;


public class ParticleEffectSpawnPacket implements IMessage {
    private double xPos;
    private double yPos;
    private double zPos;
    private int motionType;
    private double speed;
    private int count;
    private double radiusX;
    private double radiusY;
    private double radiusZ;
    private int particleID;
    private int data;
    private double headingX;
    private double headingY;
    private double headingZ;

    public ParticleEffectSpawnPacket() {
    }


    public ParticleEffectSpawnPacket(int particleID, int motionType, int count, int data,
                                     double xPos, double yPos, double zPos,
                                     double radiusX, double radiusY, double radiusZ,
                                     double speed, double headingX, double headingY, double headingZ) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.motionType = motionType;
        this.count = count;
        this.speed = speed;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
        this.radiusZ = radiusZ;
        this.particleID = particleID;
        this.data = data;
        this.headingX = headingX;
        this.headingY = headingY;
        this.headingZ = headingZ;
    }

    public ParticleEffectSpawnPacket(int particleID, int motionType, int count, int data,
                                     double xPos, double yPos, double zPos,
                                     double radiusX, double radiusY, double radiusZ,
                                     double speed, Vec3d headingVec) {
        this(particleID, motionType, count, data,
                xPos, yPos, zPos,
                radiusX, radiusY, radiusZ, speed,
                headingVec.x, headingVec.y, headingVec.z);
    }

    public ParticleEffectSpawnPacket(int particleID, int motionType, int count, int data,
                                     Vec3d posVec,
                                     double radiusX, double radiusY, double radiusZ,
                                     double speed, Vec3d headingVec) {
        this(particleID, motionType, count, data,
                posVec.x, posVec.y, posVec.z,
                radiusX, radiusY, radiusZ, speed,
                headingVec.x, headingVec.y, headingVec.z);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.particleID = buf.readInt();
        this.motionType = buf.readInt();
        this.data = buf.readInt();
        this.count = buf.readInt();
        this.xPos = buf.readDouble();
        this.yPos = buf.readDouble();
        this.zPos = buf.readDouble();
        this.radiusX = buf.readDouble();
        this.radiusY = buf.readDouble();
        this.radiusZ = buf.readDouble();
        this.speed = buf.readDouble();
        this.headingX = buf.readDouble();
        this.headingY = buf.readDouble();
        this.headingZ = buf.readDouble();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.particleID);
        buf.writeInt(this.motionType);
        buf.writeInt(this.data);
        buf.writeInt(this.count);
        buf.writeDouble(this.xPos);
        buf.writeDouble(this.yPos);
        buf.writeDouble(this.zPos);
        buf.writeDouble(this.radiusX);
        buf.writeDouble(this.radiusY);
        buf.writeDouble(this.radiusZ);
        buf.writeDouble(this.speed);
        buf.writeDouble(this.headingX);
        buf.writeDouble(this.headingY);
        buf.writeDouble(this.headingZ);
    }

    public static class Handler extends MessageHandler.Client<ParticleEffectSpawnPacket> {

        // Client reads the serialized data from the server
        @Override
        public IMessage handleClientMessage(final EntityPlayer player,
                                            final ParticleEffectSpawnPacket msg,
                                            MessageContext ctx) {
            ClientUtils.addScheduledTask(() -> ParticleEffects.spawnParticleEffect(
                    msg.particleID, msg.motionType, msg.data, msg.speed, msg.count,
                    new Vec3d(msg.xPos, msg.yPos, msg.zPos),
                    new Vec3d(msg.radiusX, msg.radiusY, msg.radiusZ),
                    new Vec3d(msg.headingX, msg.headingY, msg.headingZ),
                    player.world));
            return null;
        }
    }
}
