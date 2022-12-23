package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.item;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotType;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.TypedSlotTypes;

public interface IItemTypedSlot extends ITypedSlot<ItemStack> {
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
}
