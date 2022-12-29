package xyz.brassgoggledcoders.reengineeredtoolbox.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
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

    public static <T> T readValue(@Nullable FriendlyByteBuf friendlyByteBuf, Function<FriendlyByteBuf,T> convertor, Supplier<T> backup) {
        if (friendlyByteBuf != null) {
            return convertor.apply(friendlyByteBuf);
        } else {
            return backup.get();
        }
    }
}
