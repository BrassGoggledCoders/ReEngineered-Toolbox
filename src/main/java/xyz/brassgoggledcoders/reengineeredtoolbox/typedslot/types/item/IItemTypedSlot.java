package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.item;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotType;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotTypes;

public interface IItemTypedSlot extends ITypedSlot<ItemStack>, IItemHandler {
    ItemStack extract(int amount, boolean simulate);

    ItemStack insert(ItemStack itemStack, boolean simulate);

    boolean isValid(ItemStack itemStack);

    int getSlotLimit();

    @Override
    default boolean allowMenuClick(@NotNull ItemStack itemStack) {
        return this.isValid(itemStack);
    }

    @Override
    @NotNull
    default TypedSlotType getType() {
        return TypedSlotTypes.ITEM.get();
    }

    @Override
    default int getSlots() {
        return 1;
    }

    @Override
    default boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return slot == 0 && this.isValid(stack);
    }

    @Override
    @NotNull
    default ItemStack getStackInSlot(int slot) {
        return slot == 0 ? this.getContent() : ItemStack.EMPTY;
    }

    @Override
    @NotNull
    default ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return slot == 0 ? this.insert(stack, simulate) : stack;
    }

    @Override
    @NotNull
    default ItemStack extractItem(int slot, int amount, boolean simulate) {
        return slot == 0 ? this.extract(amount, simulate) : ItemStack.EMPTY;
    }

    @Override
    default int getSlotLimit(int slot) {
        return slot == 0 ? this.getSlotLimit() : 64;
    }
}
