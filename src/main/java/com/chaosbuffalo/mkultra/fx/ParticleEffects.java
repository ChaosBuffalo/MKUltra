package com.chaosbuffalo.mkultra.fx;

import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;


/**
 * Created by Jacob on 3/19/2016.
 */
public class ParticleEffects {
    public static int CIRCLE_MOTION = 0;
    public static int CIRCLE_PILLAR_MOTION = 1;
    public static int SPHERE_MOTION = 2;
    public static int DIRECTED_SPOUT = 3;
    public static int RAIN_EFFECT = 4;

    public static void spawnParticle(int particleID, double speed, Vec3d position, Vec3d heading, World world){
        Vec3d motion = heading.scale(speed);
        world.spawnParticle(EnumParticleTypes.getParticleFromId(particleID),
                position.x, position.y, position.z, motion.x, motion.y, motion.z);
    }

    public static void spawnParticleEffect(int particleID, int motionType, int data,
                                           double speed, int count,
                                           Vec3d position, Vec3d radii,
                                           Vec3d heading, World theWorld) {
        double[] posAndMotion;
        for (int i = 0; i < count; i++) {
            posAndMotion = getPositionAndMotion(motionType, data, position,
                    speed, i, count, radii, heading);
            theWorld.spawnParticle(EnumParticleTypes.getParticleFromId(particleID),
                    posAndMotion[0], posAndMotion[1], posAndMotion[2],
                    posAndMotion[3], posAndMotion[4], posAndMotion[5]);

        }
    }

    private static double[] getPositionAndMotion(int motionType, int data, Vec3d position,
                                                 double speed, int particleNumber, int count,
                                                 Vec3d radii, Vec3d heading) {
        double[] ret = new double[6];
        double degrees = (360.0 / count) * particleNumber;
        if (motionType == CIRCLE_MOTION) {
            Vec3d posVec = new Vec3d(position.x + radii.x * Math.cos(degrees),
                    position.y, position.z + radii.z * Math.sin(degrees));
            Vec3d diffVec = posVec.subtract(position).normalize();
            ret[0] = posVec.x;
            ret[1] = posVec.y;
            ret[2] = posVec.z;
            ret[3] = diffVec.x * speed;
            ret[4] = diffVec.y * speed;
            ret[5] = diffVec.z * speed;
        } else if (motionType == CIRCLE_PILLAR_MOTION) {
            ret[0] = position.z + radii.x * Math.cos(degrees);
            ret[1] = position.y;
            ret[2] = position.y + radii.z * Math.sin(degrees);
            ret[3] = 0;
            ret[4] = speed;
            ret[5] = 0;
        } else if (motionType == SPHERE_MOTION) {
            //Uses data to determine number of layers in sphere
            if (data == 0) {
                data = 1;
            }
            int layerCount = count / data;
            int currentLayer = particleNumber / layerCount;
            int realNum = particleNumber % data;
            double ratio = (double) currentLayer / (double) data;
            double scaledRatio = 2.0 * (ratio - 0.5);
            double realDegrees = (360.0 / data) * realNum;
            double inverseScale = 1.0 - Math.pow(scaledRatio, 2.0);
            Vec3d posVec = new Vec3d(
                    position.x + (radii.x * inverseScale * Math.cos(realDegrees)),
                    scaledRatio * radii.y + position.y,
                    position.z + (radii.z * inverseScale * Math.sin(realDegrees)));
            Vec3d diffVec = posVec.subtract(position).normalize();
            ret[0] = posVec.x;
            ret[1] = posVec.y;
            ret[2] = posVec.z;
            ret[3] = diffVec.x * speed;
            ret[4] = diffVec.y * speed;
            ret[5] = diffVec.z * speed;
        } else if (motionType == DIRECTED_SPOUT) {
            //Uses data to determine direction -1/+1
            Vec3d posVec = new Vec3d(getRandomInRadiusRange(position.x, radii.x),
                    getRandomInRadiusRange(position.y, radii.y),
                    getRandomInRadiusRange(position.z, radii.z));
            ret[0] = posVec.x;
            ret[1] = posVec.y;
            ret[2] = posVec.z;
            ret[3] = heading.x * speed * data;
            ret[4] = heading.y * speed * data;
            ret[5] = heading.z * speed * data;
        } else if (motionType == RAIN_EFFECT){
            Vec3d posVec = new Vec3d(getRandomInRadiusRange(position.x, radii.x),
                    getRandomInRadiusRange(position.y, radii.y),
                    getRandomInRadiusRange(position.z, radii.z));
            Vec3d towards = new Vec3d(posVec.x, posVec.y - 1.0, posVec.z);
            Vec3d diffVec = towards.subtract(posVec);
            ret[0] = posVec.x;
            ret[1] = posVec.y;
            ret[2] = posVec.z;
            ret[3] = diffVec.x * speed;
            ret[4] = diffVec.y * speed;
            ret[5] = diffVec.z * speed;
        }

        return ret;

    }



    private static double getRandomInRadiusRange(double coord, double radius) {
        return coord + ((Math.random() - 0.5) * 2.0) * radius;
    }

}
