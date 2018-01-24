package xyz.brassgoggledcoders.reengineeredtoolbox.api.queue;

import com.teamacronymcoders.base.util.ItemStackUtils;
import net.minecraft.item.ItemStack;

public class ItemStackQueue extends SocketQueue<ItemStack> {
    public ItemStackQueue() {
        super(5);
    }

    @Override
    protected ItemStack simulateOffer(ItemStack value) {
        return null;
    }

    @Override
    protected ItemStack addToBack(ItemStack value) {
        ItemStack remaining = value;
        if (this.getEndOfQueue().isPresent()) {
            remaining = ItemStackUtils.mergeStacks(this.getEndOfQueue().get(), value);
        }
        return remaining;
    }

    @Override
    protected boolean anyRemainingValue(ItemStack value) {
        return !value.isEmpty();
    }
}
