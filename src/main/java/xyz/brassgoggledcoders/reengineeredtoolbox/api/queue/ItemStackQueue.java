package xyz.brassgoggledcoders.reengineeredtoolbox.api.queue;

import com.teamacronymcoders.base.util.ItemStackUtils;
import net.minecraft.item.ItemStack;

import java.util.Optional;

public class ItemStackQueue extends SocketQueue<ItemStack> {
    public ItemStackQueue() {
        super(5);
    }

    @Override
    protected ItemStack addToBack(ItemStack value) {
        ItemStack remaining = value;
        if (this.getEndOfQueue().isPresent()) {
            remaining = ItemStackUtils.mergeStacks(this.getEndOfQueue().get(), value);
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
}
