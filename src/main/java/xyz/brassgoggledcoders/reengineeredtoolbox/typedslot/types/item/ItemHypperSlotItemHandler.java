package xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.item;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlotHolder;

public class ItemHypperSlotItemHandler implements IItemHandlerModifiable {
    private final ITypedSlotHolder typedSlotHolder;

    public ItemHypperSlotItemHandler(ITypedSlotHolder typedSlotHolder) {
        this.typedSlotHolder = typedSlotHolder;
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        IItemTypedSlot itemHypperSlot = this.getItemHyperSlot(slot);
        if (itemHypperSlot != null) {
            itemHypperSlot.setContent(stack);
        }
    }

    @Override
    public int getSlots() {
        int slots = 0;
        for (ITypedSlot<?> slot : this.typedSlotHolder.getSlots()) {
            if (slot instanceof IItemTypedSlot) {
                slots++;
            }
        }
        return slots;
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        IItemTypedSlot itemHypperSlot = this.getItemHyperSlot(slot);
        if (itemHypperSlot != null) {
            return itemHypperSlot.getContent();
        }
        return ItemStack.EMPTY;
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        IItemTypedSlot itemHypperSlot = this.getItemHyperSlot(slot);
        if (itemHypperSlot != null) {
            return itemHypperSlot.insert(stack, simulate);
        }
        return stack;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        IItemTypedSlot itemHypperSlot = this.getItemHyperSlot(slot);
        if (itemHypperSlot != null) {
            return itemHypperSlot.extract(amount, simulate);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        IItemTypedSlot itemHypperSlot = this.getItemHyperSlot(slot);
        if (itemHypperSlot != null) {
            return itemHypperSlot.getSlotLimit();
        }
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        IItemTypedSlot itemHypperSlot = this.getItemHyperSlot(slot);
        if (itemHypperSlot != null) {
            return itemHypperSlot.isValid(stack);
        }
        return false;
    }

    private IItemTypedSlot getItemHyperSlot(int slotNum) {
        int slots = 0;
        for (ITypedSlot<?> slot : this.typedSlotHolder.getSlots()) {
            if (slot instanceof IItemTypedSlot itemHypperSlot) {
                if (slots == slotNum) {
                    return itemHypperSlot;
                }
                slots++;
            }
        }
        return null;
    }
}
