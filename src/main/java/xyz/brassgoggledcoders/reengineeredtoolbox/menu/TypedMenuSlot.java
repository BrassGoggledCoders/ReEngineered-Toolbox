package xyz.brassgoggledcoders.reengineeredtoolbox.menu;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlotHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.SingleSlotTypedSlotHolder;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.item.IItemTypedSlot;

import javax.annotation.Nonnull;

public class TypedMenuSlot extends Slot {
    private static final Container EMPTY_INVENTORY = new SimpleContainer(0);

    private final ITypedSlotHolder hypper;

    public TypedMenuSlot(ITypedSlotHolder hypper, int index, int xPosition, int yPosition) {
        super(EMPTY_INVENTORY, index, xPosition, yPosition);
        this.hypper = hypper;
    }

    public TypedMenuSlot(ITypedSlot<?> typedSlot, int index, int xPosition, int yPosition) {
        this(new SingleSlotTypedSlotHolder(typedSlot), index, xPosition, yPosition);
    }

    @NotNull
    public ITypedSlot<?> getHypperSlot() {
        return hypper.getSlots()[this.getContainerSlot()];
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return this.getHypperSlot().allowMenuClick(stack);
    }

    @Override
    @NotNull
    public ItemStack getItem() {
        if (this.getHypperSlot().getContent() instanceof ItemStack itemStack) {
            return itemStack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    @NotNull
    public ItemStack safeInsert(@NotNull ItemStack itemStack, int count) {
        itemStack = this.getHypperSlot().menuClick(itemStack);
        return super.safeInsert(itemStack, count);
    }

    @Override
    public void set(@NotNull ItemStack stack) {
        if (this.getHypperSlot() instanceof IItemTypedSlot itemHypperSlot) {
            itemHypperSlot.setContent(stack);
            this.setChanged();
        }
    }

    @Override
    public void onQuickCraft(@Nonnull ItemStack oldStackIn, @Nonnull ItemStack newStackIn) {

    }

    @Override
    public int getMaxStackSize() {
        if (this.getHypperSlot() instanceof IItemTypedSlot itemHypperSlot) {
            return itemHypperSlot.getSlotLimit();
        } else {
            return 0;
        }
    }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        ItemStack maxAdd = stack.copy();
        int maxInput = stack.getMaxStackSize();
        maxAdd.setCount(maxInput);

        if (this.getHypperSlot() instanceof IItemTypedSlot itemHypperSlot) {
            ItemStack currentStack = itemHypperSlot.getContent();

            itemHypperSlot.setContent(ItemStack.EMPTY);
            ItemStack remainder = itemHypperSlot.insert(maxAdd, true);
            itemHypperSlot.setContent(currentStack);

            return maxInput - remainder.getCount();
        } else {
            return 0;
        }

    }

    @Override
    public boolean mayPickup(@NotNull Player playerIn) {
        if (this.getHypperSlot() instanceof IItemTypedSlot itemHypperSlot) {
            return !itemHypperSlot.extract(1, true).isEmpty();
        } else {
            return false;
        }
    }

    @Override
    @Nonnull
    public ItemStack remove(int amount) {
        if (this.getHypperSlot() instanceof IItemTypedSlot itemHypperSlot) {
            return itemHypperSlot.extract(amount, false);
        } else {
            return ItemStack.EMPTY;
        }
    }

}
