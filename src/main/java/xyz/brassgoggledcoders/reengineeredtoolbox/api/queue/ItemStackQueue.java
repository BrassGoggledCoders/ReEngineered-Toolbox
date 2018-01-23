package xyz.brassgoggledcoders.reengineeredtoolbox.api.queue;

import net.minecraft.item.ItemStack;

public class ItemQueue extends SocketQueue<ItemStack> {
    public ItemQueue(int initialSize) {
        super(initialSize);
    }

    @Override
    protected ItemStack checkFit(ItemStack value) {
        return null;
    }

    @Override
    protected ItemStack addToBack(ItemStack value, boolean simulate) {
        return null;
    }

    @Override
    protected boolean anyRemainingValue(ItemStack value) {
        return false;
    }
}
