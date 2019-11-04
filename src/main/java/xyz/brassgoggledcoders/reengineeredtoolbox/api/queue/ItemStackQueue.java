package xyz.brassgoggledcoders.reengineeredtoolbox.api.queue;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ItemStackQueue extends SocketQueue<ItemStack> {
    public ItemStackQueue() {
        super(5);
    }

    @Override
    protected ItemStack addToBack(ItemStack value) {
        ItemStack remaining = value;
        if (this.getEndOfQueue().isPresent()) {
            mergeStacks(this.getEndOfQueue().get(), value);
        }
        if (anyRemainingValue(remaining) && this.getLength() < this.getQueueSize()) {
            this.push(remaining);
            remaining = ItemStack.EMPTY;
        }
        return remaining;
    }

    @Override
    protected boolean anyRemainingValue(ItemStack value) {
        return !value.isEmpty();
    }

    @Override
    public CompoundNBT serializeValue(ItemStack value) {
        return value.serializeNBT();
    }

    @Override
    public ItemStack deserializeValue(CompoundNBT compoundNBT) {
        return ItemStack.read(compoundNBT);
    }

    private static ItemStack mergeStacks(ItemStack original, ItemStack addition) {
        if (canStacksMerge(original, addition)) {
            int spaceToAdd = original.getMaxStackSize() - original.getCount();
            if (spaceToAdd > 0) {
                int amountToAdd = addition.getCount();
                if (amountToAdd > spaceToAdd) {
                    amountToAdd = spaceToAdd;
                }

                addition.shrink(amountToAdd);
                original.grow(amountToAdd);
            }
        }

        return addition;
    }

    private static boolean canStacksMerge(ItemStack original, ItemStack addition) {
        return ItemStack.areItemsEqual(original, addition) &&
                ItemStack.areItemStackTagsEqual(original, addition);
    }
}
