package xyz.brassgoggledcoders.reengineeredtoolbox.api.util;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public class VectorHelper {
    public static Vec3 withRandomOffset(Vec3i center, RandomSource random, int size) {
        double sizeHalved = size / 2D;
        return new Vec3(
                center.getX() + 0.5D + (random.nextFloat() * size) - sizeHalved,
                center.getY() + 0.5D + (random.nextFloat() * size) - sizeHalved,
                center.getZ() + 0.5D + (random.nextFloat() * size) - sizeHalved
        );
    }

    public static Vec3 centered(Vec3i vector) {
        return new Vec3(
                vector.getX() + 0.5,
                vector.getY() + 0.5,
                vector.getZ() + 0.5
        );
    }

    public static Vec2 convertToVec2(Vec3 vec3, Direction direction) {
        double x;
        double y;
        if (direction.getAxis().isHorizontal()) {
            y = vec3.y() - (int) vec3.y();
        } else {
            y = direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? vec3.z() : -vec3.z();
        }

        y = Math.abs(y);
        y = y - (int) y;
        y *= 16D;

        if (direction == Direction.DOWN) {
            y = 16 - y;
        }


        x = switch (direction.getAxis()) {
            case X -> vec3.z();
            case Y -> direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? vec3.x() : -vec3.x();
            case Z -> vec3.x();
        };

        x = Math.abs(x);
        x = x - (int) x;
        x *= 16D;

        if (direction == Direction.WEST || direction == Direction.SOUTH || direction.getAxis() == Direction.Axis.Y) {
            x = 16 - x;
        }

        return new Vec2((float) x, (float) y);
    }
}
