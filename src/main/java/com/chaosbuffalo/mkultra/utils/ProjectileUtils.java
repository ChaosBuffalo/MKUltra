package com.chaosbuffalo.mkultra.utils;

import net.minecraft.util.math.Vec3d;

import javax.vecmath.Vector3d;

public class ProjectileUtils {

    public static class BallisticResult {
        public Vec3d lowArc;
        public Vec3d highArc;
        public boolean hasHighArc;
        public boolean foundSolution;

        public BallisticResult(Vec3d lowArc, Vec3d highArc){
            this.lowArc = lowArc;
            this.highArc = highArc;
            this.hasHighArc = true;
            this.foundSolution = true;
        }

        public BallisticResult(Vec3d lowArc){
            this.lowArc = lowArc;
            this.hasHighArc = false;
            this.foundSolution = true;
        }

        public BallisticResult(){
            this.foundSolution = false;
            this.hasHighArc = false;
        }
    }

    public static BallisticResult solveBallisticArcStationaryTarget(Vec3d projPos, Vec3d target,
                                                                    float velocity, float gravity) {


        Vec3d diff = target.subtract(projPos);
        Vec3d diffXZ = new Vec3d(diff.x, 0.0, diff.y);
        double groundDist = diff.length();
        float vel2 = velocity*velocity;
        float vel4 = velocity*velocity*velocity*velocity;
        double y = diff.y;
        double x = groundDist;
        double gx = gravity*x;
        double root = vel4 - gravity*(gravity*x*x + 2*y*vel2);

        if (root < 0){
            return new BallisticResult();
        }

        root = Math.sqrt(root);

        double lowAng = Math.atan2(vel2 - root, gx);
        double highAng = Math.atan2(vel2 + root, gx);
        Vec3d heading = diffXZ.normalize();
        Vec3d lowArc = heading.scale(Math.cos(lowAng)*velocity)
                .add(new Vec3d(0.0, 1.0, 0.0)
                        .scale(Math.sin(lowAng)*velocity));
        if (lowAng != highAng){
            Vec3d highArc = heading.scale(Math.cos(highAng)*velocity)
                    .add(new Vec3d(0.0, 1.0, 0.0)
                            .scale(Math.sin(highAng)*velocity));
            return new BallisticResult(lowArc, highArc);
        } else {
            return new BallisticResult(lowArc);
        }
    }
}
