package xyz.brassgoggledcoders.reengineeredtoolbox.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ByteBufHelper {
    public static <T> T readRegistryValue(@Nullable FriendlyByteBuf friendlyByteBuf, BiConsumer<T, CompoundTag> setup, Supplier<T> backup) {
        if (friendlyByteBuf != null) {
            T value = friendlyByteBuf.readRegistryId();
            CompoundTag tag = friendlyByteBuf.readNbt();
            setup.accept(value, tag);
            return value;
        } else {
            return backup.get();
        }
    }

    public static <T> T readValue(@Nullable FriendlyByteBuf friendlyByteBuf, Function<FriendlyByteBuf, T> convertor, Supplier<T> backup) {
        if (friendlyByteBuf != null) {
            return convertor.apply(friendlyByteBuf);
        } else {
            return backup.get();
        }
    }

    public static void writeVector3d(Vec3 vector3d, FriendlyByteBuf buffer) {
        buffer.writeDouble(vector3d.x);
        buffer.writeDouble(vector3d.y);
        buffer.writeDouble(vector3d.z);
    }

    public static Vec3 readVector3d(FriendlyByteBuf buffer) {
        return new Vec3(
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble()
        );
    }
}
