package xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.item;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.capability.IFrequencySlotItemHandler;
import xyz.brassgoggledcoders.reengineeredtoolbox.api.frame.slot.FrameSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.capabilities.IOStyle;

public class FrequencyBackedItemHandler implements IItemHandler {
    private final FrameSlot[] frameSlots;
    private final LazyOptional<IFrequencySlotItemHandler> backingItemHandler;
    private final IOStyle ioStyle;

    public FrequencyBackedItemHandler(FrameSlot frameSlot, LazyOptional<IFrequencySlotItemHandler> backingItemHandler, IOStyle ioStyle) {
        this(new FrameSlot[]{frameSlot}, backingItemHandler, ioStyle);
    }

    public FrequencyBackedItemHandler(FrameSlot[] frameSlots, LazyOptional<IFrequencySlotItemHandler> backingItemHandler, IOStyle ioStyle) {
        this.frameSlots = frameSlots;
        this.backingItemHandler = backingItemHandler;
        this.ioStyle = ioStyle;
    }

    @Override
    public int getSlots() {
        return this.frameSlots.length;
    }

    @Override
    @NotNull
    public ItemStack getStackInSlot(int slot) {
        return this.backingItemHandler.map(itemHandler -> itemHandler.getStackInSlot(frameSlots[slot].getFrequency()))
                .orElse(ItemStack.EMPTY);
    }

    @Override
    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (ioStyle.isAllowInsert()) {
            return this.backingItemHandler.map(itemHandler -> itemHandler.insertItem(frameSlots[slot].getFrequency(), stack, simulate))
                    .orElse(stack);
        }

        return stack;
    }

    @Override
    @NotNull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (ioStyle.isAllowExtract()) {
            return this.backingItemHandler.map(itemHandler -> itemHandler.extractItem(frameSlots[slot].getFrequency(), amount, simulate))
                    .orElse(ItemStack.EMPTY);
        }

        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return this.backingItemHandler.map(itemHandler -> itemHandler.isItemValid(frameSlots[slot].getFrequency(), stack))
                .orElse(false);
    }
}
