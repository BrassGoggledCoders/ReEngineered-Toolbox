package xyz.brassgoggledcoders.reengineeredtoolbox.menu;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.ITypedSlot;
import xyz.brassgoggledcoders.reengineeredtoolbox.typedslot.types.item.IItemTypedSlot;

public class SingleTypedSlotMenu extends AbstractContainerMenu {
    private final ITypedSlot<?> typedSlot;

    public SingleTypedSlotMenu(@Nullable MenuType<?> pMenuType, int pContainerId, ITypedSlot<?> typedSlot) {
        super(pMenuType, pContainerId);
        this.typedSlot = typedSlot;
    }

    @Override
    @NotNull
    public ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        if (typedSlot instanceof IItemTypedSlot) {
            Slot slot = this.slots.get(pIndex);
            if (slot.hasItem()) {
                ItemStack slotStack = slot.getItem();
                itemstack = slotStack.copy();
                if (pIndex < this.slots.size() - 1) {
                    if (!this.moveItemStackTo(slotStack, this.slots.size() - 1, this.slots.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(slotStack, 0, this.slots.size() - 1, false)) {
                    return ItemStack.EMPTY;
                }

                if (slotStack.isEmpty()) {
                    slot.set(ItemStack.EMPTY);
                } else {
                    slot.setChanged();
                }
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return false;
    }
}
