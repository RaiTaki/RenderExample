package xyz.raitaki.renderexample.Utils;

import net.minecraft.util.math.Vec3d;

public class VectorUtils {

    public static final Vec3d rotateAroundAxisY(Vec3d v, double angle) {

        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;

        return new Vec3d(x, v.getY(), z);
    }

    public static final Vec3d rotateAroundAxisX(Vec3d v, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double y = v.getY() * cos - v.getZ() * sin;
        double z = v.getY() * sin + v.getZ() * cos;
        return new Vec3d(v.getX(), y, z);
    }
}
