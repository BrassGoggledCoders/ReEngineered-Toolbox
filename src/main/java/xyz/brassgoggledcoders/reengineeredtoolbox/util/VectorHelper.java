package xyz.brassgoggledcoders.reengineeredtoolbox.util;

import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
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
}
