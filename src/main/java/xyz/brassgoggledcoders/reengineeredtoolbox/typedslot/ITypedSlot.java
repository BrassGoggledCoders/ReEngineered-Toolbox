package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ITypedSlot<U> {

    @NotNull
    TypedSlotType getType();

    U getContent();

    void setContent(U content);

    CompoundTag toNBT();

    void fromNBT(CompoundTag compoundTag);

    default void setOnChange(Runnable runnable) {

    }

    default void onChange() {

    }

    default void onReplaced(@NotNull ITypedSlotHolder hypper, @Nullable ITypedSlot<?> replacement) {
    }

    default boolean allowMenuClick(@NotNull ItemStack itemStack) {
        return false;
    }

    @NotNull
    default ItemStack menuClick(@NotNull ItemStack itemStack) {
        return itemStack;
    }
}
